/*
 * FILE:    rtp_callback.c
 * PROGRAM: RAT
 * AUTHOR:  Colin Perkins / Orion Hodson
 *
 * Copyright (c) 1999-2001 University College London
 * All rights reserved.
 */
 
#ifndef HIDE_SOURCE_STRINGS
static const char cvsid[] = 
	"$Id: rtp_callback.c,v 1.1 2002/02/04 13:23:34 Psycho Exp $";
#endif /* HIDE_SOURCE_STRINGS */

#include "config_unix.h"
#include "config_win32.h"
#include "debug.h"
#include "audio_types.h"
#include "auddev.h"
#include "channel.h"
#include "codec.h"
#include "rtp.h"
#include "ntp.h"
#include "session.h"
#include "cushion.h"
#include "pdb.h"
#include "source.h"
#include "playout_calc.h"
#include "util.h"
#include "ui_send_rtp.h"
#include "rtp_callback.h"
#include "rtp_dump.h"

/* We need to be able to resolve the rtp session to a rat session in */
/* order to get persistent participant information, etc.  We use a   */
/* double linked list with sentinel for this.  We normally don't     */
/* expect to have more than 2 sessions (i.e. transcoder mode), but   */
/* layered codecs may require more.                                  */

typedef struct s_rtp_assoc {
        struct s_rtp_assoc *next;
        struct s_rtp_assoc *prev;
        struct rtp         *rtps;
        struct s_session   *rats;
} rtp_assoc_t;

/* Sentinel for linked list that is used as small associative array */
static rtp_assoc_t rtp_as;
static int rtp_as_inited;

void 
rtp_callback_init(struct rtp *rtps, struct s_session *rats)
{
        rtp_assoc_t *cur, *sentinel;

        if (!rtp_as_inited) {
                /* First pass sentinel initialization */
                rtp_as.next = &rtp_as;
                rtp_as.prev = &rtp_as;
                rtp_as_inited = 1;
        }

        sentinel = &rtp_as;
        cur   = sentinel->next;

        while (cur != sentinel) {
                if (cur->rtps == rtps) {
                        /* Association already exists, over-riding */
                        cur->rats = rats;
                        return;
                }
		cur = cur->next;
        }

        cur = (rtp_assoc_t*)xmalloc(sizeof(rtp_assoc_t));
        cur->rtps   = rtps;
        cur->rats   = rats;

        cur->next       = sentinel->next;
        cur->prev       = sentinel;
        cur->next->prev = cur;
        cur->prev->next = cur;
}

void 
rtp_callback_exit(struct rtp *rtps)
{
        rtp_assoc_t *cur, *sentinel;
        
        sentinel = &rtp_as;
        cur = sentinel->next;
        while(cur != sentinel) {
                if (cur->rtps == rtps) {
                        cur->prev->next = cur->next;
                        cur->next->prev = cur->prev;
                        xfree(cur);
                        return;
                }
                cur = cur->next;
        }
}

/* get_session maps an rtp_session to a rat session */
static struct s_session *
get_session(struct rtp *rtps)
{
        rtp_assoc_t *cur, *sentinel;

        if (!rtp_as_inited) {
                return NULL;
        }
        
        sentinel = &rtp_as;
        cur = sentinel->next;
        while(cur != sentinel) {
                if (cur->rtps == rtps) {
                        return cur->rats;
                }
                cur = cur->next;
        }        
        return NULL;
}

/* Callback utility functions                                                */

/* rtp_rtt_calc return rtt estimate in seconds */
static double 
rtp_rtt_calc(uint32_t arr, uint32_t dep, uint32_t dlsr)
{
        uint32_t delta;

        /* rtt = arr - dep - delay */
        delta = ntp32_sub(arr, dep);
        if (delta >= dlsr) {
                delta -= dlsr;
        } else {
                /* Clock skew bigger than transit delay  
                 * or garbage dlsr value ?*/
                debug_msg("delta_ntp (%d) > dlsr (%d)\n", delta, dlsr);
                delta = 0;
        }
        return delta / 65536.0;
}

static void
process_rtp_data(session_t *sp, uint32_t ssrc, rtp_packet *p)
{
        struct s_source *s;
        pdb_entry_t     *e;

        if (sp->filter_loopback && ssrc == rtp_my_ssrc(sp->rtp_session[0])) {
                /* This packet is from us and we are filtering our own       */
                /* packets.                                                  */
                xfree(p);
                return;
        }

        if (pdb_item_get(sp->pdb, ssrc, &e) == FALSE) {
                debug_msg("Packet discarded: unknown source (0x%08x).\n", ssrc);
                xfree(p);
                return;
        }
        e->received++;
        
        s = source_get_by_ssrc(sp->active_sources, ssrc);
        if (s == NULL) {
                s = source_create(sp->active_sources, ssrc, sp->pdb);
                ui_send_rtp_active(sp, sp->mbus_ui_addr, ssrc);
                debug_msg("Source created\n");
        }

	/* Calculate the relative playout delay, for this source. Needed for lip-sync. */

	/* Discard packet if output is muted... no point wasting time decoding it... */
        if ((sp->playing_audio == FALSE) || (e->mute)) {
                xfree(p);
                return;
        }

	/* Remove any padding */
	if (p->p) {
		p->data_len -= p->data[p->data_len - 1];
		p->p = 0;
	}
        source_add_packet(s, p);
}

static void
process_rr(session_t *sp, rtp_event *rtp_e)
{
        pdb_entry_t  	*e;
        uint32_t 	 fract_lost, my_ssrc;
	uint32_t	 ssrc = rtp_e->ssrc;
	rtcp_rr		*r = (rtcp_rr *) rtp_e->data;

        /* Calculate rtt estimate */
        my_ssrc =  rtp_my_ssrc(sp->rtp_session[0]);
        if (pdb_item_get(sp->pdb, r->ssrc, &e) == FALSE) {
                /* Maybe deleted or not heard from yet */
                debug_msg("Receiver report on unknown participant (0x%lx)\n", r->ssrc);
                return;
        }

        if (pdb_item_get(sp->pdb, ssrc, &e) &&
            r->ssrc == my_ssrc &&
            r->ssrc != ssrc    && /* filter self reports */
            r->lsr != 0) {
                uint32_t ntp_sec, ntp_frac, ntp32;
                double rtt;
                ntp64_time(&ntp_sec, &ntp_frac);
                ntp32 = ntp64_to_ntp32(ntp_sec, ntp_frac);
                    
                rtt = rtp_rtt_calc(ntp32, r->lsr, r->dlsr);
                /*
                 * Filter out blatantly wrong rtt values.  Some tools might not
                 * implement dlsr and lsr (broken) or forget to do byte-swapping
                 */
                if (rtt < 100.0) {
                        e->last_rtt = rtt;
                        debug_msg("rtt %f\n", rtt);
                } else {
                        debug_msg("Junk rtt (%f secs) ntp32 0x%08x lsr 0x%08x dlsr 0x%08x ?\n", rtt, ntp32, r->lsr, r->dlsr);
                }
                if (e->avg_rtt == 0.0) {
                        e->avg_rtt = e->last_rtt;
                } else {
                        e->avg_rtt += (e->last_rtt - e->avg_rtt) / 2.0;
                }
                if (sp->mbus_engine != NULL) {
                        ui_send_rtp_rtt(sp, sp->mbus_ui_addr, ssrc, e->avg_rtt);
                }
        }
	fract_lost = (r->fract_lost * 100) >> 8;
            
        /* Update loss stats */
        if (sp->mbus_engine != NULL) {
                ui_send_rtp_packet_loss(sp, sp->mbus_ui_addr, ssrc, r->ssrc, fract_lost);
        }

	/* Do we have to log anything? */
	if ((r->ssrc == my_ssrc) && (sp->logger != NULL)) {
		rtpdump_header(sp->logger, "rtt       ", rtp_e);
		fprintf(sp->logger, "%f\n", e->avg_rtt);
	}
}
        
static void
process_rr_timeout(session_t *sp, uint32_t ssrc, rtcp_rr *r)
{
        /* Just update loss statistic in UI for this report if there */
        /* is somewhere to send them.                                */
        if (sp->mbus_engine != NULL) {
                ui_send_rtp_packet_loss(sp, sp->mbus_ui_addr, ssrc, r->ssrc, 101);
        }
}

static void
process_sdes(session_t *sp, uint32_t ssrc, rtcp_sdes_item *d)
{
        pdb_entry_t *e;

	assert(pdb_item_get(sp->pdb, ssrc, &e) == TRUE);

        if (sp->mbus_engine == NULL) {
                /* Nowhere to send updates to, so ignore them.               */
                return;
        }

        switch(d->type) {
        case RTCP_SDES_END:
                /* This is the end of the SDES list of a packet.  Nothing    */
                /* for us to deal with.                                      */
                break;
        case RTCP_SDES_CNAME:
                ui_send_rtp_cname(sp, sp->mbus_ui_addr, ssrc);
                break;
        case RTCP_SDES_NAME:
                ui_send_rtp_name(sp, sp->mbus_ui_addr, ssrc);
                break;
        case RTCP_SDES_EMAIL:
                ui_send_rtp_email(sp, sp->mbus_ui_addr, ssrc);
                break;
        case RTCP_SDES_PHONE:
                ui_send_rtp_phone(sp, sp->mbus_ui_addr, ssrc);
                break;
        case RTCP_SDES_LOC:
                ui_send_rtp_loc(sp, sp->mbus_ui_addr, ssrc);
                break;
        case RTCP_SDES_TOOL:
                ui_send_rtp_tool(sp, sp->mbus_ui_addr, ssrc);
                break;
        case RTCP_SDES_NOTE:
                ui_send_rtp_note(sp, sp->mbus_ui_addr, ssrc);
                break;
        case RTCP_SDES_PRIV:
		ui_send_rtp_priv(sp, sp->mbus_ui_addr, ssrc);
                break;
        default:
                debug_msg("Ignoring SDES type (0x%02x) from (0x%08x).\n", ssrc);
        }
}

static void
process_create(session_t *sp, uint32_t ssrc)
{
	if (pdb_item_create(sp->pdb, (uint16_t)ts_get_freq(sp->cur_ts), ssrc) == FALSE) {
		debug_msg("Unable to create source 0x%08lx\n", ssrc);
	}
}

static void
process_delete(session_t *sp, rtp_event *e)
{
        if (e->ssrc != rtp_my_ssrc(sp->rtp_session[0]) && sp->mbus_engine != NULL) {
                struct s_source *s;
                pdb_entry_t     *pdbe;
                if ((s = source_get_by_ssrc(sp->active_sources, e->ssrc)) != NULL) {
                        source_remove(sp->active_sources, s);
                }
                if (pdb_item_get(sp->pdb, e->ssrc, &pdbe)) {
                        pdb_item_destroy(sp->pdb, e->ssrc);
                        /* Will not be in ui if not in pdb */
                        ui_send_rtp_remove(sp, sp->mbus_ui_addr, e->ssrc);
                }
        }
}

void 
rtp_callback_proc(struct rtp *s, rtp_event *e)
{
        struct s_session *sp;
	int		  i;
        
	assert(s != NULL);
	assert(e != NULL);

        sp = get_session(s);
        if (sp == NULL) {
                /* Should only happen when SOURCE_CREATED is generated in */
                /* rtp_init.                                              */
                debug_msg("Could not find session (0x%08x)\n", (uint32_t)s);
                return;
        }

	if (sp->logger != NULL) {
		rtpdump_callback(sp->logger, e);
	}

	/* If we're a transcoder, add this source as a CSRC on the other session */
	if (sp->other_session != NULL) {
		for (i = 0; i < sp->other_session->rtp_session_count; i++) {
			rtp_add_csrc(sp->other_session->rtp_session[i], e->ssrc);
		}
	}

	switch (e->type) {
	case RX_RTP:
                process_rtp_data(sp, e->ssrc, (rtp_packet*)e->data);
                break;
	case RX_RTCP_START:
		break;
	case RX_RTCP_FINISH:
		break;
	case RX_SR:
		break;
	case RX_RR:
                process_rr(sp, e);
		break;
	case RX_RR_EMPTY:
		break;
	case RX_SDES:
                process_sdes(sp, e->ssrc, (rtcp_sdes_item*)e->data);
		break;
        case RX_APP:
                debug_msg("Received and ignored application specific report from %08x\n", e->ssrc);
                break;
	case RX_BYE:
	case SOURCE_DELETED:
                process_delete(sp, e);
		if (sp->other_session != NULL) {
			for (i = 0; i < sp->other_session->rtp_session_count; i++) {
				rtp_del_csrc(sp->other_session->rtp_session[i], e->ssrc);
			}
		}
		break;
	case SOURCE_CREATED:
		process_create(sp, e->ssrc);
		break;
	case RR_TIMEOUT:
                process_rr_timeout(sp, e->ssrc, (rtcp_rr*)e->data);
		break;
	default:
		debug_msg("Unknown RTP event (type=%d)\n", e->type);
		abort();
	}
}


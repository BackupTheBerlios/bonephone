/*   
 * FILE:     auddev_alsa.h
 * PROGRAM:  RAT
 * AUTHOR:   Orion Hodson
 *
 * $Revision: 1.1 $
 * $Date: 2002/02/04 13:23:34 $
 *
 * Copyright (c) 1998-2001 University College London
 * All rights reserved.
 *
 */

#ifndef _AUDDEV_ALSA_H_
#define _AUDDEV_ALSA_H_

int  alsa_audio_open       (audio_desc_t ad, audio_format* ifmt, audio_format *ofmt);
void alsa_audio_close      (audio_desc_t ad);
void alsa_audio_drain      (audio_desc_t ad);
int  alsa_audio_duplex     (audio_desc_t ad);
void alsa_audio_set_igain   (audio_desc_t ad, int gain);
int  alsa_audio_get_igain   (audio_desc_t ad);
void alsa_audio_set_ogain (audio_desc_t ad, int vol);
int  alsa_audio_get_ogain (audio_desc_t ad);
void alsa_audio_loopback   (audio_desc_t ad, int gain);
int  alsa_audio_read       (audio_desc_t ad, u_char *buf, int buf_bytes);
int  alsa_audio_write      (audio_desc_t ad, u_char *buf, int buf_bytes);
void alsa_audio_non_block  (audio_desc_t ad);
void alsa_audio_block      (audio_desc_t ad);

void         alsa_audio_oport_set   (audio_desc_t ad, audio_port_t port);
audio_port_t alsa_audio_oport_get   (audio_desc_t ad);
int          alsa_audio_oport_count (audio_desc_t ad);
const audio_port_details_t*
             alsa_audio_oport_details (audio_desc_t ad, int idx);

void         alsa_audio_iport_set   (audio_desc_t ad, audio_port_t port);
audio_port_t alsa_audio_iport_get   (audio_desc_t ad);
int          alsa_audio_iport_count (audio_desc_t ad);
const audio_port_details_t*
             alsa_audio_iport_details (audio_desc_t ad, int idx);

int  alsa_audio_is_ready  (audio_desc_t ad);
void alsa_audio_wait_for  (audio_desc_t ad, int delay_ms);
int  alsa_audio_supports  (audio_desc_t ad, audio_format *fmt);

/* Functions to get names of alsa devices */
int         alsa_audio_init (void);             /* This fn works out what we have           */
int         alsa_get_device_count    (void);             /* Then this one tells us the number of 'em */
char       *alsa_get_device_name     (audio_desc_t idx); /* Then this one tells us the name          */

#endif /* _AUDDEV_ALSA_H_ */

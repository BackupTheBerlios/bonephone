/*
 * FILE:    main_ui.c
 * PROGRAM: RAT
 * AUTHORS: Colin Perkins 
 *
 * Copyright (c) 1999-2001 University College London
 * All rights reserved.
 */
 
#ifndef HIDE_SOURCE_STRINGS
static const char cvsid[] = 
	"$Id: main_ui.c,v 1.1 2002/02/04 13:23:35 Psycho Exp $";
#endif /* HIDE_SOURCE_STRINGS */


#include "config_unix.h"
#include "config_win32.h"
#include "debug.h"
#include "fatal_error.h"
#include "mbus.h"
#include "mbus_ui.h"
#include "mbus_parser.h"
#include "tcl.h"
#include "tk.h"
#include "tcltk.h"
#include "util.h"
#include "version.h"

char*       e_addr = NULL; /* Engine address */
char*       c_addr = NULL; /* Controller address */
char        m_addr[100];
char*       c_addr, *token, *token_e; 
pid_t       ppid;

int ui_active   = FALSE;
int should_exit = FALSE;
int got_detach  = FALSE;
int got_quit    = FALSE;
static int mbus_shutdown_error = FALSE;

static void parse_args(int argc, char *argv[])
{
	int 	i;

	if (argc != 5) {
		printf("UI usage: %s -ctrl <addr> -token <token>\n", argv[0]);
		exit(1);
	}
	for (i = 1; i < argc; i++) {
		if (strcmp(argv[i], "-ctrl") == 0) {
			c_addr = xstrdup(argv[++i]);
		} else if (strcmp(argv[i], "-token") == 0) {
			token   = xstrdup(argv[++i]);
			token_e = mbus_encode_str(token);
		} else {
			printf("Unknown argument \"%s\"\n", argv[i]);
			abort();
		}
	}
        /*
         * Want app instance to be same across all processes that
         * consitute this rat.  Parent pid appears after last colon.
         * Obviously on Un*x we could use getppid...
         */
        i = strlen(c_addr) - 1;
        while(i > 1 && c_addr[i - 1] != ':') {
                i--;
        }
        ppid = (pid_t)strtoul(&c_addr[i], NULL, 10);
}

#ifdef WIN32
extern HINSTANCE hAppInstance;
extern int       TkWinXInit(HINSTANCE);
extern void      TkWinXCleanup(HINSTANCE);
#endif

static void 
mbus_error_handler(int seqnum, int reason)
{
        debug_msg("mbus message failed (%d:%d)\n", seqnum, reason);
        if (should_exit == FALSE) {
                abort();
        }
	mbus_shutdown_error = TRUE;
        UNUSED(seqnum);
        UNUSED(reason);
        /* Ignore error we're closing down anyway */
}

int main(int argc, char *argv[])
{
	struct mbus	*m;
	struct timeval	 timeout;

#ifdef WIN32
        HANDLE     hWakeUpEvent;
        TkWinXInit(hAppInstance);
        hWakeUpEvent = CreateEvent(NULL, FALSE, FALSE, "Local\\RAT UI WakeUp Event");
#endif

        debug_set_core_dir(argv[0]);

	debug_msg("rat-ui started argc=%d\n", argc);
	parse_args(argc, argv);
	tcl_init1(argc, argv);

	sprintf(m_addr, "(media:audio module:ui app:rat id:%lu)", (unsigned long) ppid);
	m = mbus_init(mbus_ui_rx, mbus_error_handler, m_addr);
        if (m == NULL) {
                fatal_error("RAT v" RAT_VERSION, "Could not initialize Mbus: Is multicast enabled?");
                return FALSE;
        }

	/* Next, we signal to the controller that we are ready to go. It should be sending  */
	/* us an mbus.waiting(foo) where "foo" is the same as the "-token" argument we were */
	/* passed on startup. We respond with mbus.go(foo) sent reliably to the controller. */
	debug_msg("Waiting for mbus.waiting(%s) from controller...\n", token);
	mbus_rendezvous_go(m, token, (void *) m);
	debug_msg("...got it\n");

	/* At this point we know the mbus address of our controller, and have conducted   */
	/* a successful rendezvous with it. It will now send us configuration commands.   */
	/* We do mbus.waiting(foo) where "foo" is the original token. The controller will */
	/* eventually respond with mbus.go(foo) when it has finished sending us commands. */
	debug_msg("Waiting for mbus.go(%s) from controller...\n", token);
	mbus_rendezvous_waiting(m, c_addr, token, (void *) m);
	debug_msg("...got it\n");

	/* Okay, we wait for the media engine to solicit for a user interface... */
	debug_msg("Waiting for mbus.waiting(rat-ui-requested) from media engine...\n");
	do {
		mbus_heartbeat(m, 1);
		mbus_retransmit(m);
		mbus_send(m);
		timeout.tv_sec  = 0;
		timeout.tv_usec = 10000;
		mbus_recv(m, (void *) m, &timeout);
		while (Tcl_DoOneEvent(TCL_DONT_WAIT | TCL_ALL_EVENTS)) {
			/* Process Tcl/Tk events... */
		}
	} while (e_addr == NULL);
	mbus_qmsgf(m, e_addr, TRUE, "mbus.go", "\"rat-ui-requested\"");
	debug_msg("...got it\n");

	tcl_init2(m, e_addr);
	ui_active = TRUE;
	while (!should_exit) {
		timeout.tv_sec  = 0;
		timeout.tv_usec = 20000;
		mbus_recv(m, (void *)m, &timeout);
		mbus_heartbeat(m, 1);
		mbus_retransmit(m);
		mbus_send(m);
		while (Tcl_DoOneEvent(TCL_DONT_WAIT | TCL_ALL_EVENTS)) {
			/* Process Tcl/Tk events... */
		}
		if (Tk_GetNumMainWindows() == 0) {
			should_exit = TRUE;
		}
		/* Throttle CPU usage */
#ifdef WIN32
                /* Just timeout waiting for event that never happens */
                WaitForSingleObject(hWakeUpEvent, 30);
#else
		timeout.tv_sec  = 0;
		timeout.tv_usec = 30000;
                select(0, NULL, NULL, NULL, &timeout);
#endif
                /* If controller has died call it a day.  Need this for Win32
                 * as controller can die via terminate call and not be given
                 * chance to send quit message
                 */
                if (mbus_addr_valid(m, c_addr) == FALSE) {
                        should_exit = TRUE;
                        debug_msg("Controller address is no longer valid.  Assuming it exited.\n");
                } 
	}

        if (mbus_addr_valid(m, e_addr)) {
                /* Close things down nicely... Tell the media engine we wish to detach... */
                mbus_qmsgf(m, e_addr, TRUE, "tool.rat.ui.detach.request", "");
                mbus_send(m);
		debug_msg("Waiting for tool.rat.ui.detach() from media engine...\n");
                while (!got_detach  && mbus_addr_valid(m, e_addr) && mbus_shutdown_error == FALSE) {
                        mbus_heartbeat(m, 1);
                        mbus_retransmit(m);
                        mbus_send(m);
                        timeout.tv_sec  = 0;
                        timeout.tv_usec = 10000;
                        mbus_recv(m, (void *) m, &timeout);
                }
                debug_msg("...got it\n");
        } else {
                debug_msg("Engine looks like it has exited already.\n");
        }

        if (mbus_addr_valid(m, c_addr)) {
                /* Tell the controller it's time to quit... */
                mbus_qmsgf(m, c_addr, TRUE, "mbus.quit", "");
                do {
                        mbus_send(m);
                        mbus_retransmit(m);
                        timeout.tv_sec  = 0;
                        timeout.tv_usec = 20000;
                        mbus_recv(m, NULL, &timeout);
                } while (!mbus_sent_all(m) && mbus_shutdown_error == FALSE);
                
                debug_msg("Waiting for mbus.quit() from controller...\n");
                while (got_quit == FALSE && mbus_shutdown_error == FALSE) {
                        mbus_heartbeat(m, 1);
                        mbus_retransmit(m);
                        mbus_send(m);
                        timeout.tv_sec  = 0;
                        timeout.tv_usec = 10000;
                        mbus_recv(m, (void *) m, &timeout);
                }
                debug_msg("...got it\n");
        } else {
                debug_msg("Controller appears to have exited already.\n");
        }

	mbus_qmsgf(m, "()", FALSE, "mbus.bye", "");
	do {
		mbus_send(m);
		mbus_retransmit(m);
		timeout.tv_sec  = 0;
		timeout.tv_usec = 20000;
		mbus_recv(m, NULL, &timeout);
	} while (mbus_sent_all(m) == FALSE && mbus_shutdown_error == FALSE);
	mbus_exit(m);

        xfree(c_addr);
        xfree(e_addr);
        xfree(token);
        xfree(token_e);
        tcl_exit();
#ifdef WIN32
        TkWinXCleanup(hAppInstance);
#endif
        xmemdmp();
	debug_msg("User interface exit\n");
        return 0;
}


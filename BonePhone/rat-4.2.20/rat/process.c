/*
 * FILE:    process.c
 * PROGRAM: RAT - controller
 * AUTHOR:  Colin Perkins / Orion Hodson
 *
 * Copyright (c) 1999-2001 University College London
 * All rights reserved.
 */

#ifndef HIDE_SOURCE_STRINGS
static const char cvsid[] = "$Id";
#endif /* HIDE_SOURCE_STRINGS */

#include "config_unix.h"
#include "config_win32.h"
#include "debug.h"
#include "mbus.h"
#include "process.h"

void fork_process(char *proc_name, char *ctrl_addr, pid_t *pid, int num_tokens, char *token[2])
{
#ifdef WIN32
        char			 args[1024];
        LPSTARTUPINFO		 startup_info;
        LPPROCESS_INFORMATION	 proc_info;
        
        startup_info = (LPSTARTUPINFO) xmalloc(sizeof(STARTUPINFO));
        startup_info->cb              = sizeof(STARTUPINFO);
        startup_info->lpReserved      = 0;
        startup_info->lpDesktop       = 0;
        startup_info->lpTitle         = 0;
        startup_info->dwX             = 0;
        startup_info->dwY             = 0;
        startup_info->dwXSize         = 0;
        startup_info->dwYSize         = 0;
        startup_info->dwXCountChars   = 0;
        startup_info->dwYCountChars   = 0;
        startup_info->dwFillAttribute = 0;
        startup_info->dwFlags         = 0;
        startup_info->wShowWindow     = 0;
        startup_info->cbReserved2     = 0;
        startup_info->lpReserved2     = 0;
        startup_info->hStdInput       = 0;
        startup_info->hStdOutput      = 0;
        startup_info->hStdError       = 0;
        
        proc_info = (LPPROCESS_INFORMATION) xmalloc(sizeof(PROCESS_INFORMATION));
        
	if (num_tokens == 1) {
		sprintf(args, "%s -ctrl \"%s\" -token %s", proc_name, ctrl_addr, token[0]);
	} else {
		sprintf(args, "%s -T -ctrl \"%s\" -token %s -token %s", proc_name, ctrl_addr, token[0], token[1]);
	}
        
        if (!CreateProcess(NULL, args, NULL, NULL, TRUE, 0, NULL, NULL, startup_info, proc_info)) {
                perror("Couldn't create process");
                abort();
        }
        *pid = (pid_t) proc_info->hProcess;	/* Sigh, hope a HANDLE fits into 32 bits... */
        debug_msg("Forked %s\n", proc_name);
#else /* ...we're on unix */
	char *path, *path_env;

#ifdef DEBUG_FORK
	if (num_tokens == 1) {
        	debug_msg("%s -ctrl '%s' -token %s\n", proc_name, ctrl_addr, token[0]);
	} else {
        	debug_msg("%s -T -ctrl '%s' -token %s -token %s\n", proc_name, ctrl_addr, token[0], token[1]);
	}
        UNUSED(pid);
#else
	if ((getuid() != 0) && (geteuid() != 0)) {
		/* Ensure that the current directory is in the PATH. This is a security */
		/* problem, but reduces the number of support calls we get...           */
		path = getenv("PATH");
		if (path == NULL) {
			path_env = (char *) xmalloc(8);
			sprintf(path_env, "PATH=.");
		} else {
			path_env = (char *) xmalloc(strlen(path) + 8);
			sprintf(path_env, "PATH=%s:.", path);
		}
		debug_msg("%s\n", path_env);
		putenv(path_env);
		/* NOTE: we MUST NOT free the memory allocated to path_env. In some    */
		/* cases the string passed to putenv() becomes part of the environment */
		/* and hence freeing the memory removes PATH from the environment.     */
	} else {
		debug_msg("Running as root? PATH unmodified\n");
	}

	/* Fork off the sub-process... */
        *pid = fork();
        if (*pid == -1) {
                perror("Cannot fork");
                abort();
        } else if (*pid == 0) {
		if (num_tokens == 1) {
			execlp(proc_name, proc_name, "-ctrl", ctrl_addr, "-token", token[0], NULL);
		} else {
			execlp(proc_name, proc_name, "-T", "-ctrl", ctrl_addr, "-token", token[0], "-token", token[1], NULL);
		}
                perror("Cannot execute subprocess");
                /* Note: this MUST NOT be exit() or abort(), since they affect the standard */
                /* IO channels in the parent process (fork duplicates file descriptors, but */
                /* they still point to the same underlying file).                           */
                _exit(1);	
        }
#endif
#endif
}

void kill_process(pid_t proc)
{
        if (proc == 0) {
                debug_msg("Process already dead\n", proc);
                return;
        }
        debug_msg("Killing process %d\n", proc);
#ifdef WIN32
        /* This doesn't close down DLLs or free resources, so we have to  */
        /* hope it doesn't get called. With any luck everything is closed */
        /* down by sending it an mbus.exit() message, anyway...           */
        TerminateProcess((HANDLE) proc, 0);
#else
        kill(proc, SIGINT);
#endif
}



/*
 * FILE:    fatal_error.c
 *
 * Copyright (c) 2000-2001 University College London
 * All rights reserved.
 *
 * $Id: fatal_error.c,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

#include <config_unix.h>
#include <config_win32.h>

#include "fatal_error.h"

void
fatal_error(const char *appname, const char *msg)
{
#ifdef WIN32
        MessageBox(NULL, msg, appname, MB_ICONERROR | MB_OK);
#else
        fprintf(stderr, "%s: %s\n", appname, msg);
#endif
}

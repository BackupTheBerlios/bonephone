/*
 * FILE:    mbus_control.h
 * PROGRAM: RAT - controller
 * AUTHOR:  Colin Perkins 
 *
 * Copyright (c) 1999-2001 University College London
 * All rights reserved.
 *
 * $Id: mbus_control.h,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

#ifndef _MBUS_CONTROL_H
#define _MBUS_CONTROL_H

void  mbus_control_wait_init(char *token);
char *mbus_control_wait_done(void);
void  mbus_control_rx(char *srce, char *cmnd, char *args, void *data);

#endif

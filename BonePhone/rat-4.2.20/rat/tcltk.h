/*
 * FILE:    tcltk.h
 * PROGRAM: RAT
 * AUTHOR:  Isidor Kouvelas + Colin Perkins
 *
 * Copyright (c) 1995-2001 University College London
 * All rights reserved.
 *
 * $Id: tcltk.h,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

#ifndef _TCLTK_H
#define _TCLTK_H

void    tcl_send(char *command);
int 	tcl_init1(int argc, char **argv);
int	tcl_init2(struct mbus *mbus_ui, char *mbus_engine_addr);
void    tcl_exit(void);

#endif

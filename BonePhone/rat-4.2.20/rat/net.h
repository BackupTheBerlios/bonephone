/*
 * FILE:    net.h
 * PROGRAM: RAT
 * AUTHOR:  Colin Perkins / Orion Hodson
 *
 * Copyright (c) 1995-2001 University College London
 * All rights reserved.
 *
 * $Id: net.h,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

#ifndef _RAT_NET_H_
#define _RAT_NET_H_

void    network_process_mbus(struct s_session *sp);
uint32_t ntp_time32(void);

#endif /* _RAT_NET_H_ */



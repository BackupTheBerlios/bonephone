/*
 * FILE:    codec_l16.h
 * AUTHORS: Orion Hodson
 *
 * Copyright (c) 1998-2001 University College London
 * All rights reserved.
 *
 * $Id: codec_l8.h,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

#ifndef _CODEC_L8_H_
#define _CODEC_L8_H_

uint16_t		l8_get_formats_count	(void);
const codec_format_t*	l8_get_format		(uint16_t idx);
int			l8_encode		(uint16_t idx, u_char *state, sample     *in, coded_unit *out);
int			l8_decode		(uint16_t idx, u_char *state, coded_unit *in, sample     *out);
int			l8_peek_frame_size	(uint16_t idx, u_char *data,  int data_len);

#endif /* _CODEC_L8_H_ */

/*
 * FILE:    convert_sinc.h
 * PROGRAM: RAT
 * AUTHOR:  O.Hodson <O.Hodson@cs.ucl.ac.uk>
 *
 * Copyright (c) 1998-2001 University College London
 * All rights reserved.
 *
 * $Id: convert_sinc.h,v 1.1 2002/02/04 13:23:35 Psycho Exp $
 */

#ifndef __CONVERT_SINC_H__
#define __CONVERT_SINC_H__

int  sinc_startup  (void);
void sinc_shutdown (void);

int  sinc_create  (const converter_fmt_t *cfmt, u_char **state, uint32_t *state_len);
void sinc_destroy (u_char **state, uint32_t *state_len);
void sinc_convert (const converter_fmt_t *cfmt, u_char *state, 
                   sample *src_buf, int src_len, 
                   sample *dst_buf, int dst_len);

#endif /* __CONVERT_SINC_H__ */

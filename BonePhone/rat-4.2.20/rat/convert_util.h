/*
 * FILE:    convert_util.h
 * PROGRAM: RAT
 * AUTHOR:  O.Hodson <O.Hodson@cs.ucl.ac.uk>
 *
 * Copyright (c) 1998-2001 University College London
 * All rights reserved.
 *
 * $Id: convert_util.h,v 1.1 2002/02/04 13:23:35 Psycho Exp $
 */

#ifndef __CONVERT_UTIL__
#define __CONVERT_UTIL_

void converter_change_channels (sample *src, 
                                int src_len, 
                                int src_channels, 
                                sample *dst, 
                                int dst_len, 
                                int dst_channels);
int  gcd(int a, int b);
int  conversion_steps(int f1, int f2);
int  converter_format_valid(const converter_fmt_t *cfmt);

#endif  /* __CONVERT_UTIL__ */


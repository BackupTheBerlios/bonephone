/*
 * FILE:    cushion.h
 * PROGRAM: RAT
 * AUTHOR:  Isidor Kouvelas
 * MODIFICATIONS: Orion Hodson
 *
 * Copyright (c) 1995-2001 University College London
 * All rights reserved.
 *
 * $Id: cushion.h,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

#ifndef __CUSHION_H__
#define __CUSHION_H__

#define CUSHION_MODE_LECTURE    0
#define CUSHION_MODE_CONFERENCE 1

struct s_cushion_struct;

int     cushion_create             (struct s_cushion_struct **c, uint16_t sample_rate);
void    cushion_destroy            (struct s_cushion_struct **c);

void    cushion_update             (struct s_cushion_struct *c, uint32_t read_dur, int cushion_mode);

uint32_t cushion_get_size           (struct s_cushion_struct *c);

uint32_t cushion_step_up            (struct s_cushion_struct *c);
uint32_t cushion_step_down          (struct s_cushion_struct *c);
uint32_t cushion_get_step           (struct s_cushion_struct *c);

uint32_t cushion_use_estimate       (struct s_cushion_struct *c);
int32_t   cushion_diff_estimate_size (struct s_cushion_struct *c);

#endif 



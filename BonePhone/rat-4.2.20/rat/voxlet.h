/*
 * FILE:    voxlet.h
 * PROGRAM: RAT
 * AUTHORS: Orion Hodson
 *
 * Copyright (c) 2000-2001 University College London
 * All rights reserved.
 *
 * $Id: voxlet.h,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

typedef struct s_voxlet voxlet_t;

int  voxlet_create  (voxlet_t          **ppv, 
                     struct s_mixer     *mixer, 
                     struct s_pdb       *pdb, 
                     const char         *sndfile);
int  voxlet_play    (voxlet_t           *ppv, 
                     timestamp_t                start, 
                     timestamp_t                end);
void voxlet_destroy (voxlet_t          **ppv);

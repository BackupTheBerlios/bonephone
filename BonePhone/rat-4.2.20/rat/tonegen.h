/*
 * FILE:    tonegen.h
 * PROGRAM: RAT
 * AUTHORS: Orion Hodson
 *
 * Copyright (c) 2000-2001 University College London
 * All rights reserved.
 *
 * $Id: tonegen.h,v 1.1 2002/02/04 13:23:35 Psycho Exp $
 */

typedef struct s_tonegen tonegen_t;

int  tonegen_create  (tonegen_t          **ppv, 
                      struct s_mixer     *mixer, 
                      struct s_pdb       *pdb, 
                      uint16_t            tonefreq,
                      uint16_t            toneamp);
int  tonegen_play    (tonegen_t           *ppv, 
                      timestamp_t                start, 
                      timestamp_t                end);
void tonegen_destroy (tonegen_t          **ppv);



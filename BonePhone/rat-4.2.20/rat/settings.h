/*
 * FILE:    settings.h
 * PROGRAM: RAT
 * AUTHORS: Colin Perkins 
 *
 * Copyright (c) 1999-2001 University College London
 * All rights reserved.
 *
 * $Id: settings.h,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

void settings_load_early(session_t *sp);
void settings_load_late(session_t *sp);
void settings_save(session_t *sp);


/*
 * FILE:    mbus_engine.h
 * AUTHORS: Colin Perkins
 *
 * Copyright (c) 1998-2001 University College London
 * All rights reserved.
 *
 * $Id: mbus_engine.h,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

#ifndef _MBUS_ENGINE_H
#define _MBUS_ENGINE_H

void mbus_engine_rx(char *srce, char *cmnd, char *args, void *data);

#endif

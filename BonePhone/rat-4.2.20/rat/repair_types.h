/*
 * FILE:    repair_types.h
 * PROGRAM: RAT
 * AUTHOR:  Orion Hodson
 *
 * Copyright (c) 1995-2001 University College London
 * All rights reserved.
 *
 * $Id: repair_types.h,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

#ifndef __REPAIR_TYPES__
#define __REPAIR_TYPES__

typedef uint32_t repair_id_t;

typedef struct {
        const char  *name;
        repair_id_t  id;
} repair_details_t;

#endif /* __REPAIR_TYPES__ */

/*
 * FILE:     auddev_pca.h
 * PROGRAM:  RAT
 * AUTHOR:   Orion Hodson
 *
 * Copyright (c) 1998-2001 University College London
 * All rights reserved.
 *
 * $Id: auddev_pca.h,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */

#ifndef _AUDDEV_PCA_H_
#define _AUDDEV_PCA_H_

int pca_audio_init        (void);
int pca_audio_device_count(void);
char*
    pca_audio_device_name (audio_desc_t ad);
int  pca_audio_open       (audio_desc_t ad, audio_format* ifmt, audio_format* ofmt);
void pca_audio_close      (audio_desc_t ad);
void pca_audio_drain      (audio_desc_t ad);
int  pca_audio_duplex     (audio_desc_t ad);
void pca_audio_set_igain   (audio_desc_t ad, int gain);
int  pca_audio_get_igain   (audio_desc_t ad);
void pca_audio_set_ogain (audio_desc_t ad, int vol);
int  pca_audio_get_ogain (audio_desc_t ad);
void pca_audio_loopback   (audio_desc_t ad, int gain);
int  pca_audio_read       (audio_desc_t ad, u_char *buf, int buf_len);
int  pca_audio_write      (audio_desc_t ad, u_char *buf, int buf_len);
void pca_audio_non_block  (audio_desc_t ad);
void pca_audio_block      (audio_desc_t ad);

void          pca_audio_oport_set     (audio_desc_t ad, audio_port_t port);
audio_port_t  pca_audio_oport_get     (audio_desc_t ad);
const audio_port_details_t*
              pca_audio_oport_details (audio_desc_t ad, int idx);
int           pca_audio_oport_count   (audio_desc_t ad);

void          pca_audio_iport_set     (audio_desc_t ad, audio_port_t port);
audio_port_t  pca_audio_iport_get     (audio_desc_t ad);
const audio_port_details_t*
              pca_audio_iport_details (audio_desc_t ad, int idx);
int           pca_audio_iport_count   (audio_desc_t ad);

int  pca_audio_is_ready  (audio_desc_t ad);
void pca_audio_wait_for  (audio_desc_t ad, int delay_ms);
int  pca_audio_supports  (audio_desc_t ad, audio_format *fmt);
#endif /* _AUDDEV_PCA_H_ */

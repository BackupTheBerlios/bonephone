/*
 * FILE:    auddev_alsa.c
 * PROGRAM: RAT
 * AUTHOR:  Robert Olson
 *
 * $Revision: 1.1 $
 * $Date: 2002/02/04 13:23:34 $
 *
 * Copyright (c) 2000 Argonne National Laboratory
 * All rights reserved.
 *
 */

/*
 * ALSA audio device. 
 */

/*
 * Notes on mixers.
 *
 * Depending on the type of mixer on the soundcard, there are
 * different actions that need to be taken to enable and disable
 * capture and to set input levels.
 *
 * AC97 based mixers tap the signal before the per-input volume
 * controls. The input level is controlled by the "Input Gain" group.
 * This group must be unmuted and controlled by the igain_set routine.
 * Input selection is still done on a per-input basis.
 *
 * AK4531 based mixers (SB PCI128) tap the signal after the per-input
 * volume controls. Therefore the input level is controlled by the
 * group corresponding to the currently selected input.
 *
 * For both mixer types, audio loopback is enabled by turning off
 * mute on the appropriate input channel.
 *
 */

#include "config_unix.h"
#include "config_win32.h"
#include "audio_types.h"
#include "auddev_alsa.h"
#include "memory.h"
#include "debug.h"
#undef UNUSED
#include <sys/asoundlib.h>
#undef UNUSED
#define UNUSED(x) (x=x)
#include <stdarg.h>

#define PB SND_PCM_CHANNEL_PLAYBACK
#define CAP SND_PCM_CHANNEL_CAPTURE

static int doCheckStatus(int channel, int line);
#define checkStatus(x) doCheckStatus(x, __LINE__)

static int channelPrepare(snd_pcm_t *handle, int channel);

/*
 * Structure that keeps track of the cards we know about. Rat wants a linear
 * list of cards, so we'll give it that.
 *
 * This is filled in during the init step.
 */

typedef struct RatCardInfo_t
{
    int cardNumber;
    int pcmDevice;
    int mixerDevice;
    char name[256];
    
} RatCardInfo;

#define MAX_RAT_CARDS 128

static RatCardInfo ratCards[MAX_RAT_CARDS];
static int nRatCards = 0;

/*
 * Each mixer type is defined by a mapping from RAT port
 * id to the set of gids for the different functions on that
 * port (input source selection, set of ports to deselect when
 * this port selected, group to adjust volume with)
 */

struct MixerInPortInfo
{
    snd_mixer_gid_t captureGID;
    snd_mixer_gid_t *noncaptureGID; /* GIDs to deselect when we select this input */
    int noncaptureGIDCount;
    snd_mixer_gid_t loopbackGID; /* GID to unmute to turn on loopback */
    
    snd_mixer_gid_t inputGainGID;
};

struct MixerOutPortInfo
{
    snd_mixer_gid_t enableGID;
    snd_mixer_gid_t *disableGID; /* GIDs to deselect when we select this input */
    int disableGIDCount;
    snd_mixer_gid_t outputGainGID;
};

enum MixerInPortIndex
{
    ALSA_MIC,
    ALSA_LINE_IN,
    ALSA_CD,
};

enum MixerOutPortIndex
{
    ALSA_SPEAKER,
    ALSA_HEADPHONE,
    ALSA_LINE_OUT
};

/*
 * Mappings for ports. This table maps from ALSA port index to Rat port identifiers,
 * and is what rat wants back from the get_details funcs.
 */
static audio_port_details_t in_ports[] = {
    { ALSA_MIC, AUDIO_PORT_MICROPHONE },
    { ALSA_LINE_IN, AUDIO_PORT_LINE_IN },
    { ALSA_CD, AUDIO_PORT_CD, }
};
#define ALSA_NUM_INPORTS (sizeof(in_ports) / sizeof(in_ports[0]))

static audio_port_details_t out_ports[] = {
    { ALSA_SPEAKER, AUDIO_PORT_SPEAKER },
};
#define ALSA_NUM_OUTPORTS (sizeof(out_ports) / sizeof(out_ports[0]))

/*
 * Current open audio device
 */

static RatCardInfo *CurCard = 0;
static int CurCardIndex = -1;
static snd_pcm_t *CurHandle = 0;
static snd_mixer_t *CurMixer = 0;
static audio_port_t CurInPort;
static audio_port_t CurOutPort;
static int CurLoopbackGain = 0;
static int CurCaptureMode;
static int CurPlaybackMode;
static audio_format CurCaptureFormat;
static audio_format CurPlaybackFormat;

struct MixerOutPortInfo OutPorts[ALSA_NUM_OUTPORTS];
struct MixerInPortInfo InPorts[ALSA_NUM_INPORTS];

static snd_mixer_gid_t *makeGIDList(int n, ...)
{
    va_list va;
    snd_mixer_gid_t *list, *elt;
    int i;
    
    list = (snd_mixer_gid_t *) malloc(sizeof(snd_mixer_gid_t) * n);

    va_start(va, n);
    for (i = 0; i < n; i++)
    {
	elt = va_arg(va, snd_mixer_gid_t *);
	list[i] = *elt;
    }
    va_end(va);
    return list;
}

static void getGID(char *name, snd_mixer_gid_t *gid)
{
    memset(gid, 0, sizeof(*gid));
    strcpy(gid->name, name);
}

static void setupMixerAC97()
{
    snd_mixer_gid_t inputGain, pcm, line, mic, cd;
    struct MixerInPortInfo *in;
    struct MixerOutPortInfo *out;

    getGID(SND_MIXER_GRP_IGAIN, &inputGain);
    getGID(SND_MIXER_IN_PCM, &pcm);
    getGID(SND_MIXER_IN_LINE, &line);
    getGID(SND_MIXER_IN_MIC, &mic);
    getGID(SND_MIXER_IN_CD, &cd);

    /* Mic bindings */
    
    in = &(InPorts[ALSA_MIC]);
    in->captureGID = mic;
    in->noncaptureGID = makeGIDList(2, &line, &cd);
    in->noncaptureGIDCount = 2;
    in->loopbackGID = mic;
    in->inputGainGID = inputGain;

    /* Line */

    in = &(InPorts[ALSA_LINE_IN]);
    in->captureGID = line;
    in->noncaptureGID = makeGIDList(2, &mic, &cd);
    in->noncaptureGIDCount = 2;
    in->loopbackGID = line;
    in->inputGainGID = inputGain;

    /* CD */
	   
    in = &(InPorts[ALSA_CD]);
    in->captureGID = cd;
    in->noncaptureGID = makeGIDList(2, &line, &cd);
    in->noncaptureGIDCount = 2;
    in->loopbackGID = cd;
    in->inputGainGID = inputGain;

    /* Output */

    out = &(OutPorts[ALSA_SPEAKER]);

    out->enableGID = pcm;
    out->disableGID = 0;
    out->disableGIDCount = 0;
    out->outputGainGID = pcm;
}

static void setupMixerAK4531()
{
    snd_mixer_gid_t inputGain, pcm, line, mic, cd;
    struct MixerInPortInfo *in;
    struct MixerOutPortInfo *out;

    getGID(SND_MIXER_GRP_IGAIN, &inputGain);
    getGID(SND_MIXER_IN_PCM, &pcm);
    getGID(SND_MIXER_IN_LINE, &line);
    getGID(SND_MIXER_IN_MIC, &mic);
    getGID(SND_MIXER_IN_CD, &cd);

    /* Mic bindings */
    
    in = &(InPorts[ALSA_MIC]);
    in->captureGID = mic;
    in->noncaptureGID = makeGIDList(2, &line, &cd);
    in->noncaptureGIDCount = 2;
    in->loopbackGID = mic;
    in->inputGainGID = mic;

    /* Line */

    in = &(InPorts[ALSA_LINE_IN]);
    in->captureGID = line;
    in->noncaptureGID = makeGIDList(2, &mic, &cd);
    in->noncaptureGIDCount = 2;
    in->loopbackGID = line;
    in->inputGainGID = line;

    /* CD */
	   
    in = &(InPorts[ALSA_CD]);
    in->captureGID = cd;
    in->noncaptureGID = makeGIDList(2, &line, &cd);
    in->noncaptureGIDCount = 2;
    in->loopbackGID = cd;
    in->inputGainGID = cd;

    /* Output */

    out = &(OutPorts[ALSA_SPEAKER]);

    out->enableGID = pcm;
    out->disableGID = 0;
    out->disableGIDCount = 0;
    out->outputGainGID = pcm;
}


/*
 * Input buffer handling
 */

struct InputBuffer_t
{
    int (*readAudioFunc)(char *buf, int bufsize);
    int (*audioReadyFunc)();
    void (*audioWaitForFunc)(int delay_ms);

    char *buffer;
    char *currentBufferLocation;
    int nBytesLeft;
    int bufferSize;
};

static int streamAudioReady(void);
static void streamAudioWaitFor(int delay_ms);
static int streamReadAudio(char *buf, int bufsize);

static int directAudioReady(void);
static void directAudioWaitFor(int delay_ms);
static int directReadAudio(char *buf, int bufsize);

static int bufferedAudioReady(void);
static void bufferedAudioWaitFor(int delay_ms);
static int bufferedReadAudio(char *buf, int bufsize);

static struct InputBuffer_t InputBuffer = {
    directReadAudio, directAudioReady, directAudioWaitFor, 0, 0, 0, 0
};

/*
 * See if we have to do our own buffering. If the blocksize
 * that's returned by the device is what we asked for, 
 * (PCI128) we don't need to buffer. Otherwise (SBLive,
 * THinkpad 570) we need to buffer. Allocate a buffer
 * of the device blocksize.
 */
static void setupInputBuffer(int ratSize, int deviceSize)
{
    if (CurCaptureMode == SND_PCM_MODE_STREAM)
    {
	debug_msg("Using streaming audio reads (size=%d)\n", deviceSize);
	InputBuffer.readAudioFunc = streamReadAudio;
	InputBuffer.audioReadyFunc = streamAudioReady;
	InputBuffer.audioWaitForFunc = streamAudioWaitFor;
    }
    else if (ratSize == deviceSize)
    {
	debug_msg("Using direct audio reads (size=%d)\n", deviceSize);
	InputBuffer.readAudioFunc = directReadAudio;
	InputBuffer.audioReadyFunc = directAudioReady;
	InputBuffer.audioWaitForFunc = directAudioWaitFor;
    }
    else
    {
	debug_msg("Using buffered audio reads (size=%d)\n", deviceSize);
	InputBuffer.readAudioFunc = bufferedReadAudio;
	InputBuffer.audioReadyFunc = bufferedAudioReady;
	InputBuffer.audioWaitForFunc = bufferedAudioWaitFor;
	
	if (InputBuffer.buffer != 0)
	{
	    if (InputBuffer.bufferSize != deviceSize)
	    {
		free(InputBuffer.buffer);
		InputBuffer.buffer = 0;
	    }
	}

	if (InputBuffer.buffer == 0)
	{
	    InputBuffer.buffer = (char *) malloc(deviceSize);
	    InputBuffer.bufferSize = deviceSize;
	}
	InputBuffer.currentBufferLocation = InputBuffer.buffer;
	InputBuffer.nBytesLeft = 0;
    }
}


/* Device routines */

/* Read audio */

static int deviceReadAudio(char *buf, int bufsize)
{
    int read_bytes;
    int status;
    
    if ((read_bytes = snd_pcm_read(CurHandle, buf, bufsize)) < 0)
    {
	switch (read_bytes)
	{
	case -EAGAIN:
	    return 0;

	case -EPIPE:
	    status = checkStatus(CAP);

	    if (status == SND_PCM_STATUS_READY || status == SND_PCM_STATUS_OVERRUN)
	    {
		debug_msg("preparing capture due to non-ready status %d\n", status);
		channelPrepare(CurHandle, CAP);
		status = checkStatus(CAP);
		debug_msg("after prepare, status=%d\n", status);
	    }
	    else
	    {
		debug_msg("Why is capture in status %d?\n", status);
	    }
	    return 0;

	default:
	    status = checkStatus(CAP);
	
	    debug_msg("read %d failed status=%d: %s\n", bufsize, status, snd_strerror(read_bytes));
	    read_bytes = 0;
	    break;
	}
    }
    else
    {
	/* debug_msg("read %d of %d\n", read_bytes, bufsize); */
    }
    return read_bytes;
}

static void 
deviceAudioWaitFor(int delay_ms)
{
    fd_set rfds;
    int n;
    int cap_fd;
    struct timeval tv;

    cap_fd = snd_pcm_file_descriptor(CurHandle, SND_PCM_CHANNEL_CAPTURE);

    FD_ZERO(&rfds);
    FD_SET(cap_fd, &rfds); 

    tv.tv_sec = delay_ms / 1000;
    tv.tv_usec = 1000 * (delay_ms % 1000);
    
    n = select(cap_fd + 1, &rfds, 0, 0, &tv);
}

static int deviceAudioReady()
{
    fd_set rfds;
    int n;
    int cap_fd;
    struct timeval tv;

    cap_fd = snd_pcm_file_descriptor(CurHandle, SND_PCM_CHANNEL_CAPTURE);

    FD_ZERO(&rfds);
    FD_SET(cap_fd, &rfds);

    tv.tv_sec = 0;
    tv.tv_usec = 10;
    
    n = select(cap_fd + 1, &rfds, 0, 0, &tv);
    if (n > 0)
    {
	/*	debug_msg("Audio ready\n");*/
    }
    else if (n < 0)
    {
	debug_msg("select error: %s\n", strerror(errno));
    }
    else
    {
//	 debug_msg("no audio ready on %d\n", cap_fd); 
    }

    return n > 0;
}

/* Generic routines */

/*
 * Stream-mode reads
 */

static int streamAudioReady(void)
{
    snd_pcm_channel_status_t status;
    int rc;

    memset(&status, 0, sizeof(status));
    status.channel = SND_PCM_CHANNEL_CAPTURE;

    if ((rc = snd_pcm_channel_status(CurHandle, &status)) != 0)
    {
	debug_msg("streamAudioReady: status check failed: %s\n",
		  snd_strerror(rc));
	return 0;
    }

//    debug_msg("stream ready check status=%d count=%d bytes_per_block=%d\n",
//	      status.status, status.count, CurCaptureFormat.bytes_per_block);
    return status.count >= CurCaptureFormat.bytes_per_block;
}

static void streamAudioWaitFor(int delay_ms)
{
    if (!streamAudioReady())
    {
	usleep(delay_ms * 1000 / 2);
    }
}

static int streamReadAudio(char *buf, int bufsize)
{
    int n;
    n = deviceReadAudio(buf, bufsize);
//    debug_msg("streamReadAudio: read %d returns %d\n", bufsize, n);
    return n;
}



/*
 * Block-mode reads, where device blocksize matches the
 * desired audio blocksize
 */

static int directAudioReady()
{
    return deviceAudioReady();
}

static void directAudioWaitFor(int delay_ms)
{
    return deviceAudioWaitFor(delay_ms);
}

static int directReadAudio(char *buf, int bufsize)
{
    return deviceReadAudio(buf,  bufsize);
}

/*
 * Block-mode reads, where device size doesn't
 * match the desired audio blocksize.
 */

static int bufferedAudioReady()
{
    if (InputBuffer.nBytesLeft > 0)
	return 1;
    else
	return deviceAudioReady();
}

static void bufferedAudioWaitFor(int delay_ms)
{
    if (InputBuffer.nBytesLeft == 0)
	return deviceAudioWaitFor(delay_ms);
}

static int bufferedReadAudio(char *buf, int bufsize)
{
    int nRead;
    if (InputBuffer.nBytesLeft == 0)
    {
	int nDev;
	nDev = deviceReadAudio(InputBuffer.buffer, InputBuffer.bufferSize);
	InputBuffer.currentBufferLocation = InputBuffer.buffer;
	InputBuffer.nBytesLeft = nDev;
	
    }

    if (InputBuffer.nBytesLeft > 0)
    {
	nRead = (bufsize > InputBuffer.nBytesLeft) ? InputBuffer.nBytesLeft : bufsize;
	memcpy(buf, InputBuffer.currentBufferLocation, nRead);
	InputBuffer.currentBufferLocation += nRead;
	InputBuffer.nBytesLeft -= nRead;
    }
    else
	nRead = 0;
    
    return nRead;
}

/*
 * Utility funcs
 */

static char *encodingToString[] = {
    "pcmu",
    "s8",
    "u8",
    "s16"
};

static int doCheckStatus(int channel, int line)
{
    int rc;
  
    snd_pcm_channel_status_t status;

    UNUSED(line);

    memset(&status, 0, sizeof(status));
    status.channel = channel;
    if ((rc = snd_pcm_channel_status(CurHandle, &status)) != 0)
	debug_msg("channel status failed: %s\n", snd_strerror(rc));
    else
	debug_msg("status: line=%d channel=%s mode=%d status=%d scount=%d frag=%d count=%d free=%d under=%d over=%d\n",
		  line,
		  channel == PB ? "PB" : "CAP",
		  status.mode, status.status, status.scount,
		  status.free, status.underrun, status.overrun);
    return status.status;
}

static int mapFormat(int encoding)
{
    int format = -1;
    switch (encoding)
    {
    case DEV_PCMU:
	format = SND_PCM_SFMT_MU_LAW;
	break;
    case DEV_S8:
	format = SND_PCM_SFMT_S8;
	break;
    case DEV_U8:
	format = SND_PCM_SFMT_U8;
	break;
    case DEV_S16:
	format = SND_PCM_SFMT_S16;
	break;
    }
    return format;
}

static void dump_audio_format(audio_format *f)
{
    if (f == 0)
	debug_msg("    <null>\n");
    else
	debug_msg("    encoding=%s sample_rate=%d bits_per_sample=%d channels=%d bytes_per_block=%d\n",
		  encodingToString[f->encoding], f->sample_rate, f->bits_per_sample,
		  f->channels, f->bytes_per_block);
}

static void dump_hw_info(struct snd_ctl_hw_info *info)
{
    debug_msg("Hw info: type=%d hwdepdevs=%d pcmdevs=%d mixerdevs=%d\n",
	      info->type, info->hwdepdevs, info->pcmdevs, info->mixerdevs);
    debug_msg("         mididevs=%d timerdevs=%d id=%s abbr=%s name=%s longname=%s\n",
	      info->mididevs, info->timerdevs, info->id, info->abbreviation,
	      info->name, info->longname);
}


static void dump_pcm_info(snd_pcm_info_t *info)
{
    char typestr[128];

    typestr[0] = 0;
    if (info->type & SND_PCM_INFO_PLAYBACK) {
	strcat(typestr, "SND_PCM_INFO_PLAYBACK ");
    }
    if (info->type & SND_PCM_INFO_CAPTURE) {
	strcat(typestr, "SND_PCM_INFO_CAPTURE ");
    }
    if (info->type & SND_PCM_INFO_DUPLEX) {
	strcat(typestr, "SND_PCM_INFO_DUPLEX ");
    }
    if (info->type & SND_PCM_INFO_DUPLEX_RATE) {
	strcat(typestr, "SND_PCM_INFO_DUPLEX_RATE ");
    }
    if (info->type & SND_PCM_INFO_DUPLEX_MONO) {
	strcat(typestr, "SND_PCM_INFO_DUPLEX_MONO ");
    }
    debug_msg("Card type=%d flags=%s id=%s name=%s\n",
	      info->type, typestr, info->id, info->name);
}

#if 0
static void dump_channel_info(snd_pcm_channel_info_t *c)
{
    char formatstr[1024];
    char chanstr[1024];
    formatstr[0] = 0;
    chanstr[0] = 0;

    if (c->flags & SND_PCM_CHNINFO_MMAP) {
	strcat(chanstr, "SND_PCM_CHNINFO_MMAP ");
    }
    if (c->flags & SND_PCM_CHNINFO_STREAM) {
	strcat(chanstr, "SND_PCM_CHNINFO_STREAM ");
    }
    if (c->flags & SND_PCM_CHNINFO_BLOCK) {
	strcat(chanstr, "SND_PCM_CHNINFO_BLOCK ");
    }
    if (c->flags & SND_PCM_CHNINFO_BATCH) {
	strcat(chanstr, "SND_PCM_CHNINFO_BATCH ");
    }
    if (c->flags & SND_PCM_CHNINFO_INTERLEAVE) {
	strcat(chanstr, "SND_PCM_CHNINFO_INTERLEAVE ");
    }
    if (c->flags & SND_PCM_CHNINFO_NONINTERLEAVE) {
	strcat(chanstr, "SND_PCM_CHNINFO_NONINTERLEAVE ");
    }
    if (c->flags & SND_PCM_CHNINFO_BLOCK_TRANSFER) {
	strcat(chanstr, "SND_PCM_CHNINFO_BLOCK_TRANSFER ");
    }
    if (c->flags & SND_PCM_CHNINFO_OVERRANGE) {
	strcat(chanstr, "SND_PCM_CHNINFO_OVERRANGE ");
    }
    if (c->flags & SND_PCM_CHNINFO_MMAP_VALID) {
	strcat(chanstr, "SND_PCM_CHNINFO_MMAP_VALID ");
    }
    if (c->flags & SND_PCM_CHNINFO_PAUSE) {
	strcat(chanstr, "SND_PCM_CHNINFO_PAUSE ");
    }
    if (c->flags & SND_PCM_CHNINFO_GLOBAL_PARAMS) {
	strcat(chanstr, "SND_PCM_CHNINFO_GLOBAL_PARAMS ");
    }


    if (c->formats & SND_PCM_FMT_S8) {
	strcat(formatstr, "SND_PCM_FMT_S8 ");
    }
    if (c->formats & SND_PCM_FMT_U8) {
	strcat(formatstr, "SND_PCM_FMT_U8 ");
    }
    if (c->formats & SND_PCM_FMT_S16_LE) {
	strcat(formatstr, "SND_PCM_FMT_S16_LE ");
    }
    if (c->formats & SND_PCM_FMT_S16_BE) {
	strcat(formatstr, "SND_PCM_FMT_S16_BE ");
    }
    if (c->formats & SND_PCM_FMT_U16_LE) {
	strcat(formatstr, "SND_PCM_FMT_U16_LE ");
    }
    if (c->formats & SND_PCM_FMT_U16_BE) {
	strcat(formatstr, "SND_PCM_FMT_U16_BE ");
    }
    if (c->formats & SND_PCM_FMT_S24_LE) {
	strcat(formatstr, "SND_PCM_FMT_S24_LE ");
    }
    if (c->formats & SND_PCM_FMT_S24_BE) {
	strcat(formatstr, "SND_PCM_FMT_S24_BE ");
    }
    if (c->formats & SND_PCM_FMT_U24_LE) {
	strcat(formatstr, "SND_PCM_FMT_U24_LE ");
    }
    if (c->formats & SND_PCM_FMT_U24_BE) {
	strcat(formatstr, "SND_PCM_FMT_U24_BE ");
    }
    if (c->formats & SND_PCM_FMT_S32_LE) {
	strcat(formatstr, "SND_PCM_FMT_S32_LE ");
    }
    if (c->formats & SND_PCM_FMT_S32_BE) {
	strcat(formatstr, "SND_PCM_FMT_S32_BE ");
    }
    if (c->formats & SND_PCM_FMT_U32_LE) {
	strcat(formatstr, "SND_PCM_FMT_U32_LE ");
    }
    if (c->formats & SND_PCM_FMT_U32_BE) {
	strcat(formatstr, "SND_PCM_FMT_U32_BE ");
    }
    if (c->formats & SND_PCM_FMT_FLOAT_LE) {
	strcat(formatstr, "SND_PCM_FMT_FLOAT_LE ");
    }
    if (c->formats & SND_PCM_FMT_FLOAT_BE) {
	strcat(formatstr, "SND_PCM_FMT_FLOAT_BE ");
    }
    if (c->formats & SND_PCM_FMT_FLOAT64_LE) {
	strcat(formatstr, "SND_PCM_FMT_FLOAT64_LE ");
    }
    if (c->formats & SND_PCM_FMT_FLOAT64_BE) {
	strcat(formatstr, "SND_PCM_FMT_FLOAT64_BE ");
    }
    if (c->formats & SND_PCM_FMT_IEC958_SUBFRAME_LE) {
	strcat(formatstr, "SND_PCM_FMT_IEC958_SUBFRAME_LE ");
    }
    if (c->formats & SND_PCM_FMT_IEC958_SUBFRAME_BE) {
	strcat(formatstr, "SND_PCM_FMT_IEC958_SUBFRAME_BE ");
    }
    if (c->formats & SND_PCM_FMT_MU_LAW) {
	strcat(formatstr, "SND_PCM_FMT_MU_LAW ");
    }
    if (c->formats & SND_PCM_FMT_A_LAW) {
	strcat(formatstr, "SND_PCM_FMT_A_LAW ");
    }
    if (c->formats & SND_PCM_FMT_IMA_ADPCM) {
	strcat(formatstr, "SND_PCM_FMT_IMA_ADPCM ");
    }
    if (c->formats & SND_PCM_FMT_MPEG) {
	strcat(formatstr, "SND_PCM_FMT_MPEG ");
    }
    if (c->formats & SND_PCM_FMT_GSM) {
	strcat(formatstr, "SND_PCM_FMT_GSM ");
    }
    if (c->formats & SND_PCM_FMT_SPECIAL) {
	strcat(formatstr, "SND_PCM_FMT_SPECIAL ");
    }

    debug_msg("subdev=%d subname=%s channel=%d mode=%d flags=%x %s formats=%x %s rates=%x\n",
	      c->subdevice, c->subname, c->channel, c->mode, c->flags, chanstr, c->formats, formatstr, c->rates);
    debug_msg("min_rate=%d max_rate=%d min_voices=%d max_voices=%d buffer_size=%d mixer_device=%d\n",
	      c->min_rate, c->max_rate, c->min_voices, c->max_voices, c->buffer_size, c->mixer_device);
}
#endif

static void dump_mixer_group(snd_mixer_group_t *g)
{
    debug_msg("gid=%d elements_size=%d elements=%d elements_over=%d\n",
	      g->gid, g->elements_size, g->elements, g->elements_over);
    debug_msg("caps=%x channels=%d mute=%d capture=%d capture_group=%d min=%d max=%d\n",
	      g->caps, g->channels, g->mute, g->capture, g->capture_group, g->min, g->max);
    debug_msg("frontleft=%d frontright=%d\n",
	      g->volume.names.front_left, 
	      g->volume.names.front_right);

/*
    debug_msg("elements:\n");
    for (i = 0; i < g->elements; i++)
    {
	p = &(g->pelements[i]);
	debug_msg("element %d: name=%s index=%d type=%d\n",
		  i,
		  p->name, p->index, p->type);
    }
*/
}

static int channelSetParams(snd_pcm_t *handle,
			    int channel,
			    int mode,
			    audio_format *format)
{
    snd_pcm_channel_params_t p;
    int rc;

    memset(&p, 0, sizeof(p));
    p.channel = channel;
    p.mode = mode;
    p.format.interleave = 1;
    p.format.format = mapFormat(format->encoding);
    p.format.rate = format->sample_rate;
    p.format.voices = format->channels;

    if (channel == SND_PCM_CHANNEL_PLAYBACK)
    {
	p.start_mode = SND_PCM_START_DATA;
	p.stop_mode = SND_PCM_STOP_STOP;
    }
    else
    {
	p.start_mode = SND_PCM_START_DATA;
	p.stop_mode = SND_PCM_STOP_STOP;
    }

    if (mode == SND_PCM_MODE_BLOCK)
    {
	p.buf.block.frag_size = format->bytes_per_block;
	p.buf.block.frags_min = 1;
	p.buf.block.frags_max = -1;
    }
    else
    {
	p.buf.stream.queue_size = 1024 * 512;
	//p.buf.stream.queue_size = format->bytes_per_block * 10;
	p.buf.stream.fill = SND_PCM_FILL_NONE;
	p.buf.stream.max_fill = 1024;
    }

    if ((rc = snd_pcm_channel_params(handle, &p)) != 0)
    {
	debug_msg("set params for channel %d mode %d failed: %s\n",
		  channel, mode, snd_strerror(rc));
	return rc;
    }

    return 0;
}

static int channelPrepare(snd_pcm_t *handle, int channel)
{
    int rc;
    
    if ((rc = snd_pcm_channel_prepare(handle, channel)) != 0)
    {
	debug_msg("snd_pcm_channel_prepare(%s) failed: %s\n",
		  channel == SND_PCM_CHANNEL_PLAYBACK ? "playback" : "capture",
		  snd_strerror(rc));
	return rc;
    }
    return 0;
}

static int channelSetup(snd_pcm_t *handle, int channel,
			int mode,
			int *bufferSize)
{
    int rc;
    struct snd_pcm_channel_setup setup;

    memset(&setup, 0, sizeof(setup));
    setup.mode = mode;
    setup.channel = channel;

    if ((rc = snd_pcm_channel_setup(handle, &setup)) != 0)
    {
	debug_msg("setup failed: %s\n", snd_strerror(rc));
	*bufferSize = -1;
	return rc;
    }

    if (mode == SND_PCM_MODE_BLOCK)
    {
	*bufferSize = setup.buf.block.frag_size;
    }
    else
    {
	*bufferSize = setup.buf.stream.queue_size;
    }
    return 0;
}

static int channelGo(snd_pcm_t *handle, int channel)
{
    int rc;
    
    if ((rc = snd_pcm_channel_go(handle, channel)) < 0)
    {
	debug_msg("channel_go on channel %d failed: %s\n",
		  channel, snd_strerror(rc));
    }
    return rc;
}

static void setupMixer()
{
    snd_mixer_info_t minfo;
    unsigned int i;

    debug_msg("Mixer open: card=%d device=%d\n", CurCard->cardNumber, CurCard->mixerDevice);
	      
    if (snd_mixer_open(&CurMixer, CurCard->cardNumber, CurCard->mixerDevice) != 0)
    {
	debug_msg("Mixer open failed\n");
	return;
    }
    debug_msg("curmixer=%x\n", CurMixer);

    if (snd_mixer_info(CurMixer, &minfo) != 0)
	debug_msg("mixer info failed");
    else
    {
	debug_msg("Mixer info: type=%d attrib=%x id=%s name=%s elements=%d groups=%d\n",
		  minfo.type, minfo.attrib, minfo.id, minfo.name, minfo.elements, minfo.groups);
    }

    if (strcmp(minfo.id, "AK4531") == 0)
    {
	setupMixerAK4531();
    }
    else if (strcmp(minfo.id, "AC97") == 0)
    {
	setupMixerAC97();
    }
    else
    {
	debug_msg("Unknown mixer id %s, using AC97\n", minfo.id);
	setupMixerAC97();
    }

    for (i = 0; i < ALSA_NUM_INPORTS; i++)
    {
	struct MixerInPortInfo *in = &(InPorts[i]);
	char buf[1024];

	buf[0] = 0;
	if (in->noncaptureGID != 0)
	{
	    int j;
	    for (j = 0; j < in->noncaptureGIDCount; j++)
	    {
		strcat(buf, in->noncaptureGID[j].name);
		strcat(buf, " ");
	    }
	}

	debug_msg("Inport info for %d\n", i);
	debug_msg("   capture=%s\n", in->captureGID.name);
	debug_msg("   noncapture=%s\n", buf);
	debug_msg("   loopback=%s\n", in->loopbackGID.name);
	debug_msg("   inputGain=%s\n", in->inputGainGID.name);
    }
}

/*
 * ALSA mixer callback functions
 */

static void mixer_rebuild(void *private)
{
    UNUSED(private);
    debug_msg("Mixer rebuild\n");
}

static void mixer_element(void *private, int cmd, snd_mixer_eid_t *eid)
{
    UNUSED(private);
    UNUSED(cmd);
    UNUSED(eid);
    debug_msg("mixer_element\n");
}

static void mixer_group(void *private, int cmd, snd_mixer_gid_t *gid)
{
    UNUSED(private);
    debug_msg("mixer_group %d %s\n", cmd, gid->name);
}

static snd_mixer_callbacks_t MixerCallbacks = {
    0, mixer_rebuild, mixer_element, mixer_group, { 0 }
};

static void updateMixer()
{
/*    debug_msg("start mixer read\n");*/
    snd_mixer_read(CurMixer, &MixerCallbacks);
/*    debug_msg("finish mixer read\n");*/
}

int
alsa_audio_open(audio_desc_t ad, audio_format *infmt, audio_format *outfmt)
{
    int playbackBufferSize, captureBufferSize;
    int rc;
    
    debug_msg("Audio open ad=%d\n", ad);
    debug_msg("Input format:\n");
    dump_audio_format(infmt);
    debug_msg("Output format:\n");
    dump_audio_format(outfmt);

    if (CurHandle != 0)
    {
	debug_msg("Device already open!\n");
	return FALSE;
    }

    CurCard = &(ratCards[ad]);
    CurCardIndex = ad;
    debug_msg("Opening card %s\n", CurCard->name);
    
    if (snd_pcm_open(&CurHandle, CurCard->cardNumber, CurCard->pcmDevice,
		     SND_PCM_OPEN_DUPLEX) != 0)
    {
	debug_msg("Card open failed\n");
	return FALSE;
    }

    /* Open up the mixer too */

    if (CurCard->mixerDevice != -1)
    {
	setupMixer();
    }
    else
	debug_msg("No mixer for device\n");

#define CLEANUP \
	    if (CurHandle != 0) \
	        snd_pcm_close(CurHandle);	\
	    if (CurMixer != 0) \
		snd_mixer_close(CurMixer);	\
	    CurHandle = 0;	\
	    CurMixer = 0;	\
	    return FALSE;	
#define TRY_SETUP(func, args)		\
    {	\
	int rc;	\
	rc = func args;	\
	if (rc != 0)	\
	{	\
	    _dprintf("%d:%s:%d Card setup failed with %s\n",	\
		     getpid(), __FILE__, __LINE__, snd_strerror(rc));	\
	    CLEANUP; \
	}	\
    }

    /*
     * Set up the adapter.
     * We'd prefer to use streaming mode where possible. If that doesn't
     * work, go ahead and set up for block mode.
     */

    /* Set up the playback side */

    CurPlaybackMode = SND_PCM_MODE_STREAM;

    if ((rc = channelSetParams(CurHandle, SND_PCM_CHANNEL_PLAYBACK,
			       CurPlaybackMode, outfmt)) != 0)
    {
	debug_msg("Setup for playback stream mode failed, trying block mode\n");

	CurPlaybackMode = SND_PCM_MODE_BLOCK;
	if ((rc = channelSetParams(CurHandle, SND_PCM_CHANNEL_PLAYBACK,
				   CurPlaybackMode, outfmt)) != 0)
	{
	    debug_msg("Setup for playback block mode failing, playback setup fails\n");
	    CLEANUP;
	}
    }

    TRY_SETUP(channelPrepare, (CurHandle, SND_PCM_CHANNEL_PLAYBACK));
    TRY_SETUP(channelSetup, (CurHandle, SND_PCM_CHANNEL_PLAYBACK,
			     CurPlaybackMode, &playbackBufferSize));
       

    /* And the capture side */
    
    CurCaptureMode = SND_PCM_MODE_STREAM;

    if ((rc = channelSetParams(CurHandle, SND_PCM_CHANNEL_CAPTURE,
			       CurCaptureMode, outfmt)) != 0)
    {
	debug_msg("Setup for capture stream mode failed, trying block mode\n");

	CurCaptureMode = SND_PCM_MODE_BLOCK;
	if ((rc = channelSetParams(CurHandle, SND_PCM_CHANNEL_CAPTURE,
				   CurCaptureMode, outfmt)) != 0)
	{
	    debug_msg("Setup for capture block mode failing, capture setup fails\n");
	    CLEANUP;
	}
    }

    TRY_SETUP(channelPrepare, (CurHandle, SND_PCM_CHANNEL_CAPTURE));
    TRY_SETUP(channelSetup, (CurHandle, SND_PCM_CHANNEL_CAPTURE,
			     CurCaptureMode, &captureBufferSize));

    setupInputBuffer(infmt->bytes_per_block, captureBufferSize);

    debug_msg("Card open succeeded playback buffer=%d record buffer=%d\n",
	      playbackBufferSize, captureBufferSize);

    CurInPort = ALSA_MIC;
    CurOutPort = ALSA_SPEAKER;

    /* Flush channels */

    if ((rc = snd_pcm_playback_flush(CurHandle)) != 0)
	debug_msg("initial flush failed: %s\n", snd_strerror(rc));
    if ((rc = snd_pcm_capture_flush(CurHandle)) != 0)
	debug_msg("initial flush failed: %s\n", snd_strerror(rc));

    checkStatus(CAP);
    checkStatus(PB);
    channelPrepare(CurHandle, SND_PCM_CHANNEL_CAPTURE);
    checkStatus(CAP);
    channelPrepare(CurHandle, SND_PCM_CHANNEL_PLAYBACK);
    checkStatus(PB);
      

    /* Kick off capture & playback */
    channelGo(CurHandle, SND_PCM_CHANNEL_PLAYBACK);
    checkStatus(PB);
    channelGo(CurHandle, SND_PCM_CHANNEL_CAPTURE);
    checkStatus(CAP);

    debug_msg("Audio open ad=%d\n", ad);
    debug_msg("Input format:\n");
    dump_audio_format(infmt);
    debug_msg("Output format:\n");
    dump_audio_format(outfmt);

    CurCaptureFormat = *infmt;
    CurPlaybackFormat = *outfmt;

    return TRUE;
}

/*
 * Shutdown.
 */
void
alsa_audio_close(audio_desc_t ad)
{
    int rc;

    debug_msg("Closing device %d\n", ad);
    
    if (CurCardIndex != ad)
    {
	debug_msg("Hm, CurCardIndex(%d) doesn't match request(%d)\n",
		  CurCardIndex, ad);
	return;
    }

    if ((rc = snd_pcm_close(CurHandle)) != 0)
    {
	debug_msg("Error on close: %s\n", snd_strerror(rc));
    }

    if ((rc = snd_mixer_close(CurMixer)) != 0)
    {
	debug_msg("Error on mixer close: %s\n", snd_strerror(rc));
    }

    CurMixer = 0;
    CurHandle = 0;
    CurCard = 0;
    CurCardIndex = -1;
    return;
}

/*
 * Flush input buffer.
 */
void
alsa_audio_drain(audio_desc_t ad)
{
    if (CurCardIndex != ad)
    {
	debug_msg("alsa_audio_drain: CurCardIndex(%d) doesn't match request(%d)\n",
		  CurCardIndex, ad);
	return;
    }

    debug_msg("audio_drain\n");
/*    if (snd_pcm_capture_flush(CurHandle) != 0)
	debug_msg("snd_pcm_flush_record failed\n");
*/
}

/*
 * Get the gain (0-100) on a mixer group
 */
static int get_group_gain(snd_mixer_gid_t *gid)
{
    snd_mixer_group_t group;
    int level;
    int rc;
    
    updateMixer();
    memset(&group, 0, sizeof(group));
    group.gid = *gid;

#ifdef DEBUG_MIXER
    debug_msg("gid is %s %d handle=%x\n", group.gid.name, group.gid.index, CurMixer);
#endif

    level = 0;
    if ((rc = snd_mixer_group_read(CurMixer, &group)) != 0)
    {
	debug_msg("mixer read failed: %s\n", snd_strerror(rc));
	return 0;
    }
/*    dump_mixer_group(&group);*/
    level = (int) 100.0 * ((double) (group.volume.names.front_left - group.min) / (double) (group.max - group.min));

#ifdef DEBUG_MIXER
    debug_msg("returning level=%d\n", level);
#endif


    return level;
}

/*
 * Set the gain (0-100) on a mixer group
 */
static void set_group_gain(snd_mixer_gid_t *gid, int gain)
{
    int rc;
    snd_mixer_group_t group;
    int level;

    updateMixer();
    memset(&group, 0, sizeof(group));
    group.gid = *gid;

#ifdef DEBUG_MIXER
    debug_msg("gid is %s %d handle=%x\n", group.gid.name, group.gid.index, CurMixer);
#endif

    level = 0;
    if ((rc =snd_mixer_group_read(CurMixer, &group)) != 0)
    {
	debug_msg("mixer read failed: %s\n", snd_strerror(rc));
	return;
    }
    
    level = (int) (((double) gain / 100.0) * ((double) (group.max - group.min))) + group.min;
    group.volume.names.front_left = group.volume.names.front_right = level;

    if ((rc = snd_mixer_group_write(CurMixer, &group)) != 0)
    {
	debug_msg("mixer_group_write failed: %s\n", snd_strerror(rc));
	return;
    }

#ifdef DEBUG_MIXER
    debug_msg("set level to %d\n", level);
#endif
}

/*
 * Set record gain.
 */
void
alsa_audio_set_igain(audio_desc_t ad, int gain)
{
#if 0
    debug_msg("Set igain %d %d\n", ad, gain);

    debug_msg("cur in is %d gid=%s %d\n", CurInPort,
	      alsa_in_gids[CurInPort].name,
	      alsa_in_gids[CurInPort].index);
#endif
    UNUSED(ad);

    set_group_gain(&(InPorts[CurInPort].inputGainGID), gain);
    
    return;
}

/*
 * Get record gain.
 */
int
alsa_audio_get_igain(audio_desc_t ad)
{
    UNUSED(ad);
    return get_group_gain(&(InPorts[CurInPort].inputGainGID));
}

int
alsa_audio_duplex(audio_desc_t ad)
{
    UNUSED(ad);
  /*    debug_msg("set duplex for %d\n", ad);*/
    return TRUE;
}

/*
 * Set play gain.
 */
void
alsa_audio_set_ogain(audio_desc_t ad, int vol)
{
    UNUSED(ad);
    set_group_gain(&(OutPorts[CurOutPort].outputGainGID), vol);
    return;
}

/*
 * Get play gain.
 */
int
alsa_audio_get_ogain(audio_desc_t ad)
{
    UNUSED(ad);
    return get_group_gain(&(OutPorts[CurOutPort].outputGainGID));
}

/*
 * Record audio data.
 */
int
alsa_audio_read(audio_desc_t ad, u_char *buf, int buf_bytes)
{
    UNUSED(ad);
    return InputBuffer.readAudioFunc(buf, buf_bytes);
}

/*
 * Playback audio data.
 */
int
alsa_audio_write(audio_desc_t ad, u_char *buf, int buf_bytes)
{
    int rc;
    int write_bytes;
    int retry = 0;
    snd_pcm_channel_status_t status;

    UNUSED(ad);

    memset(&status, 0, sizeof(status));
    status.channel = SND_PCM_CHANNEL_PLAYBACK;
    if ((rc = snd_pcm_channel_status(CurHandle, &status)) != 0)
	debug_msg("channel_status failed: %s\n", snd_strerror(rc));
    else
    {
	if (status.status == SND_PCM_STATUS_READY || status.status == SND_PCM_STATUS_UNDERRUN)
	{
	    debug_msg("Had to prepare before write, status=%d\n", status.status);
	    channelPrepare(CurHandle, PB);
	}
    }

    write_bytes  = snd_pcm_write(CurHandle, buf, buf_bytes);

    if (write_bytes < 0)
    {
	int status;
	debug_msg("top write failed: %d %s\n", write_bytes, snd_strerror(write_bytes));
	checkStatus(PB);
	switch (write_bytes)
	{
	case -EAGAIN:
	    return 0;

	case -EPIPE:
	    status = checkStatus(PB);
	    if (status == SND_PCM_STATUS_READY || status == SND_PCM_STATUS_UNDERRUN)
		channelPrepare(CurHandle, PB);
	    status = checkStatus(PB);
	    debug_msg("in write after prepare, status=%d\n", status);

	    retry = 1;
	    break;

	case -EIO:
	    if ((rc = snd_pcm_playback_flush(CurHandle)) != 0)
		debug_msg("playback_flush failed: %s\n", snd_strerror(rc));
	    checkStatus(PB);
	    return 0;

	case -EINVAL:

	    /*
	    debug_msg("write %d failed: %d %s\n",
		      buf_bytes, 
		      write_bytes, snd_strerror(write_bytes));
	    */
	    status = checkStatus(PB);
	    /* debug_msg("status was %d\n", status); */
	    if (status == SND_PCM_STATUS_READY)
	    {
		if ((rc = snd_pcm_channel_prepare(CurHandle, SND_PCM_CHANNEL_PLAYBACK)) != 0)
		{
		    debug_msg("playback prepare failed: %s\n", snd_strerror(rc));
		}
		debug_msg("prepared\n");

		checkStatus(PB);

		/*
		if ((rc = snd_pcm_playback_go(CurHandle) ) != 0)
		{
		    debug_msg("playback_go failed with %s\n", snd_strerror(rc));
		}
		debug_msg("go!\n");
		checkStatus(PB);
	    */
	    }
	    return 0;
	default:
	    debug_msg("write failed: %d %s\n", write_bytes, snd_strerror(write_bytes));
	    checkStatus(PB);
	    break;
	    
	}

	if (retry)
	{
	    write_bytes = snd_pcm_write(CurHandle, buf, buf_bytes);
	    if (write_bytes < 0)
	    {
		debug_msg("retry write failed: %s\n", snd_strerror(write_bytes));
		return 0;
	    }
	    else
	    {
		debug_msg("retry write succeeded %d\n", write_bytes);
		return write_bytes;
	    }
	}
	return 0;
    }
    
    return write_bytes;
}


/*
 * Set options on audio device to be non-blocking.
 */
void
alsa_audio_non_block(audio_desc_t ad)
{
    int rc;
    debug_msg("set nonblocking\n");
//    checkStatus(CAP);
    if (ad != CurCardIndex)
	debug_msg("alsa_audio_non_block: ad != current\n");
    if ((rc = snd_pcm_nonblock_mode(CurHandle, 1)) != 0)
	debug_msg("nonblock_mode failed: %s\n", snd_strerror(rc));
//    checkStatus(CAP);
}

/*
 * Set options on audio device to be blocking.
 */
void
alsa_audio_block(audio_desc_t ad)
{
    int rc;
    debug_msg("set blocking\n");
//    checkStatus(CAP);
    if (ad != CurCardIndex)
	debug_msg("alsa_audio_non_block: ad != current\n");
    if ((rc = snd_pcm_nonblock_mode(CurHandle, 0)) != 0)
	debug_msg("nonblock_mode failed: %s\n", snd_strerror(rc));
//    checkStatus(CAP);
}

/*
 * Set mute on this input port.
 */
static void enable_mute(snd_mixer_gid_t *gid, int enabled)
{
    int rc;
    snd_mixer_group_t group;

    updateMixer();

    debug_msg("mute %s: %s\n", enabled ? "enable" : "disable", gid->name);

    group.gid = *gid;

    if ((rc = snd_mixer_group_read(CurMixer, &group)) != 0)
    {
	debug_msg("mixer read failed: %s\n", snd_strerror(rc));
	return;
    }

    /* dump_mixer_group(&group); */
    if (enabled)
    {
	int val;
	val = SND_MIXER_CHN_MASK_STEREO;

	group.mute = val;
	/*	if (group.caps & SND_MIXER_GRPCAP_JOINTLY_MUTE)
		val = group.channels;*/
	/* group.capture ^= (val & group.channels); */

    }
    else
	group.mute = 0;

    /*
    debug_msg("set group.mute to %d for port %d %s\n",
    group.mute, port, alsa_ports[port]);
    */

    if ((rc = snd_mixer_group_write(CurMixer, &group)) != 0)
    {
	debug_msg("enable_capture: mixer write failed: %d %s\n", rc, snd_strerror(rc));
	return;
    }

//    checkStatus(CAP);
}


/*
 * Set capture on this input port.
 */
static void enable_capture(snd_mixer_gid_t *gid, int enabled)
{
    int rc;
    snd_mixer_group_t group;

    updateMixer();

    debug_msg("capture %s: %s\n", enabled ? "enable" : "disable", gid->name);
    
    memset(&group, 0, sizeof(group));
    group.gid = *gid;

    if ((rc = snd_mixer_group_read(CurMixer, &group)) != 0)
    {
	debug_msg("mixer read failed: %s\n", snd_strerror(rc));
	return;
    }

    /* dump_mixer_group(&group); */
    if (enabled)
    {
	int val;
	val = SND_MIXER_CHN_MASK_STEREO;

	group.capture = val;
	/*	if (group.caps & SND_MIXER_GRPCAP_JOINTLY_CAPTURE)
	    val = group.channels;
	    group.capture ^= (val & group.channels);*/

    }
    else
	group.capture = 0;

    /*
    debug_msg("set group.capture to %d for port %d %s\n",
    group.capture, port, alsa_ports[port]);
    */

    if ((rc = snd_mixer_group_write(CurMixer, &group)) != 0)
    {
	debug_msg("enable_capture: mixer write failed: %d %s\n", rc, snd_strerror(rc));
	return;
    }

//    checkStatus(CAP);
}


/*
 * Set output port.
 */
void
alsa_audio_oport_set(audio_desc_t ad, audio_port_t port)
{
    int i;
    struct MixerOutPortInfo *out;
    UNUSED(ad);

//    debug_msg("oport_set %d %d %s\n", ad, port, alsa_ports[port]);

    CurOutPort = port;

    out = &(OutPorts[CurOutPort]);

    /*
     * Mute outputs for the other ports
     */
    for (i = 0; i < out->disableGIDCount; i++)
    {
	enable_mute(&(out->disableGID[i]), 1);
    }
    enable_mute(&out->enableGID, 0);
    return;
}

/*
 * Get output port.
 */

audio_port_t
alsa_audio_oport_get(audio_desc_t ad)
{
    debug_msg("oport_get %d\n", ad);
    return CurOutPort;
}

int
alsa_audio_oport_count(audio_desc_t ad)
{
	int n = ALSA_NUM_OUTPORTS;
	debug_msg("get oport count %d returning %d\n", ad, n);
	return n;
}

const audio_port_details_t*
alsa_audio_oport_details(audio_desc_t ad, int idx)
{
	debug_msg("oport details ad=%d idx=%d\n", ad, idx);
        return &out_ports[idx];
}

/*
 * set the loopback gain on the currently selected port to match CurLoopbackGain
 * This might properly turn off loopback on the other devices.
 */
static void setLoopbackGain()
{
    int mute;

    mute = (CurLoopbackGain == 0);
    enable_mute(&(InPorts[CurInPort].loopbackGID), mute);
}

/*
 * Set input port.
 */
void
alsa_audio_iport_set(audio_desc_t ad, audio_port_t port)
{
    int i;
    struct MixerInPortInfo *in;

    UNUSED(ad);
    debug_msg("iport_set %d %d %s\n", ad, port, in_ports[port].name);

    CurInPort = port;
    in = &(InPorts[CurInPort]);

    /*
     * Turn off capture & mute input for the other ports
     */
    for (i = 0; i < in->noncaptureGIDCount; i++)
    {
	enable_capture(&(in->noncaptureGID[i]), 0);
	enable_mute(&(in->noncaptureGID[i]), 1);
    }
    enable_capture(&in->captureGID, 1);

    /*
     * Also set the loopback status for this. It's a global state in
     * rat, not per-port.
     */
    setLoopbackGain();
    return;
}


/*
 * Get input port.
 */
audio_port_t
alsa_audio_iport_get(audio_desc_t ad)
{
    UNUSED(ad);
    debug_msg("iport_get %d returns %d\n", ad, CurInPort);
    return CurInPort;
}

int
alsa_audio_iport_count(audio_desc_t ad)
{
    int n =  ALSA_NUM_INPORTS;
    UNUSED(ad);
/*    debug_msg("get iport count %d returning %d\n", ad, n);*/
    return n;
}

const audio_port_details_t*
alsa_audio_iport_details(audio_desc_t ad, int idx)
{
    UNUSED(ad);
	debug_msg("iport details ad=%d idx=%d\n", ad, idx);
        return &in_ports[idx];
}

/*
 * Enable hardware loopback
 */
void 
alsa_audio_loopback(audio_desc_t ad, int gain)
{
        UNUSED(ad);
        UNUSED(gain);
	debug_msg("loopback gain=%d\n", gain);
	CurLoopbackGain = gain;
	setLoopbackGain();
        /* Nothing doing... */
}

/*
 * For external purposes this function returns non-zero
 * if audio is ready.
 */
int
alsa_audio_is_ready(audio_desc_t ad)
{
    UNUSED(ad);
    return InputBuffer.audioReadyFunc();
}


void
alsa_audio_wait_for(audio_desc_t ad, int delay_ms)
{
    UNUSED(ad);

    InputBuffer.audioWaitForFunc(delay_ms);
}

char *
alsa_get_device_name(audio_desc_t idx)
{
        debug_msg("get name for card %d\n", idx);
	if (idx < 0 || idx >= nRatCards)
	{
	    debug_msg("Card %d out of range 0..%d\n",
		      idx, nRatCards - 1);
	    return NULL;
	}
	else
	{
	    return(ratCards[idx].name);
	}
}

int
alsa_audio_init()
{
    int n_cards = snd_cards();
    int card;

    /*
     * Find all the PCM devices
     */
    for (card = 0; card < n_cards; card++)
    {
	int err;  
	snd_ctl_t *handle;  
	snd_ctl_hw_info_t info;
	unsigned int pcmdev;

	if ((err = snd_ctl_open(&handle, card)) < 0) {  
	    debug_msg("open failed: %s\n", snd_strerror(err)); 
	    continue;
	}  

	if ((err = snd_ctl_hw_info(handle, &info)) < 0) { 
	    debug_msg("hw info failed: %s\n", 
		    snd_strerror(err));  

	    snd_ctl_close(handle); 
	    continue;
	} 

	dump_hw_info(&info);

	/* Scan the PCM device list */
	for (pcmdev = 0; pcmdev < info.pcmdevs; pcmdev++)
	{
	    snd_pcm_info_t pinfo;
	    RatCardInfo *ratCard;
	    
	    if (snd_ctl_pcm_info(handle, pcmdev, &pinfo) == 0)
	    {
		dump_pcm_info(&pinfo);
	    }
	    else
	    {
		debug_msg("Couldn't get pcm info for card %d dev %d\n",
			  card, pcmdev);
	    }
	    /*
	     * Hm, goofy. SbLive shows 3 pcm devices per card. We'll just
	     * use the first one.
	     */

	    if (pcmdev == 0)
	    {
		ratCard = &(ratCards[nRatCards++]);
		ratCard->cardNumber = card;
		ratCard->pcmDevice = 0;
		snprintf(ratCard->name, sizeof(ratCard->name),
			 "ALSA: %s (%s)",
			 info.name, info.id);
		if (info.mixerdevs > 0)
		    ratCard->mixerDevice = 0;
		else
		    ratCard->mixerDevice = -1;
	    }
	}

	    
	snd_ctl_close(handle);  
    }
    debug_msg("alsa_audio_device_count: nDevices = %d\n", nRatCards);
    
    return nRatCards;
}

int
alsa_get_device_count()
{
    return nRatCards;
}

int
alsa_audio_supports(audio_desc_t ad, audio_format *fmt)
{
        UNUSED(ad);
        if ((!(fmt->sample_rate % 8000) || !(fmt->sample_rate % 11025)) && 
            (fmt->channels == 1 || fmt->channels == 2)) {
                return TRUE;
        }
        return FALSE;
}


void ImNotUsedHereJustFaking(void);

void ImNotUsedHereJustFaking(void)
{
    dump_mixer_group(0);
}


/* ratconf.h.  Generated automatically by configure.  */
/* config.h.in.  Generated automatically from configure.in by autoheader.  */

/* Define if type char is unsigned and you are not using gcc.  */
#ifndef __CHAR_UNSIGNED__
/* #undef __CHAR_UNSIGNED__ */
#endif

/* Define to empty if the keyword does not work.  */
/* #undef const */

/* Define if you have <sys/wait.h> that is POSIX.1 compatible.  */
#define HAVE_SYS_WAIT_H 1

/* Define to `unsigned' if <sys/types.h> doesn't define.  */
/* #undef size_t */

/* Define if you have the ANSI C header files.  */
#define STDC_HEADERS 1

/* Define if your processor stores words with the most significant
   byte first (like Motorola and SPARC, unlike Intel and VAX).  */
/* #undef WORDS_BIGENDIAN */

/* Define if the X Window System is missing or not being used.  */
/* #undef X_DISPLAY_MISSING */

/*
 * Define this if your C library doesn't have usleep.
 *
 * $Id: ratconf.h,v 1.1 2002/02/04 13:23:34 Psycho Exp $
 */
/* #undef NEED_USLEEP */
/* #undef NEED_SNPRINTF */

/* 
 * Missing declarations
 */
/* #undef GETTOD_NOT_DECLARED */
/* #undef KILL_NOT_DECLARED */

/*
 * If you don't have these types in <inttypes.h>, #define these to be
 * the types you do have.
 */
/* #undef int16_t */
/* #undef int32_t */
/* #undef int64_t */
/* #undef int8_t */
/* #undef uint16_t */
/* #undef uint32_t */
/* #undef uint8_t */

/*
 * Debugging:
 * DEBUG: general debugging
 * DEBUG_MEM: debug memory allocation
 */
/* #undef DEBUG */
/* #undef DEBUG_MEM */

/*
 * Optimization
 */
/* #undef NDEBUG */

/* Audio device relate */
/* #undef HAVE_SPARC_AUDIO */
/* #undef HAVE_SGI_AUDIO */
/* #undef HAVE_PCA_AUDIO */
/* #undef HAVE_LUIGI_AUDIO */
/* #undef HAVE_NEWPCM_AUDIO */
#define HAVE_OSS_AUDIO 1
/* #undef HAVE_HP_AUDIO */
/* #undef HAVE_NETBSD_AUDIO */
/* #undef HAVE_OSPREY_AUDIO */
/* #undef HAVE_MACHINE_PCAUDIOIO_H */
#define HAVE_ALSA_AUDIO 1
/* #undef HAVE_IXJ_AUDIO */

/* #undef HAVE_G728 */

#define HAVE_IPv6 1

/* GSM related */
#define SASR 1
#define FAST 1
#define USE_FLOAT_MUL 1

/* Define if you have the <bstring.h> header file.  */
/* #undef HAVE_BSTRING_H */

/* Define if you have the <inttypes.h> header file.  */
#define HAVE_INTTYPES_H 1

/* Define if you have the <machine/pcaudioio.h> header file.  */
/* #undef HAVE_MACHINE_PCAUDIOIO_H */

/* Define if you have the <malloc.h> header file.  */
#define HAVE_MALLOC_H 1

/* Define if you have the <soundcard.h> header file.  */
/* #undef HAVE_SOUNDCARD_H */

/* Define if you have the <stdint.h> header file.  */
#define HAVE_STDINT_H 1

/* Define if you have the <stropts.h> header file.  */
#define HAVE_STROPTS_H 1

/* Define if you have the <sys/filio.h> header file.  */
/* #undef HAVE_SYS_FILIO_H */

/* Define if you have the <sys/sockio.h> header file.  */
/* #undef HAVE_SYS_SOCKIO_H */

/* Define if you have the <sys/soundcard.h> header file.  */
#define HAVE_SYS_SOUNDCARD_H 1

#ifndef WORDS_BIGENDIAN
#define WORDS_SMALLENDIAN
#endif

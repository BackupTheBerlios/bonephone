/*
 * tkWinInt.h --
 *
 *	This file contains declarations that are shared among the
 *	Windows-specific parts of Tk, but aren't used by the rest of
 *	Tk.
 *
 * Copyright (c) 1995 Sun Microsystems, Inc.
 *
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 *
 * SCCS: @(#) tkWinInt.h 1.34 97/09/02 13:06:20
 */

#ifndef _TKWININT
#define _TKWININT

#ifndef _TKINT
#include "tkInt.h"
#endif

/*
 * Include platform specific public interfaces.
 */

#ifndef _TKWIN
#include "tkWin.h"
#endif
#if (__BORLANDC__ && __BORLANDC__<=0x460)
#define COLOR_3DFACE            COLOR_BTNFACE
#define COLOR_3DSHADOW          COLOR_BTNSHADOW
#define COLOR_3DHIGHLIGHT       COLOR_BTNHIGHLIGHT
#define COLOR_3DHILIGHT         COLOR_BTNHIGHLIGHT
#define COLOR_3DDKSHADOW        21
#define COLOR_3DLIGHT           22
#define COLOR_INFOBK            24
#define COLOR_INFOTEXT          23
#define BST_UNCHECKED      0x0000
#define BST_CHECKED        0x0001
#define BST_INDETERMINATE  0x0002
#define BST_PUSHED         0x0004
#define BST_FOCUS          0x0008
#define SM_CXEDGE               45
#define SS_OWNERDRAW        0x0000000DL
#define OFN_EXPLORER                 0x00080000
#define MB_ICONERROR                MB_ICONHAND
#define MB_ICONWARNING              MB_ICONEXCLAMATION
#define MB_DEFBUTTON4               0x00000300L
#define MB_TOPMOST                  0x00040000L
typedef struct tagNONCLIENTMETRICSA
{
    UINT    cbSize;
    int     iBorderWidth;
    int     iScrollWidth;
    int     iScrollHeight;
    int     iCaptionWidth;
    int     iCaptionHeight;
    LOGFONTA lfCaptionFont;
    int     iSmCaptionWidth;
    int     iSmCaptionHeight;
    LOGFONTA lfSmCaptionFont;
    int     iMenuWidth;
    int     iMenuHeight;
    LOGFONTA lfMenuFont;
    LOGFONTA lfStatusFont;
    LOGFONTA lfMessageFont;
}   NONCLIENTMETRICSA, *PNONCLIENTMETRICSA, FAR* LPNONCLIENTMETRICSA;
typedef struct tagNONCLIENTMETRICSW
{
    UINT    cbSize;
    int     iBorderWidth;
    int     iScrollWidth;
    int     iScrollHeight;
    int     iCaptionWidth;
    int     iCaptionHeight;
    LOGFONTW lfCaptionFont;
    int     iSmCaptionWidth;
    int     iSmCaptionHeight;
    LOGFONTW lfSmCaptionFont;
    int     iMenuWidth;
    int     iMenuHeight;
    LOGFONTW lfMenuFont;
    LOGFONTW lfStatusFont;
    LOGFONTW lfMessageFont;
}   NONCLIENTMETRICSW, *PNONCLIENTMETRICSW, FAR* LPNONCLIENTMETRICSW;
#ifdef UNICODE
typedef NONCLIENTMETRICSW NONCLIENTMETRICS;
typedef PNONCLIENTMETRICSW PNONCLIENTMETRICS;
typedef LPNONCLIENTMETRICSW LPNONCLIENTMETRICS;
#else
typedef NONCLIENTMETRICSA NONCLIENTMETRICS;
typedef PNONCLIENTMETRICSA PNONCLIENTMETRICS;
typedef LPNONCLIENTMETRICSA LPNONCLIENTMETRICS;
#endif // UNICODE
#define SPI_GETNONCLIENTMETRICS    41
#define VER_PLATFORM_WIN32_WINDOWS      1
#define SM_CXMENUCHECK          71
#define SM_CYMENUCHECK          72
#define SM_CXFIXEDFRAME           SM_CXDLGFRAME
#define WM_ENTERSIZEMOVE                0x0231
#define WM_EXITSIZEMOVE                 0x0232
#define WM_NOTIFY                       0x004E
#define SIF_RANGE           0x0001
#define SIF_PAGE            0x0002
#define SIF_POS             0x0004
#define SIF_DISABLENOSCROLL 0x0008
#define SIF_TRACKPOS        0x0010
#define SIF_ALL             (SIF_RANGE | SIF_PAGE | SIF_POS | SIF_TRACKPOS)

typedef struct tagSCROLLINFO {  // si
    UINT cbSize;
    UINT fMask;
    int  nMin;
    int  nMax;
    UINT nPage;
    int  nPos;
    int  nTrackPos;
}   SCROLLINFO;
typedef SCROLLINFO FAR *LPSCROLLINFO;

int WINAPI SetScrollInfo(HWND, int, LPSCROLLINFO, BOOL);
typedef struct tagNMHDR
{
    HWND  hwndFrom;
    UINT  idFrom;
    UINT  code;         // NM_ code
}   NMHDR;
typedef NMHDR FAR * LPNMHDR;


#endif /*__BORLANDC__*/

#include "tkWinGdi.h"

/*
 * Define constants missing from older Win32 SDK header files.
 */

#ifndef WS_EX_TOOLWINDOW
#define WS_EX_TOOLWINDOW	0x00000080L 
#endif

typedef struct TkFontAttributes TkFontAttributes;

/*
 * The TkWinDCState is used to save the state of a device context
 * so that it can be restored later.
 */

typedef struct TkWinDCState {
    HPALETTE palette;
} TkWinDCState;

/*
 * The TkWinDrawable is the internal implementation of an X Drawable (either
 * a Window or a Pixmap).  The following constants define the valid Drawable
 * types.
 */

#define TWD_BITMAP	1
#define TWD_WINDOW	2
#define TWD_WINDC	3

typedef struct {
    int type;
    HWND handle;
    TkWindow *winPtr;
} TkWinWindow;

typedef struct {
    int type;
    HBITMAP handle;
    Colormap colormap;
    int depth;
} TkWinBitmap;

typedef struct {
    int type;
    HDC hdc;
}TkWinDC;

typedef union {
    int type;
    TkWinWindow window;
    TkWinBitmap bitmap;
    TkWinDC winDC;
} TkWinDrawable;

/*
 * The following macros are used to retrieve internal values from a Drawable.
 */

#define TkWinGetHWND(w) (((TkWinDrawable *) w)->window.handle)
#define TkWinGetWinPtr(w) (((TkWinDrawable*)w)->window.winPtr)
#define TkWinGetHBITMAP(w) (((TkWinDrawable*)w)->bitmap.handle)
#define TkWinGetColormap(w) (((TkWinDrawable*)w)->bitmap.colormap)
#define TkWinGetHDC(w) (((TkWinDrawable *) w)->winDC.hdc)

/*
 * The following structure is used to encapsulate palette information.
 */

typedef struct {
    HPALETTE palette;		/* Palette handle used when drawing. */
    UINT size;			/* Number of entries in the palette. */
    int stale;			/* 1 if palette needs to be realized,
				 * otherwise 0.  If the palette is stale,
				 * then an idle handler is scheduled to
				 * realize the palette. */
    Tcl_HashTable refCounts;	/* Hash table of palette entry reference counts
				 * indexed by pixel value. */
} TkWinColormap;

/*
 * The following macro retrieves the Win32 palette from a colormap.
 */

#define TkWinGetPalette(colormap) (((TkWinColormap *) colormap)->palette)

/*
 * The following macros define the class names for Tk Window types.
 */

#define TK_WIN_TOPLEVEL_CLASS_NAME "TkTopLevel"
#define TK_WIN_CHILD_CLASS_NAME "TkChild"

/*
 * The following variable indicates whether we are restricted to Win32s
 * GDI calls.
 */

extern int tkpIsWin32s;

/*
 * The following variable is a translation table between X gc functions and
 * Win32 raster op modes.
 */

extern int tkpWinRopModes[];

/*
 * The following defines are used with TkWinGetBorderPixels to get the
 * extra 2 border colors from a Tk_3DBorder.
 */

#define TK_3D_LIGHT2 TK_3D_DARK_GC+1
#define TK_3D_DARK2 TK_3D_DARK_GC+2

/*
 * Internal procedures used by more than one source file.
 */

extern void dprintf    _ANSI_ARGS_((const char *format, ...));
extern void             TkWinFreeCompatDCs _ANSI_ARGS_((void));
extern void             TkWinFreePixmaps   _ANSI_ARGS_((void));
extern LRESULT CALLBACK	TkWinChildProc _ANSI_ARGS_((HWND hwnd, UINT message,
			    WPARAM wParam, LPARAM lParam));
extern void		TkWinClipboardRender _ANSI_ARGS_((TkDisplay *dispPtr,
			    UINT format));
extern LRESULT		TkWinEmbeddedEventProc _ANSI_ARGS_((HWND hwnd,
			    UINT message, WPARAM wParam, LPARAM lParam));
extern void		TkWinFillRect _ANSI_ARGS_((HDC dc, int x, int y,
			    int width, int height, int pixel));
#define FILLRECTGC
#ifdef FILLRECTGC
extern void		TkWinFillRectGC _ANSI_ARGS_((HDC dc, int x, int y,
			    int width, int height, int pixel, GC gc));
#endif
extern COLORREF		TkWinGetBorderPixels _ANSI_ARGS_((Tk_Window tkwin,
			    Tk_3DBorder border, int which));
extern HDC		TkWinGetDrawableDC _ANSI_ARGS_((Display *display,
			    Drawable d, TkWinDCState* state));
extern int		TkWinGetModifierState _ANSI_ARGS_((void));
extern HPALETTE		TkWinGetSystemPalette _ANSI_ARGS_((void));
extern HWND		TkWinGetWrapperWindow _ANSI_ARGS_((Tk_Window tkwin));
extern int		TkWinHandleMenuEvent _ANSI_ARGS_((HWND *phwnd,
			    UINT *pMessage, WPARAM *pwParam, LPARAM *plParam,
			    LRESULT *plResult));
extern int		TkWinIndexOfColor _ANSI_ARGS_((XColor *colorPtr));
extern void		TkWinPointerDeadWindow _ANSI_ARGS_((TkWindow *winPtr));
extern void		TkWinPointerEvent _ANSI_ARGS_((HWND hwnd, int x,
			    int y));
extern void		TkWinPointerInit _ANSI_ARGS_((void));
extern LRESULT 		TkWinReflectMessage _ANSI_ARGS_((HWND hwnd,
			    UINT message, WPARAM wParam, LPARAM lParam));
extern void		TkWinReleaseDrawableDC _ANSI_ARGS_((Drawable d,
			    HDC hdc, TkWinDCState* state));
extern LRESULT		TkWinResendEvent _ANSI_ARGS_((WNDPROC wndproc,
			    HWND hwnd, XEvent *eventPtr));
extern HPALETTE		TkWinSelectPalette _ANSI_ARGS_((HDC dc,
			    Colormap colormap));
extern void		TkWinSetMenu _ANSI_ARGS_((Tk_Window tkwin,
			    HMENU hMenu));
extern void		TkWinSetWindowPos _ANSI_ARGS_((HWND hwnd,
			    HWND siblingHwnd, int pos));
extern void		TkWinUpdateCursor _ANSI_ARGS_((TkWindow *winPtr));
extern void		TkWinWmCleanup _ANSI_ARGS_((HINSTANCE hInstance));
extern HWND		TkWinWmFindEmbedAssociation _ANSI_ARGS_((
			    TkWindow *winPtr));
extern void		TkWinWmStoreEmbedAssociation _ANSI_ARGS_((
			    TkWindow *winPtr, HWND hwnd));
extern void		TkWinXCleanup _ANSI_ARGS_((HINSTANCE hInstance));
extern void 		TkWinXInit _ANSI_ARGS_((HINSTANCE hInstance));

extern void		TkWinGdiInit _ANSI_ARGS_((HINSTANCE hInstance));
extern void		TkWinGdiCleanup _ANSI_ARGS_((HINSTANCE hInstance));
extern HDC		TkWinGetNULLDC _ANSI_ARGS_((void)); 
extern void		TkWinReleaseNULLDC _ANSI_ARGS_((HDC hdc)); 
extern HBRUSH           TkWinCreateSolidBrush _ANSI_ARGS_((GC gc,COLORREF color));
extern BOOL             TkWinDeleteBrush _ANSI_ARGS_((GC gc,HBRUSH hBrush));
#ifdef USE_CKGRAPH_IMP
  extern int tkWinHashBrushs;
  extern int tkWinHashPens;
#endif

#endif /* _TKWININT */


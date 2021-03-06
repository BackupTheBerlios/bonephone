/* 
 * tkWinFont.c --
 *
 *	Contains the Windows implementation of the platform-independant
 *	font package interface.
 *
 * Copyright (c) 1995-1997 Sun Microsystems, Inc.
 * Copyright (c) 1994 Software Research Associates, Inc. 
 *
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 *
 * SCCS: @(#) tkWinFont.c 1.20 97/05/14 15:45:30
 */

#include "tkWinInt.h"
#include "tkFont.h"

/*
 * The following structure represents Windows' implementation of a font.
 */

typedef struct WinFont {
    TkFont font;		/* Stuff used by generic font package.  Must
				 * be first in structure. */
    HFONT hFont;		/* Windows information about font. */
    HWND hwnd;			/* Toplevel window of application that owns
				 * this font, used for getting HDC. */
} WinFont;

/*
 * The following structure is used as to map between the Tcl strings
 * that represent the system fonts and the numbers used by Windows.
 */

static TkStateMap systemMap[] = {
    {ANSI_FIXED_FONT,	    "ansifixed"},
    {ANSI_VAR_FONT,	    "ansi"},
    {DEVICE_DEFAULT_FONT,   "device"},
    {OEM_FIXED_FONT,	    "oemfixed"},
    {SYSTEM_FIXED_FONT,	    "systemfixed"},
    {SYSTEM_FONT,	    "system"},
    {-1,		    NULL}
};

#define ABS(x)          (((x) < 0) ? -(x) : (x))

static TkFont *		AllocFont _ANSI_ARGS_((TkFont *tkFontPtr, 
                            Tk_Window tkwin, HFONT hFont));
static char *		GetProperty _ANSI_ARGS_((CONST TkFontAttributes *faPtr,
			    CONST char *option));
static int CALLBACK	WinFontFamilyEnumProc _ANSI_ARGS_((ENUMLOGFONT *elfPtr,
			    NEWTEXTMETRIC *ntmPtr, int fontType,
			    LPARAM lParam));


/*
 *---------------------------------------------------------------------------
 *
 * TkpGetNativeFont --
 *
 *	Map a platform-specific native font name to a TkFont.
 *
 * Results:
 * 	The return value is a pointer to a TkFont that represents the
 *	native font.  If a native font by the given name could not be
 *	found, the return value is NULL.  
 *
 *	Every call to this procedure returns a new TkFont structure,
 *	even if the name has already been seen before.  The caller should
 *	call TkpDeleteFont() when the font is no longer needed.
 *
 *	The caller is responsible for initializing the memory associated
 *	with the generic TkFont when this function returns and releasing
 *	the contents of the generic TkFont before calling TkpDeleteFont().
 *
 * Side effects:
 *	None.
 *
 *---------------------------------------------------------------------------
 */

TkFont *
TkpGetNativeFont(tkwin, name)
    Tk_Window tkwin;		/* For display where font will be used. */
    CONST char *name;		/* Platform-specific font name. */
{
    int object;
    HFONT hFont;
    
    object = TkFindStateNum(NULL, NULL, systemMap, name);
    if (object < 0) {
	return NULL;
    }
    hFont = CkGetStockObject(object);
    if (hFont == NULL) {
	panic("TkpGetNativeFont: can't allocate stock font");
    }

    return AllocFont(NULL, tkwin, hFont);
}

/*
 *---------------------------------------------------------------------------
 *
 * TkpGetFontFromAttributes -- 
 *
 *	Given a desired set of attributes for a font, find a font with
 *	the closest matching attributes.
 *
 * Results:
 * 	The return value is a pointer to a TkFont that represents the
 *	font with the desired attributes.  If a font with the desired
 *	attributes could not be constructed, some other font will be
 *	substituted automatically.  NULL is never returned.
 *
 *	Every call to this procedure returns a new TkFont structure,
 *	even if the specified attributes have already been seen before.
 *	The caller should call TkpDeleteFont() to free the platform-
 *	specific data when the font is no longer needed.  
 *
 *	The caller is responsible for initializing the memory associated
 *	with the generic TkFont when this function returns and releasing
 *	the contents of the generic TkFont before calling TkpDeleteFont().
 *
 * Side effects:
 *	None.
 *
 *---------------------------------------------------------------------------
 */
TkFont *
TkpGetFontFromAttributes(tkFontPtr, tkwin, faPtr)
    TkFont *tkFontPtr;		/* If non-NULL, store the information in
				 * this existing TkFont structure, rather than
				 * allocating a new structure to hold the
				 * font; the existing contents of the font
				 * will be released.  If NULL, a new TkFont
				 * structure is allocated. */
    Tk_Window tkwin;		/* For display where font will be used. */
    CONST TkFontAttributes *faPtr;  /* Set of attributes to match. */
{
    LOGFONT lf;
    HFONT hFont;
    int result=0;
    //not necessary , leo
    //Window window;
    //HWND hwnd;
    //HDC hdc;

    //window = Tk_WindowId(((TkWindow *) tkwin)->mainPtr->winPtr);
    //hwnd = (window == None) ? NULL : TkWinGetHWND(window);

    //hdc = CkGetDC(hwnd);
    lf.lfHeight		= -faPtr->pointsize;
    if (lf.lfHeight < 0) {
	lf.lfHeight = MulDiv(lf.lfHeight, 
	        254 * WidthOfScreen(Tk_Screen(tkwin)),
		720 * WidthMMOfScreen(Tk_Screen(tkwin)));
    }
    lf.lfWidth		= 0;
    lf.lfEscapement	= 0;
    lf.lfOrientation	= 0;
    lf.lfWeight		= (faPtr->weight == TK_FW_NORMAL) ? FW_NORMAL : FW_BOLD;
    lf.lfItalic		= faPtr->slant;
    lf.lfUnderline	= faPtr->underline;
    lf.lfStrikeOut	= faPtr->overstrike;

	/* try and match font with character set - KH */
	result=strlen(faPtr->family);
    if (strcmp(faPtr->family+result-3, "Cyr")==0) {
        lf.lfCharSet	= RUSSIAN_CHARSET;
    } else if (strcmp(faPtr->family+result-2, "CE")==0) {
        lf.lfCharSet	= EASTEUROPE_CHARSET;    
	} else if (strcmp(faPtr->family+result-6, "Baltic")==0) {
        lf.lfCharSet	= BALTIC_CHARSET;    
	} else if (strcmp(faPtr->family+result-3, "Tur")==0) {
        lf.lfCharSet	= TURKISH_CHARSET;    
	} else if (strcmp(faPtr->family+result-5, "Greek")==0) {
        lf.lfCharSet	= GREEK_CHARSET;    
	} else {
        lf.lfCharSet	= DEFAULT_CHARSET;    
	}

    lf.lfOutPrecision	= OUT_DEFAULT_PRECIS;
    lf.lfClipPrecision	= CLIP_DEFAULT_PRECIS;
    lf.lfQuality	= DEFAULT_QUALITY;
    lf.lfPitchAndFamily = DEFAULT_PITCH | FF_DONTCARE;
    if (faPtr->family == NULL) {
	lf.lfFaceName[0] = '\0';
    } else {
	lstrcpyn(lf.lfFaceName, faPtr->family, sizeof(lf.lfFaceName));
    }
    //CkReleaseDC(hwnd, hdc);

    /*
     * Replace the standard X and Mac family names with the names that
     * Windows likes.
     */

    if ((stricmp(lf.lfFaceName, "Times") == 0)
	    || (stricmp(lf.lfFaceName, "New York") == 0)) {
	strcpy(lf.lfFaceName, "Times New Roman");
    } else if ((stricmp(lf.lfFaceName, "Courier") == 0)
	    || (stricmp(lf.lfFaceName, "Monaco") == 0)) {
	strcpy(lf.lfFaceName, "Courier New");
    } else if ((stricmp(lf.lfFaceName, "Helvetica") == 0)
	    || (stricmp(lf.lfFaceName, "Geneva") == 0)) {
	strcpy(lf.lfFaceName, "Arial");
    }

    hFont = CkCreateFontIndirect(&lf);
    if (hFont == NULL) {
        hFont = CkGetStockObject(SYSTEM_FONT);
	if (hFont == NULL) {
	    panic("TkpGetFontFromAttributes: cannot get system font");
	}
    }
    return AllocFont(tkFontPtr, tkwin, hFont);
}

/*
 *---------------------------------------------------------------------------
 *
 * TkpDeleteFont --
 *
 *	Called to release a font allocated by TkpGetNativeFont() or
 *	TkpGetFontFromAttributes().  The caller should have already
 *	released the fields of the TkFont that are used exclusively by
 *	the generic TkFont code.
 *
 * Results:
 *	None.
 *
 * Side effects:
 *	TkFont is deallocated.
 *
 *---------------------------------------------------------------------------
 */

void
TkpDeleteFont(tkFontPtr)
    TkFont *tkFontPtr;		/* Token of font to be deleted. */
{
    WinFont *fontPtr;

    fontPtr = (WinFont *) tkFontPtr;
    CkDeleteFont(fontPtr->hFont);
    ckfree((char *) fontPtr);
}

/*
 *---------------------------------------------------------------------------
 *
 * TkpGetFontFamilies, WinFontEnumFamilyProc --
 *
 *	Return information about the font families that are available
 *	on the display of the given window.
 *
 * Results:
 *	interp->result is modified to hold a list of all the available
 *	font families.
 *
 * Side effects:
 *	None.
 *
 *---------------------------------------------------------------------------
 */
 
void
TkpGetFontFamilies(interp, tkwin)
    Tcl_Interp *interp;		/* Interp to hold result. */
    Tk_Window tkwin;		/* For display to query. */
{    
    Window window;
    HWND hwnd;
    HDC hdc;

    window = Tk_WindowId(tkwin);
    hwnd = (window == (Window) NULL) ? NULL : TkWinGetHWND(window);

    hdc = CkGetDC(hwnd);
    EnumFontFamilies(hdc, NULL, (FONTENUMPROC) WinFontFamilyEnumProc,
	    (LPARAM) interp);
    CkReleaseDC(hwnd, hdc);
}

/* ARGSUSED */

static int CALLBACK
WinFontFamilyEnumProc(elfPtr, ntmPtr, fontType, lParam)
    ENUMLOGFONT *elfPtr;	/* Logical-font data. */
    NEWTEXTMETRIC *ntmPtr;	/* Physical-font data (not used). */
    int fontType;		/* Type of font (not used). */
    LPARAM lParam;		/* Interp to hold result. */
{
    Tcl_Interp *interp;

    interp = (Tcl_Interp *) lParam;
    Tcl_AppendElement(interp, elfPtr->elfLogFont.lfFaceName);
    return 1;
}

/*
 *---------------------------------------------------------------------------
 *
 *  Tk_MeasureChars --
 *
 *	Determine the number of characters from the string that will fit
 *	in the given horizontal span.  The measurement is done under the
 *	assumption that Tk_DrawChars() will be used to actually display
 *	the characters.
 *
 * Results:
 *	The return value is the number of characters from source that
 *	fit into the span that extends from 0 to maxLength.  *lengthPtr is
 *	filled with the x-coordinate of the right edge of the last
 *	character that did fit.
 *
 * Side effects:
 *	None.
 *
 *---------------------------------------------------------------------------
 */
int
Tk_MeasureChars(tkfont, source, numChars, maxLength, flags, lengthPtr)
    Tk_Font tkfont;		/* Font in which characters will be drawn. */
    CONST char *source;		/* Characters to be displayed.  Need not be
				 * '\0' terminated. */
    int numChars;		/* Maximum number of characters to consider
				 * from source string. */
    int maxLength;		/* If > 0, maxLength specifies the longest
				 * permissible line length; don't consider any
				 * character that would cross this
				 * x-position.  If <= 0, then line length is
				 * unbounded and the flags argument is
				 * ignored. */
    int flags;			/* Various flag bits OR-ed together:
				 * TK_PARTIAL_OK means include the last char
				 * which only partially fit on this line.
				 * TK_WHOLE_WORDS means stop on a word
				 * boundary, if possible.
				 * TK_AT_LEAST_ONE means return at least one
				 * character even if no characters fit. */
    int *lengthPtr;		/* Filled with x-location just after the
				 * terminating character. */
{
    WinFont *fontPtr;
    HDC hdc;
#ifndef USE_CKGRAPH_IMP
    HFONT hFont;
#endif
    int curX, curIdx;

    fontPtr = (WinFont *) tkfont;

    //hdc = CkGetDC(fontPtr->hwnd);
    hdc = TkWinGetNULLDC();
#ifdef USE_CKGRAPH_IMP
    CkSelectFont(hdc, fontPtr->hFont);
#else
    hFont = CkSelectFont(hdc, fontPtr->hFont);
#endif

    if (numChars == 0) {
	curX = 0;
	curIdx = 0;
    } else if (maxLength <= 0) {
	SIZE size;

	GetTextExtentPoint32(hdc, source, numChars, &size);
	curX = size.cx;
	curIdx = numChars;
    } else {
	int max;
	int *partials;
	SIZE size;
	
	partials = (int *) ckalloc(numChars * sizeof (int));
	GetTextExtentExPoint(hdc, source, numChars, maxLength, &max,
		partials, &size);
	
	if ((flags & TK_WHOLE_WORDS) && max < numChars) {
	    int sawSpace;
	    int i;
	    
	    sawSpace = 0;
	    i = max;
	    while (i >= 0 && !isspace(source[i])) {
    		--i;
            }
	    while (i >= 0 && isspace(source[i])) {
		sawSpace = 1;
		--i;
            }

	    /*
	     * If a space char was not found, and the flag for forcing
	     * at least on (or more) chars to be drawn is false, then
	     * set MAX to zero so no text is drawn.  Otherwise, if a
	     * space was found, set max to be one char past the space.
	     */
	    
	    if ((i < 0) && !(flags & TK_AT_LEAST_ONE)) {
		max = 0;
	    } else if (sawSpace) {
		max = i + 1;
	    }
		
	}

	if (max == 0) {
	    curX = 0;
	} else {
	    curX = partials[max - 1];
	}

	if (((flags & TK_PARTIAL_OK) && max < numChars && curX < maxLength)
		|| ((flags & TK_AT_LEAST_ONE) && max == 0 && numChars > 0)) {
	    /*
	     * We want to include the first character that didn't
	     * quite fit.  Call the function again to include the
	     * width of the extra character.
             */
	    
	    GetTextExtentExPoint(hdc, source, max + 1, 0, NULL, partials,
                       &size);
	    curX = partials[max];
	    ++max;

	} 
		
	ckfree((char *) partials);
	curIdx = max;
    }
#ifndef USE_CKGRAPH_IMP
    CkSelectFont(hdc, hFont);
#endif
    //CkReleaseDC(fontPtr->hwnd, hdc);
    TkWinReleaseNULLDC(hdc);

    *lengthPtr = curX;
    return curIdx;
}

/*
 *---------------------------------------------------------------------------
 *
 * Tk_DrawChars --
 *
 *	Draw a string of characters on the screen.  
 *
 * Results:
 *	None.
 *
 * Side effects:
 *	Information gets drawn on the screen.
 *
 *---------------------------------------------------------------------------
 */

void
Tk_DrawChars(display, drawable, gc, tkfont, source, numChars, x, y)
    Display *display;		/* Display on which to draw. */
    Drawable drawable;		/* Window or pixmap in which to draw. */
    GC gc;			/* Graphics context for drawing characters. */
    Tk_Font tkfont;		/* Font in which characters will be drawn;
				 * must be the same as font used in GC. */
    CONST char *source;		/* Characters to be displayed.  Need not be
				 * '\0' terminated.  All Tk meta-characters
				 * (tabs, control characters, and newlines)
				 * should be stripped out of the string that
				 * is passed to this function.  If they are
				 * not stripped out, they will be displayed as
				 * regular printing characters. */
    int numChars;		/* Number of characters in string. */
    int x, y;			/* Coordinates at which to place origin of
				 * string when drawing. */
{
    HDC dc;
#ifndef USE_CKGRAPH_IMP
    HFONT hFont;
#endif
    TkWinDCState state;
    WinFont *fontPtr;

    fontPtr = (WinFont *) gc->font;
    display->request++;

    if (drawable == None) {
	return;
    }

    dc = TkWinGetDrawableDC(display, drawable, &state);

    CkSetROP2(dc, tkpWinRopModes[gc->function]);

    if ((gc->fill_style == FillStippled
	    || gc->fill_style == FillOpaqueStippled)
	    && gc->stipple != None) {
	TkWinDrawable *twdPtr = (TkWinDrawable *)gc->stipple;
	HBRUSH oldBrush, stipple;
	HBITMAP oldBitmap, bitmap;
	HDC dcMem;
	TEXTMETRIC tm;
	SIZE size;

	if (twdPtr->type != TWD_BITMAP) {
	    panic("unexpected drawable type in stipple");
	}

#ifdef USE_CKGRAPH_IMP
	CkGraph_ClearDC(dc);
#endif
	/*
	 * Select stipple pattern into destination dc.
	 */
	
	dcMem = CkCreateCompatibleDC(dc);

	stipple = CkCreatePatternBrush(twdPtr->bitmap.handle);
	SetBrushOrgEx(dc, gc->ts_x_origin, gc->ts_y_origin, NULL);
	oldBrush = CkSelectBrush(dc, stipple);

	SetTextAlign(dcMem, TA_LEFT | TA_TOP);
	CkSetTextColor(dcMem, gc->foreground);
	CkSetBkMode(dcMem, TRANSPARENT);
	CkSetBkColor(dcMem, RGB(0, 0, 0));

#ifdef USE_CKGRAPH_IMP
        CkSelectFont(dcMem, fontPtr->hFont);
#else
        hFont = CkSelectFont(dcMem, fontPtr->hFont);
#endif

	/*
	 * Compute the bounding box and create a compatible bitmap.
	 */

	GetTextExtentPoint(dcMem, source, numChars, &size);
	GetTextMetrics(dcMem, &tm);
	size.cx -= tm.tmOverhang;
	bitmap = CkCreateCompatibleBitmap(dc, size.cx, size.cy);
	oldBitmap = CkSelectBitmap(dcMem, bitmap);

	/*
	 * The following code is tricky because fonts are rendered in multiple
	 * colors.  First we draw onto a black background and copy the white
	 * bits.  Then we draw onto a white background and copy the black bits.
	 * Both the foreground and background bits of the font are ANDed with
	 * the stipple pattern as they are copied.
	 */

	PatBlt(dcMem, 0, 0, size.cx, size.cy, BLACKNESS);
	CkTextOut(dcMem, 0, 0, source, numChars);
	CkBitBlt(dc, x, y - tm.tmAscent, size.cx, size.cy, dcMem,
		0, 0, 0xEA02E9);
	PatBlt(dcMem, 0, 0, size.cx, size.cy, WHITENESS);
	CkTextOut(dcMem, 0, 0, source, numChars);
	CkBitBlt(dc, x, y - tm.tmAscent, size.cx, size.cy, dcMem,
		0, 0, 0x8A0E06);

	/*
	 * Destroy the temporary bitmap and restore the device context.
	 */
#ifndef USE_CKGRAPH_IMP
        CkSelectFont(dcMem, hFont);
#endif
	CkSelectBitmap(dcMem, oldBitmap);
	CkDeleteBitmap(bitmap);
	CkDeleteDC(dcMem);
	CkSelectBrush(dc, oldBrush);
	CkDeleteBrush(stipple);
    } else {
	SetTextAlign(dc, TA_LEFT | TA_BASELINE);
	CkSetTextColor(dc, gc->foreground);
	CkSetBkMode(dc, TRANSPARENT);
#ifdef USE_CKGRAPH_IMP
	CkSelectFont(dc, fontPtr->hFont);
#else
	hFont = CkSelectFont(dc, fontPtr->hFont);
#endif
	CkTextOut(dc, x, y, source, numChars);
#ifndef USE_CKGRAPH_IMP
        CkSelectFont(dc, hFont);
#endif
    }
    TkWinReleaseDrawableDC(drawable, dc, &state);
}

/*
 *---------------------------------------------------------------------------
 *
 * AllocFont --
 *
 *	Helper for TkpGetNativeFont() and TkpGetFontFromAttributes().
 *	Allocates and intializes the memory for a new TkFont that
 *	wraps the platform-specific data.
 *
 * Results:
 *	Returns pointer to newly constructed TkFont.  
 *
 *	The caller is responsible for initializing the fields of the
 *	TkFont that are used exclusively by the generic TkFont code, and
 *	for releasing those fields before calling TkpDeleteFont().
 *
 * Side effects:
 *	Memory allocated.
 *
 *---------------------------------------------------------------------------
 */ 

static TkFont *
AllocFont(tkFontPtr, tkwin, hFont)
    TkFont *tkFontPtr;		/* If non-NULL, store the information in
				 * this existing TkFont structure, rather than
				 * allocating a new structure to hold the
				 * font; the existing contents of the font
				 * will be released.  If NULL, a new TkFont
				 * structure is allocated. */
    Tk_Window tkwin;		/* For display where font will be used. */
    HFONT hFont;		/* Windows information about font. */
{
    WinFont *fontPtr;
    HDC hdc;
    TEXTMETRIC tm;
    char buf[LF_FACESIZE];
    TkFontAttributes *faPtr;

    if (tkFontPtr != NULL) {
        fontPtr = (WinFont *) tkFontPtr;
        CkDeleteFont(fontPtr->hFont);
    } else {
        fontPtr = (WinFont *) ckalloc(sizeof(WinFont));
    }
    
    hdc = TkWinGetNULLDC();
#ifdef USE_CKGRAPH_IMP
    CkSelectFont(hdc, hFont);
#else
    hFont = CkSelectFont(hdc, hFont);
#endif
    GetTextFace(hdc, sizeof(buf), buf);
    GetTextMetrics(hdc, &tm);

    fontPtr->font.fid	= (Font) fontPtr;

    faPtr = &fontPtr->font.fa;
    faPtr->family	= Tk_GetUid(buf);
    faPtr->pointsize	= MulDiv(tm.tmHeight - tm.tmInternalLeading,
	    720 * WidthMMOfScreen(Tk_Screen(tkwin)),
	    254 * WidthOfScreen(Tk_Screen(tkwin)));
    faPtr->weight	= (tm.tmWeight > FW_MEDIUM) ? TK_FW_BOLD : TK_FW_NORMAL;
    faPtr->slant	= (tm.tmItalic != 0) ? TK_FS_ITALIC : TK_FS_ROMAN;
    faPtr->underline	= (tm.tmUnderlined != 0) ? 1 : 0;
    faPtr->overstrike	= (tm.tmStruckOut != 0) ? 1 : 0;

    fontPtr->font.fm.ascent	= tm.tmAscent;
    fontPtr->font.fm.descent	= tm.tmDescent;
    fontPtr->font.fm.maxWidth	= tm.tmMaxCharWidth;
    fontPtr->font.fm.fixed	= !(tm.tmPitchAndFamily & TMPF_FIXED_PITCH);
#ifndef USE_CKGRAPH_IMP
    hFont = CkSelectFont(hdc, hFont);
#endif
    TkWinReleaseNULLDC(hdc);

    fontPtr->hFont		= hFont;
    fontPtr->hwnd		= NULL;

    return (TkFont *) fontPtr;
}


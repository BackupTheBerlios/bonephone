# Microsoft Developer Studio Project File - Name="tklib" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Static Library" 0x0104

CFG=tklib - Win32 Debug IPv6
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "tklib.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "tklib.mak" CFG="tklib - Win32 Debug IPv6"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "tklib - Win32 Release" (based on "Win32 (x86) Static Library")
!MESSAGE "tklib - Win32 Debug" (based on "Win32 (x86) Static Library")
!MESSAGE "tklib - Win32 Debug IPv6" (based on "Win32 (x86) Static Library")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
RSC=rc.exe

!IF  "$(CFG)" == "tklib - Win32 Release"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "Release"
# PROP Intermediate_Dir "Release"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /YX /FD /c
# ADD CPP /nologo /W3 /GX /Ot /Oi /Oy /Ob1 /Gf /Gy /D "STATIC_BUILD" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MT" /D "_WIN32" /D _X86_=1 /D try=__try /D except=__except /YX /FD /I /src/tk-8.0/speedup/ /I /src/tk-8.0/speedup/X11 /I /src/tcl-8.0/win /I /src/tcl-8.0/generic /I /src/tk-8.0/win /I /src/tk-8.0/generic /I /src/tk-8.0/bitmaps /I /src/tk-8.0/win/rc /c
# ADD BASE RSC /l 0x809
# ADD RSC /l 0x809
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LIB32=link.exe -lib
# ADD BASE LIB32 /nologo
# ADD LIB32 /nologo

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "Debug"
# PROP BASE Intermediate_Dir "Debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug"
# PROP Intermediate_Dir "Debug"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /GX /Z7 /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /YX /FD /c
# ADD CPP /nologo /W3 /Gm /GX /ZI /Od /D "STATIC_BUILD" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MT" /D "_WIN32" /D _X86_=1 /D try=__try /D except=__except /YX /FD /I /src/tk-8.0/speedup/ /I /src/tk-8.0/speedup/X11 /I /src/tcl-8.0/win /I /src/tcl-8.0/generic /I /src/tk-8.0/win /I /src/tk-8.0/generic /I /src/tk-8.0/bitmaps /I /src/tk-8.0/win/rc /c
# ADD BASE RSC /l 0x809
# ADD RSC /l 0x809
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LIB32=link.exe -lib
# ADD BASE LIB32 /nologo
# ADD LIB32 /nologo

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "tklib___Win32_Debug_IPv6"
# PROP BASE Intermediate_Dir "tklib___Win32_Debug_IPv6"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug_IPv6"
# PROP Intermediate_Dir "Debug_IPv6"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /Gm /GX /ZI /Od /I "\src\tcl-8.0\generic" /I "\src\tk-8.0\generic" /D "STATIC_BUILD" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MT" /D "_WIN32" /D _X86_=1 /D try=__try /D except=__except /YX /FD /I /src/tcl-8.0/win /I /src/tcl-8.0/generic /I /src/tk-8.0/win /I /src/tk-8.0/generic /I /src/tk-8.0/xlib /I /src/tk-8.0/bitmaps /I /src/tk-8.0/win/rc /c
# ADD CPP /nologo /W3 /Gm /GX /ZI /Od /D "STATIC_BUILD" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MT" /D "_WIN32" /D _X86_=1 /D try=__try /D except=__except /YX /FD /I /src/tk-8.0/speedup/ /I /src/tk-8.0/speedup/X11 /I /src/tcl-8.0/win /I /src/tcl-8.0/generic /I /src/tk-8.0/win /I /src/tk-8.0/generic /I /src/tk-8.0/bitmaps /I /src/tk-8.0/win/rc /c
# ADD BASE RSC /l 0x809
# ADD RSC /l 0x809
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LIB32=link.exe -lib
# ADD BASE LIB32 /nologo
# ADD LIB32 /nologo

!ENDIF 

# Begin Target

# Name "tklib - Win32 Release"
# Name "tklib - Win32 Debug"
# Name "tklib - Win32 Debug IPv6"
# Begin Group "Source Files"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Source File

SOURCE=.\lib_bgerror.c
# End Source File
# Begin Source File

SOURCE=.\lib_button.c
# End Source File
# Begin Source File

SOURCE=.\lib_clrpick.c
# End Source File
# Begin Source File

SOURCE=.\lib_comdlg.c
# End Source File
# Begin Source File

SOURCE=.\lib_console.c
# End Source File
# Begin Source File

SOURCE=.\lib_dialog.c
# End Source File
# Begin Source File

SOURCE=.\lib_entry.c
# End Source File
# Begin Source File

SOURCE=.\lib_focus.c
# End Source File
# Begin Source File

SOURCE=.\lib_listbox.c
# End Source File
# Begin Source File

SOURCE=.\lib_menu.c
# End Source File
# Begin Source File

SOURCE=.\lib_msgbox.c
# End Source File
# Begin Source File

SOURCE=.\lib_obsolete.c
# End Source File
# Begin Source File

SOURCE=.\lib_optMenu.c
# End Source File
# Begin Source File

SOURCE=.\lib_palette.c
# End Source File
# Begin Source File

SOURCE=.\lib_safetk.c
# End Source File
# Begin Source File

SOURCE=.\lib_scale.c
# End Source File
# Begin Source File

SOURCE=.\lib_scrlbar.c
# End Source File
# Begin Source File

SOURCE=.\lib_tearoff.c
# End Source File
# Begin Source File

SOURCE=.\lib_text.c
# End Source File
# Begin Source File

SOURCE=.\lib_tk.c
# End Source File
# Begin Source File

SOURCE=.\lib_tkfbox.c
# End Source File
# Begin Source File

SOURCE=.\lib_xmfbox.c
# End Source File
# Begin Source File

SOURCE=.\stubs.c
# End Source File
# Begin Source File

SOURCE=..\generic\tk3d.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkArgv.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkAtom.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkBind.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkBitmap.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkButton.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvArc.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvas.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvBmap.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvImg.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvLine.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvPoly.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvPs.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvText.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvUtil.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvWind.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkClipboard.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkCmds.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkColor.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkConfig.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkConsole.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkCursor.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkEntry.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkError.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkEvent.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkFileFilter.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkFocus.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkFont.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkFrame.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkGC.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkGeometry.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkGet.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkGrab.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkGrid.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkImage.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkImgBmap.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkImgGIF.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkImgPhoto.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkImgPPM.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkImgUtil.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkListbox.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkMacWinMenu.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkMain.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkMenu.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkMenubutton.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkMenuDraw.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkMessage.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkOption.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkPack.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkPlace.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkPointer.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkRectOval.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkScale.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkScrollbar.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkSelect.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkSquare.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkTest.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkText.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkTextBTree.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkTextDisp.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkTextImage.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkTextIndex.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkTextMark.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkTextTag.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkTextWind.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkTrig.c
# End Source File
# Begin Source File

SOURCE=..\unix\tkUnixMenubu.c
# End Source File
# Begin Source File

SOURCE=..\unix\tkUnixScale.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkUtil.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkVisual.c
# End Source File
# Begin Source File

SOURCE=.\tkWin3d.c
# End Source File
# Begin Source File

SOURCE=.\tkWinButton.c
# End Source File
# Begin Source File

SOURCE=.\tkWinClipboard.c
# End Source File
# Begin Source File

SOURCE=.\tkWinColor.c
# End Source File
# Begin Source File

SOURCE=.\tkWinCursor.c
# End Source File
# Begin Source File

SOURCE=.\tkWinDialog.c
# End Source File
# Begin Source File

SOURCE=..\generic\tkWindow.c
# End Source File
# Begin Source File

SOURCE=.\tkWinDraw.c
# End Source File
# Begin Source File

SOURCE=.\tkWinEmbed.c
# End Source File
# Begin Source File

SOURCE=.\tkWinFont.c
# End Source File
# Begin Source File

SOURCE=.\tkWinGdi.c
# End Source File
# Begin Source File

SOURCE=.\tkWinImage.c
# End Source File
# Begin Source File

SOURCE=.\tkWinInit.c
# End Source File
# Begin Source File

SOURCE=.\tkWinKey.c
# End Source File
# Begin Source File

SOURCE=.\tkWinMenu.c
# End Source File
# Begin Source File

SOURCE=.\tkWinPixmap.c
# End Source File
# Begin Source File

SOURCE=.\tkWinPointer.c
# End Source File
# Begin Source File

SOURCE=.\tkWinRegion.c
# End Source File
# Begin Source File

SOURCE=.\tkWinScrlbr.c
# End Source File
# Begin Source File

SOURCE=.\tkWinSend.c
# End Source File
# Begin Source File

SOURCE=.\tkWinUtil.c
# End Source File
# Begin Source File

SOURCE=.\tkWinWindow.c
# End Source File
# Begin Source File

SOURCE=.\tkWinWm.c
# End Source File
# Begin Source File

SOURCE=.\tkWinX.c
# End Source File
# Begin Source File

SOURCE=.\xcolors.c
# End Source File
# Begin Source File

SOURCE=.\xdraw.c
# End Source File
# Begin Source File

SOURCE=.\xgc.c
# End Source File
# Begin Source File

SOURCE=.\ximage.c
# End Source File
# Begin Source File

SOURCE=.\xutil.c
# End Source File
# End Group
# Begin Group "Header Files"

# PROP Default_Filter "h;hpp;hxx;hm;inl"
# Begin Source File

SOURCE=..\generic\default.h
# End Source File
# Begin Source File

SOURCE=..\generic\ks_names.h
# End Source File
# Begin Source File

SOURCE=..\generic\tk.h
# End Source File
# Begin Source File

SOURCE=..\generic\tk3d.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkButton.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvas.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkColor.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkFileFilter.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkFont.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkInitScript.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkInt.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkMenu.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkMenubutton.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkPort.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkScale.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkScrollbar.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkSelect.h
# End Source File
# Begin Source File

SOURCE=..\generic\tkText.h
# End Source File
# Begin Source File

SOURCE=.\tkWin.h
# End Source File
# Begin Source File

SOURCE=.\tkWinDefault.h
# End Source File
# Begin Source File

SOURCE=.\tkWinInt.h
# End Source File
# Begin Source File

SOURCE=.\tkWinPort.h
# End Source File
# End Group
# Begin Group "Tcl Files"

# PROP Default_Filter ".tcl"
# Begin Source File

SOURCE=..\library\bgerror.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\bgerror.tcl
InputName=bgerror

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\bgerror.tcl
InputName=bgerror

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\bgerror.tcl
InputName=bgerror

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\button.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\button.tcl
InputName=button

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\button.tcl
InputName=button

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\button.tcl
InputName=button

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\clrpick.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\clrpick.tcl
InputName=clrpick

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\clrpick.tcl
InputName=clrpick

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\clrpick.tcl
InputName=clrpick

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\comdlg.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\comdlg.tcl
InputName=comdlg

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\comdlg.tcl
InputName=comdlg

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\comdlg.tcl
InputName=comdlg

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\console.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\console.tcl
InputName=console

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\console.tcl
InputName=console

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\console.tcl
InputName=console

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\dialog.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\dialog.tcl
InputName=dialog

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\dialog.tcl
InputName=dialog

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\dialog.tcl
InputName=dialog

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\entry.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\entry.tcl
InputName=entry

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\entry.tcl
InputName=entry

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\entry.tcl
InputName=entry

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\focus.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\focus.tcl
InputName=focus

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\focus.tcl
InputName=focus

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\focus.tcl
InputName=focus

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\listbox.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\listbox.tcl
InputName=listbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\listbox.tcl
InputName=listbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\listbox.tcl
InputName=listbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\menu.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\menu.tcl
InputName=menu

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\menu.tcl
InputName=menu

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\menu.tcl
InputName=menu

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\msgbox.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\msgbox.tcl
InputName=msgbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\msgbox.tcl
InputName=msgbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\msgbox.tcl
InputName=msgbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\obsolete.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\obsolete.tcl
InputName=obsolete

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\obsolete.tcl
InputName=obsolete

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\obsolete.tcl
InputName=obsolete

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\optMenu.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\optMenu.tcl
InputName=optMenu

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\optMenu.tcl
InputName=optMenu

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\optMenu.tcl
InputName=optMenu

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\palette.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\palette.tcl
InputName=palette

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\palette.tcl
InputName=palette

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\palette.tcl
InputName=palette

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\safetk.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\safetk.tcl
InputName=safetk

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\safetk.tcl
InputName=safetk

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\safetk.tcl
InputName=safetk

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\scale.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\scale.tcl
InputName=scale

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\scale.tcl
InputName=scale

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\scale.tcl
InputName=scale

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\scrlbar.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\scrlbar.tcl
InputName=scrlbar

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\scrlbar.tcl
InputName=scrlbar

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\scrlbar.tcl
InputName=scrlbar

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\tearoff.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\tearoff.tcl
InputName=tearoff

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\tearoff.tcl
InputName=tearoff

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\tearoff.tcl
InputName=tearoff

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\text.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\text.tcl
InputName=text

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\text.tcl
InputName=text

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\text.tcl
InputName=text

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\tk.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\tk.tcl
InputName=tk

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\tk.tcl
InputName=tk

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\tk.tcl
InputName=tk

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\tkfbox.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\tkfbox.tcl
InputName=tkfbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\tkfbox.tcl
InputName=tkfbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\tkfbox.tcl
InputName=tkfbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\xmfbox.tcl

!IF  "$(CFG)" == "tklib - Win32 Release"

# Begin Custom Build
InputPath=..\library\xmfbox.tcl
InputName=xmfbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\xmfbox.tcl
InputName=xmfbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\xmfbox.tcl
InputName=xmfbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	\src\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# End Group
# Begin Group "Resource Files"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\win\rc\tk.rc
# ADD BASE RSC /l 0x809 /i "\src\tk-8.0\win\rc"
# ADD RSC /l 0x809 /i "\src\tk-8.0\win\rc" /i "\src\tk-8.0\generic"
# End Source File
# End Group
# End Target
# End Project

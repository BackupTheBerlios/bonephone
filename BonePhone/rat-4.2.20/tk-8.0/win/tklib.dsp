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
# ADD CPP /nologo /W3 /GX /Ot /Oi /Oy /Ob1 /Gf /Gy /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\tk-8.0\xlib\X11" /D "STATIC_BUILD" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MT" /D "_WIN32" /D _X86_=1 /D try=__try /D except=__except /YX /FD /I /src/tcl-8.0/win /I /src/tcl-8.0/generic /I /src/tk-8.0/win /I /src/tk-8.0/generic /I /src/tk-8.0/xlib /I /src/tk-8.0/bitmaps /I /src/tk-8.0/win/rc /c
# ADD BASE RSC /l 0x809
# ADD RSC /l 0x809 /fo"tk.res"
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
# ADD CPP /nologo /W3 /Gm /GX /ZI /Od /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\tk-8.0\xlib\X11" /D "STATIC_BUILD" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MT" /D "_WIN32" /D _X86_=1 /D try=__try /D except=__except /YX /FD /I /src/tcl-8.0/win /I /src/tcl-8.0/generic /I /src/tk-8.0/win /I /src/tk-8.0/generic /I /src/tk-8.0/xlib /I /src/tk-8.0/bitmaps /I /src/tk-8.0/win/rc /c
# ADD BASE RSC /l 0x809
# ADD RSC /l 0x809 /fo"tk.res"
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
# ADD CPP /nologo /W3 /Gm /GX /ZI /Od /I "\src\tcl-8.0\generic" /I "\src\tk-8.0\generic" /I "\src\tk-8.0\xlib" /I "\src\tk-8.0\xlib\X11" /D "STATIC_BUILD" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MT" /D "_WIN32" /D _X86_=1 /D try=__try /D except=__except /YX /FD /I /src/tcl-8.0/win /I /src/tcl-8.0/generic /I /src/tk-8.0/win /I /src/tk-8.0/generic /I /src/tk-8.0/xlib /I /src/tk-8.0/bitmaps /I /src/tk-8.0/win/rc /c
# ADD BASE RSC /l 0x809
# ADD RSC /l 0x809 /fo"tk.res"
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

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"
# SUBTRACT CPP /I "..\tk-8.0\xlib\X11"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkArgv.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkAtom.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkBind.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkBitmap.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkButton.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "c:/src/tcl-8.0/win" /I "c:/src/tcl-8.0/generic" /I "c:/src/tk-8.0/win" /I "c:/src/tk-8.0/xlib" /I "c:/src/tk-8.0/bitmaps" /I "c:/src/tk-8.0/win/rc" /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvArc.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvas.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvBmap.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvImg.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvLine.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvPoly.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvPs.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvText.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvUtil.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkCanvWind.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkClipboard.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkCmds.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkColor.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkConfig.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkConsole.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkCursor.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkEntry.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkError.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkEvent.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkFileFilter.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkFocus.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkFont.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkFrame.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkGC.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkGeometry.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkGet.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkGrab.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkGrid.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkImage.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkImgBmap.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkImgGIF.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkImgPhoto.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkImgPPM.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkImgUtil.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkListbox.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkMacWinMenu.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkMain.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkMenu.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkMenubutton.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkMenuDraw.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkMessage.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkOption.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkPack.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkPlace.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkPointer.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkRectOval.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkScale.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkScrollbar.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkSelect.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkSquare.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkTest.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkText.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkTextBTree.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkTextDisp.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkTextImage.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkTextIndex.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkTextMark.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkTextTag.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkTextWind.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkTrig.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\unix\tkUnixMenubu.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\unix\tkUnixScale.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkUtil.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkVisual.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWin3d.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinButton.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinClipboard.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinColor.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinCursor.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinDialog.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\generic\tkWindow.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinDraw.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinEmbed.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinFont.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinImage.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinInit.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinKey.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinMenu.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinPixmap.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinPointer.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinRegion.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinScrlbr.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinSend.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinWindow.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinWm.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\tkWinX.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\xlib\xcolors.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\xlib\xdraw.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\xlib\xgc.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\xlib\ximage.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\xlib\xutil.c

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD CPP /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD CPP /I "..\..\tcl-8.0\generic" /I "..\generic" /I "..\xlib" /I "..\xlib\X11" /I "..\win" /I "..\bitmaps"

!ENDIF 

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\bgerror.tcl
InputName=bgerror

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\bgerror.tcl
InputName=bgerror

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\button.tcl
InputName=button

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\button.tcl
InputName=button

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\clrpick.tcl
InputName=clrpick

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\clrpick.tcl
InputName=clrpick

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\comdlg.tcl
InputName=comdlg

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\comdlg.tcl
InputName=comdlg

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\console.tcl
InputName=console

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\console.tcl
InputName=console

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\dialog.tcl
InputName=dialog

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\dialog.tcl
InputName=dialog

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\entry.tcl
InputName=entry

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\entry.tcl
InputName=entry

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\focus.tcl
InputName=focus

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\focus.tcl
InputName=focus

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\listbox.tcl
InputName=listbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\listbox.tcl
InputName=listbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\menu.tcl
InputName=menu

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\menu.tcl
InputName=menu

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\msgbox.tcl
InputName=msgbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\msgbox.tcl
InputName=msgbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\obsolete.tcl
InputName=obsolete

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\obsolete.tcl
InputName=obsolete

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\optMenu.tcl
InputName=optMenu

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\optMenu.tcl
InputName=optMenu

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\palette.tcl
InputName=palette

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\palette.tcl
InputName=palette

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\safetk.tcl
InputName=safetk

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\safetk.tcl
InputName=safetk

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\scale.tcl
InputName=scale

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\scale.tcl
InputName=scale

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\scrlbar.tcl
InputName=scrlbar

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\scrlbar.tcl
InputName=scrlbar

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\tearoff.tcl
InputName=tearoff

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\tearoff.tcl
InputName=tearoff

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\text.tcl
InputName=text

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\text.tcl
InputName=text

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\tk.tcl
InputName=tk

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\tk.tcl
InputName=tk

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\tkfbox.tcl
InputName=tkfbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\tkfbox.tcl
InputName=tkfbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

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
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\xmfbox.tcl
InputName=xmfbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\xmfbox.tcl
InputName=xmfbox

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	..\..\tcl-8.0\win\tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# End Group
# Begin Group "Resource Files"

# PROP Default_Filter ""
# Begin Source File

SOURCE=.\rc\tk.rc

!IF  "$(CFG)" == "tklib - Win32 Release"

# ADD BASE RSC /l 0x409 /i "rc"
# ADD RSC /l 0x809 /i "rc" /i "..\generic" /i "..\Xlib" /i "..\..\tcl-8.0\generic"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug"

# ADD BASE RSC /l 0x409 /i "rc"
# ADD RSC /l 0x409 /i "rc" /i "..\generic" /i "..\Xlib" /i "..\..\tcl-8.0\generic"

!ELSEIF  "$(CFG)" == "tklib - Win32 Debug IPv6"

# ADD BASE RSC /l 0x409 /i "rc" /i "..\generic" /i "..\Xlib" /i "..\..\tcl-8.0\generic"
# ADD RSC /l 0x409 /i "rc" /i "..\generic" /i "..\Xlib" /i "..\..\tcl-8.0\generic"

!ENDIF 

# End Source File
# End Group
# Begin Source File

SOURCE=.\rc\cursor00.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor02.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor04.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor06.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor08.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor0a.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor0c.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor0e.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor10.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor12.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor14.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor16.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor18.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor1a.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor1c.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor1e.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor20.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor22.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor24.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor26.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor28.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor2a.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor2c.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor2e.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor30.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor32.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor34.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor36.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor38.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor3a.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor3c.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor3e.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor40.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor42.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor44.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor46.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor48.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor4a.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor4c.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor4e.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor50.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor52.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor54.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor56.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor58.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor5a.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor5c.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor60.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor62.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor64.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor66.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor68.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor6a.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor6c.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor6e.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor70.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor72.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor74.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor76.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor78.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor7a.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor7c.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor7e.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor80.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor82.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor84.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor86.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor88.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor8a.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor8c.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor8e.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor90.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor92.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor94.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor96.cur
# End Source File
# Begin Source File

SOURCE=.\rc\cursor98.cur
# End Source File
# Begin Source File

SOURCE=.\rc\tk.ico
# End Source File
# End Target
# End Project

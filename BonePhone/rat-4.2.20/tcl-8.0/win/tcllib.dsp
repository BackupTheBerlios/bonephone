# Microsoft Developer Studio Project File - Name="tcllib" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Static Library" 0x0104

CFG=tcllib - Win32 Debug IPv6
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "tcllib.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "tcllib.mak" CFG="tcllib - Win32 Debug IPv6"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "tcllib - Win32 Release" (based on "Win32 (x86) Static Library")
!MESSAGE "tcllib - Win32 Debug" (based on "Win32 (x86) Static Library")
!MESSAGE "tcllib - Win32 Debug IPv6" (based on "Win32 (x86) Static Library")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
RSC=rc.exe

!IF  "$(CFG)" == "tcllib - Win32 Release"

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
# ADD CPP /nologo /W3 /GX /Ot /Oi /Oy /Ob1 /Gf /Gy /I "..\generic" /D "STATIC_BUILD" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MT" /D "_WIN32" /D _X86_=1 /D try=__try /D except=__except /YX /FD /I /src/tcl-8.0/win /I /src/tcl-8.0/generic /I /src/tk-8.0/win /I /src/tk-8.0/generic /I /src/tk-8.0/xlib /I /src/tk-8.0/bitmaps /I /src/tk-8.0/win/rc /c
# SUBTRACT CPP /Ox /Oa /Ow /Og
# ADD BASE RSC /l 0x809
# ADD RSC /l 0x809
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LIB32=link.exe -lib
# ADD BASE LIB32 /nologo
# ADD LIB32 /nologo

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug"

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
# ADD CPP /nologo /W3 /GX /Z7 /Od /I "..\generic" /D "STATIC_BUILD" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MT" /D "_WIN32" /D _X86_=1 /D try=__try /D except=__except /YX /FD /I /src/tcl-8.0/win /I /src/tcl-8.0/generic /I /src/tk-8.0/win /I /src/tk-8.0/generic /I /src/tk-8.0/xlib /I /src/tk-8.0/bitmaps /I /src/tk-8.0/win/rc /c
# ADD BASE RSC /l 0x809
# ADD RSC /l 0x809
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LIB32=link.exe -lib
# ADD BASE LIB32 /nologo
# ADD LIB32 /nologo

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug IPv6"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "tcllib___Win32_Debug_IPv6"
# PROP BASE Intermediate_Dir "tcllib___Win32_Debug_IPv6"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug_IPv6"
# PROP Intermediate_Dir "Debug_IPv6"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /GX /Z7 /Od /D "STATIC_BUILD" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MT" /D "_WIN32" /D _X86_=1 /D try=__try /D except=__except /YX /FD /I /src/tcl-8.0/win /I /src/tcl-8.0/generic /I /src/tk-8.0/win /I /src/tk-8.0/generic /I /src/tk-8.0/xlib /I /src/tk-8.0/bitmaps /I /src/tk-8.0/win/rc /c
# ADD CPP /nologo /W3 /GX /Z7 /Od /I "..\generic" /D "STATIC_BUILD" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MT" /D "_WIN32" /D _X86_=1 /D try=__try /D except=__except /YX /FD /I /src/tcl-8.0/win /I /src/tcl-8.0/generic /I /src/tk-8.0/win /I /src/tk-8.0/generic /I /src/tk-8.0/xlib /I /src/tk-8.0/bitmaps /I /src/tk-8.0/win/rc /c
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

# Name "tcllib - Win32 Release"
# Name "tcllib - Win32 Debug"
# Name "tcllib - Win32 Debug IPv6"
# Begin Group "Source Files"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Source File

SOURCE=.\lib_history.c
# End Source File
# Begin Source File

SOURCE=.\lib_http.c
# End Source File
# Begin Source File

SOURCE=.\lib_init.c
# End Source File
# Begin Source File

SOURCE=.\lib_ldAout.c
# End Source File
# Begin Source File

SOURCE=.\lib_optparse.c
# End Source File
# Begin Source File

SOURCE=.\lib_parray.c
# End Source File
# Begin Source File

SOURCE=.\lib_safe.c
# End Source File
# Begin Source File

SOURCE=.\lib_word.c
# End Source File
# Begin Source File

SOURCE=..\generic\panic.c
# End Source File
# Begin Source File

SOURCE=..\generic\regexp.c
# End Source File
# Begin Source File

SOURCE=..\compat\strftime.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclAlloc.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclAsync.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclBasic.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclBinary.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclCkalloc.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclClock.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclCmdAH.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclCmdIL.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclCmdMZ.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclCompExpr.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclCompile.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclDate.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclEnv.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclEvent.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclExecute.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclFCmd.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclFileName.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclGet.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclHash.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclHistory.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclIndexObj.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclInterp.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclIO.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclIOCmd.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclIOSock.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclIOUtil.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclLink.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclListObj.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclLoad.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclMain.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclNamesp.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclNotify.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclObj.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclParse.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclPipe.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclPkg.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclPosixStr.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclPreserve.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclProc.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclResolve.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclStringObj.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclTest.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclTestObj.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclTimer.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclUtil.c
# End Source File
# Begin Source File

SOURCE=..\generic\tclVar.c
# End Source File
# Begin Source File

SOURCE=.\tclWin32Dll.c
# End Source File
# Begin Source File

SOURCE=.\tclWinChan.c
# End Source File
# Begin Source File

SOURCE=.\tclWinError.c
# End Source File
# Begin Source File

SOURCE=.\tclWinFCmd.c
# End Source File
# Begin Source File

SOURCE=.\tclWinFile.c
# End Source File
# Begin Source File

SOURCE=.\tclWinInit.c
# End Source File
# Begin Source File

SOURCE=.\tclWinLoad.c
# End Source File
# Begin Source File

SOURCE=.\tclWinMtherr.c
# End Source File
# Begin Source File

SOURCE=.\tclWinNotify.c
# End Source File
# Begin Source File

SOURCE=.\tclWinPipe.c
# End Source File
# Begin Source File

SOURCE=.\tclWinReg.c
# End Source File
# Begin Source File

SOURCE=.\tclWinSock.c
# End Source File
# Begin Source File

SOURCE=.\tclWinTest.c
# End Source File
# Begin Source File

SOURCE=.\tclWinTime.c
# End Source File
# End Group
# Begin Group "Header Files"

# PROP Default_Filter "h;hpp;hxx;hm;inl"
# Begin Source File

SOURCE=..\generic\tcl.h
# End Source File
# End Group
# Begin Group "Tcl Files"

# PROP Default_Filter ".tcl"
# Begin Source File

SOURCE=..\library\history.tcl

!IF  "$(CFG)" == "tcllib - Win32 Release"

# Begin Custom Build
InputPath=..\library\history.tcl
InputName=history

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\history.tcl
InputName=history

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\history.tcl
InputName=history

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\http2.0\http.tcl

!IF  "$(CFG)" == "tcllib - Win32 Release"

# Begin Custom Build
InputPath=..\library\http2.0\http.tcl
InputName=http

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\http2.0\http.tcl
InputName=http

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\http2.0\http.tcl
InputName=http

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\init.tcl

!IF  "$(CFG)" == "tcllib - Win32 Release"

# Begin Custom Build
InputPath=..\library\init.tcl
InputName=init

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\init.tcl
InputName=init

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\init.tcl
InputName=init

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\ldAout.tcl

!IF  "$(CFG)" == "tcllib - Win32 Release"

# Begin Custom Build
InputPath=..\library\ldAout.tcl
InputName=ldAout

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\ldAout.tcl
InputName=ldAout

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\ldAout.tcl
InputName=ldAout

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\opt0.1\optparse.tcl

!IF  "$(CFG)" == "tcllib - Win32 Release"

# Begin Custom Build
InputPath=..\library\opt0.1\optparse.tcl
InputName=optparse

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\opt0.1\optparse.tcl
InputName=optparse

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\opt0.1\optparse.tcl
InputName=optparse

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\parray.tcl

!IF  "$(CFG)" == "tcllib - Win32 Release"

# Begin Custom Build
InputPath=..\library\parray.tcl
InputName=parray

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\parray.tcl
InputName=parray

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\parray.tcl
InputName=parray

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\safe.tcl

!IF  "$(CFG)" == "tcllib - Win32 Release"

# Begin Custom Build
InputPath=..\library\safe.tcl
InputName=safe

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\safe.tcl
InputName=safe

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\safe.tcl
InputName=safe

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\library\word.tcl

!IF  "$(CFG)" == "tcllib - Win32 Release"

# Begin Custom Build
InputPath=..\library\word.tcl
InputName=word

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug"

# Begin Custom Build
InputPath=..\library\word.tcl
InputName=word

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ELSEIF  "$(CFG)" == "tcllib - Win32 Debug IPv6"

# Begin Custom Build
InputPath=..\library\word.tcl
InputName=word

"lib_$(InputName).c" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	tcl2c\tcl2c.exe lib_$(InputName) < $(InputPath) > lib_$(InputName).c

# End Custom Build

!ENDIF 

# End Source File
# End Group
# End Target
# End Project

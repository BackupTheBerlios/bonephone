# Microsoft Developer Studio Project File - Name="common" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Static Library" 0x0104

CFG=common - Win32 Debug IPv6 MSR
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "common.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "common.mak" CFG="common - Win32 Debug IPv6 MSR"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "common - Win32 Release" (based on "Win32 (x86) Static Library")
!MESSAGE "common - Win32 Debug IPv6 Musica" (based on "Win32 (x86) Static Library")
!MESSAGE "common - Win32 Debug" (based on "Win32 (x86) Static Library")
!MESSAGE "common - Win32 Debug IPv6 Win2000" (based on "Win32 (x86) Static Library")
!MESSAGE "common - Win32 Debug IPv6 MSR" (based on "Win32 (x86) Static Library")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
RSC=rc.exe

!IF  "$(CFG)" == "common - Win32 Release"

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
# ADD BASE CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_MBCS" /D "_LIB" /YX /FD /c
# ADD CPP /nologo /W3 /GX /O2 /I "..\tcl-8.0\generic" /I "..\tk-8.0\generic" /I "..\tk-8.0\xlib" /D "WIN32" /D "NDEBUG" /D "_MBCS" /D "_LIB" /D "HAVE_INET_NTOP" /YX /FD /c
# ADD BASE RSC /l 0x409 /d "NDEBUG"
# ADD RSC /l 0x409 /d "NDEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LIB32=link.exe -lib
# ADD BASE LIB32 /nologo
# ADD LIB32 /nologo /out:"Release\uclmm.lib"

!ELSEIF  "$(CFG)" == "common - Win32 Debug IPv6 Musica"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "common___Win32_Debug_IPv6_Musica"
# PROP BASE Intermediate_Dir "common___Win32_Debug_IPv6_Musica"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug_IPv6_Musica"
# PROP Intermediate_Dir "Debug_IPv6_Musica"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /Gm /GX /Zi /Od /I "\src\tcl-8.0\generic" /I "\src\tk-8.0\generic" /I "\src\tk-8.0\xlib" /I "\DDK\inc" /I "\src\MSR_IPv6_1.3\inc" /D "WIN32" /D "_DEBUG" /D "_MBCS" /D "_LIB" /D "DEBUG" /D "DEBUG_MEM" /D "HAVE_IPv6" /D "BUILD_tcl" /D "BUILD_tk" /FR /YX /FD /GZ /c
# ADD CPP /nologo /W3 /Gm /GX /Zi /Od /I "..\tcl-8.0\generic" /I "..\tk-8.0\generic" /I "..\tk-8.0\xlib" /I "..\MUSICA\WINSOCK6" /D "_MBCS" /D "_LIB" /D "DEBUG_MEM" /D "BUILD_tcl" /D "BUILD_tk" /D "DEBUG" /D "WIN32" /D "_DEBUG" /D "HAVE_IPv6" /D "MUSICA_IPV6" /D "_WINNT" /D "_POSIX" /D "NEED_ADDRINFO_H" /D "NEED_IN_EXPERIMENTAL" /D "NEED_IN6_IS_ADDR_V4MAPPED" /FR /YX /FD /GZ /c
# ADD BASE RSC /l 0x409 /d "_DEBUG"
# ADD RSC /l 0x409 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LIB32=link.exe -lib
# ADD BASE LIB32 /nologo /out:"Debug\uclmm.lib"
# ADD LIB32 /nologo /out:"Debug_IPv6\uclmm.lib"

!ELSEIF  "$(CFG)" == "common - Win32 Debug"

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
# ADD BASE CPP /nologo /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_MBCS" /D "_LIB" /YX /FD /GZ /c
# ADD CPP /nologo /W3 /Gm /GX /ZI /Od /I "..\tcl-8.0\generic" /I "..\tk-8.0\generic" /I "..\tk-8.0\xlib" /D "WIN32" /D "_DEBUG" /D "_MBCS" /D "_LIB" /D "DEBUG" /D "DEBUG_MEM" /FR /YX /FD /GZ /c
# ADD BASE RSC /l 0x409 /d "_DEBUG"
# ADD RSC /l 0x409 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LIB32=link.exe -lib
# ADD BASE LIB32 /nologo
# ADD LIB32 /nologo /out:"Debug\uclmm.lib"

!ELSEIF  "$(CFG)" == "common - Win32 Debug IPv6 Win2000"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "common___Win32_Debug_IPv6_Win2000"
# PROP BASE Intermediate_Dir "common___Win32_Debug_IPv6_Win2000"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug_IPv6_Win2000"
# PROP Intermediate_Dir "Debug_IPv6_Win2000"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /Gm /GX /Zi /Od /I "\src\tcl-8.0\generic" /I "\src\tk-8.0\generic" /I "\src\tk-8.0\xlib" /I "\DDK\inc" /I "\src\IPv6Kit\inc" /I "..\tcl-8.0\generic" /I "..\tk-8.0\generic" /I "..\tk-8.0\xlib" /I "..\ipv6kit\inc" /D "WIN32" /D "_DEBUG" /D "_MBCS" /D "_LIB" /D "DEBUG" /D "DEBUG_MEM" /D "HAVE_IPv6" /D "BUILD_tcl" /D "BUILD_tk" /D "NEED_IN6_IS_ADDR_MULTICAST" /D "HAVE_INET_PTON" /D "HAVE_INET_NTOP" /FR /YX /FD /GZ /GZ /c
# ADD CPP /nologo /W3 /Gm /GX /Zi /Od /I "\src\tcl-8.0\generic" /I "\src\tk-8.0\generic" /I "\src\tk-8.0\xlib" /I "\DDK\inc" /I "\src\IPv6Kit\inc" /I "..\tcl-8.0\generic" /I "..\tk-8.0\generic" /I "..\tk-8.0\xlib" /I "..\ipv6kit\inc" /D "WIN2K_IPV6" /D "WIN32" /D "_DEBUG" /D "_MBCS" /D "_LIB" /D "DEBUG" /D "DEBUG_MEM" /D "HAVE_IPv6" /D "BUILD_tcl" /D "BUILD_tk" /D "NEED_IN6_IS_ADDR_MULTICAST" /D "NEED_INET_PTON" /D "NEED_INET_NTOP" /FR /YX /FD /GZ /GZ /c
# ADD BASE RSC /l 0x409 /d "_DEBUG"
# ADD RSC /l 0x409 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LIB32=link.exe -lib
# ADD BASE LIB32 /nologo /out:"Debug_IPv6\uclmm.lib"
# ADD LIB32 /nologo /out:"Debug_IPv6\uclmm.lib"

!ELSEIF  "$(CFG)" == "common - Win32 Debug IPv6 MSR"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "common___Win32_Debug_IPv6_MSR"
# PROP BASE Intermediate_Dir "common___Win32_Debug_IPv6_MSR"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug_IPv6_MSR"
# PROP Intermediate_Dir "Debug_IPv6_MSR"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /Gm /GX /Zi /Od /I "\src\tcl-8.0\generic" /I "\src\tk-8.0\generic" /I "\src\tk-8.0\xlib" /I "\DDK\inc" /I "\src\IPv6Kit\inc" /I "..\tcl-8.0\generic" /I "..\tk-8.0\generic" /I "..\tk-8.0\xlib" /I "..\ipv6kit\inc" /D "WIN32" /D "_DEBUG" /D "_MBCS" /D "_LIB" /D "DEBUG" /D "DEBUG_MEM" /D "HAVE_IPv6" /D "BUILD_tcl" /D "BUILD_tk" /D "NEED_IN6_IS_ADDR_MULTICAST" /D "HAVE_INET_PTON" /D "HAVE_INET_NTOP" /FR /YX /FD /GZ /GZ /c
# ADD CPP /nologo /W3 /Gm /GX /Zi /Od /I "\src\tcl-8.0\generic" /I "\src\tk-8.0\generic" /I "\src\tk-8.0\xlib" /I "\DDK\inc" /I "\src\IPv6Kit\inc" /I "..\tcl-8.0\generic" /I "..\tk-8.0\generic" /I "..\tk-8.0\xlib" /I "d:\ipv6kit\inc" /D "WIN32" /D "_DEBUG" /D "_MBCS" /D "_LIB" /D "DEBUG" /D "DEBUG_MEM" /D "HAVE_IPv6" /D "BUILD_tcl" /D "BUILD_tk" /D "NEED_IN6_IS_ADDR_MULTICAST" /D "HAVE_INET_PTON" /D "HAVE_INET_NTOP" /FR /YX /FD /GZ /GZ /c
# ADD BASE RSC /l 0x409 /d "_DEBUG"
# ADD RSC /l 0x409 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LIB32=link.exe -lib
# ADD BASE LIB32 /nologo /out:"Debug_IPv6\uclmm.lib"
# ADD LIB32 /nologo /out:"Debug_IPv6\uclmm.lib"

!ENDIF 

# Begin Target

# Name "common - Win32 Release"
# Name "common - Win32 Debug IPv6 Musica"
# Name "common - Win32 Debug"
# Name "common - Win32 Debug IPv6 Win2000"
# Name "common - Win32 Debug IPv6 MSR"
# Begin Group "Source Files"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Source File

SOURCE=.\base64.c
# End Source File
# Begin Source File

SOURCE=.\btree.c
# End Source File
# Begin Source File

SOURCE=.\crypt_random.c
# End Source File
# Begin Source File

SOURCE=.\debug.c
# End Source File
# Begin Source File

SOURCE=.\drand48.c
# End Source File
# Begin Source File

SOURCE=.\getaddrinfo.c

!IF  "$(CFG)" == "common - Win32 Release"

# PROP Exclude_From_Build 1

!ELSEIF  "$(CFG)" == "common - Win32 Debug IPv6 Musica"

# ADD CPP /D "INET6" /D "HAVE_GETHOSTBYNAME2"

!ELSEIF  "$(CFG)" == "common - Win32 Debug"

# PROP Exclude_From_Build 1

!ELSEIF  "$(CFG)" == "common - Win32 Debug IPv6 Win2000"

# PROP BASE Exclude_From_Build 1
# PROP Exclude_From_Build 1

!ELSEIF  "$(CFG)" == "common - Win32 Debug IPv6 MSR"

# PROP BASE Exclude_From_Build 1
# PROP Exclude_From_Build 1

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\gettimeofday.c

!IF  "$(CFG)" == "common - Win32 Release"

!ELSEIF  "$(CFG)" == "common - Win32 Debug IPv6 Musica"

# ADD CPP /W4

!ELSEIF  "$(CFG)" == "common - Win32 Debug"

!ELSEIF  "$(CFG)" == "common - Win32 Debug IPv6 Win2000"

!ELSEIF  "$(CFG)" == "common - Win32 Debug IPv6 MSR"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\hmac.c
# End Source File
# Begin Source File

SOURCE=.\inet_ntop.c
# End Source File
# Begin Source File

SOURCE=.\inet_pton.c
# End Source File
# Begin Source File

SOURCE=.\mbus.c
# End Source File
# Begin Source File

SOURCE=.\mbus_addr.c
# End Source File
# Begin Source File

SOURCE=.\mbus_config.c
# End Source File
# Begin Source File

SOURCE=.\mbus_parser.c
# End Source File
# Begin Source File

SOURCE=.\md5.c
# End Source File
# Begin Source File

SOURCE=.\memory.c
# End Source File
# Begin Source File

SOURCE=.\net_udp.c
# End Source File
# Begin Source File

SOURCE=.\ntp.c
# End Source File
# Begin Source File

SOURCE=.\qfDES.c
# End Source File
# Begin Source File

SOURCE=".\rijndael-alg-fst.c"
# End Source File
# Begin Source File

SOURCE=".\rijndael-api-fst.c"
# End Source File
# Begin Source File

SOURCE=.\rtp.c
# End Source File
# Begin Source File

SOURCE=.\util.c
# End Source File
# End Group
# Begin Group "Header Files"

# PROP Default_Filter "h;hpp;hxx;hm;inl"
# Begin Source File

SOURCE=.\base64.h
# End Source File
# Begin Source File

SOURCE=.\btree.h
# End Source File
# Begin Source File

SOURCE=.\config_unix.h
# End Source File
# Begin Source File

SOURCE=.\config_win32.h
# End Source File
# Begin Source File

SOURCE=.\crypt_random.h
# End Source File
# Begin Source File

SOURCE=.\debug.h
# End Source File
# Begin Source File

SOURCE=.\drand48.h
# End Source File
# Begin Source File

SOURCE=.\gettimeofday.h
# End Source File
# Begin Source File

SOURCE=.\hmac.h
# End Source File
# Begin Source File

SOURCE=.\inet_ntop.h
# End Source File
# Begin Source File

SOURCE=.\inet_pton.h
# End Source File
# Begin Source File

SOURCE=.\mbus.h
# End Source File
# Begin Source File

SOURCE=.\mbus_addr.h
# End Source File
# Begin Source File

SOURCE=.\mbus_config.h
# End Source File
# Begin Source File

SOURCE=.\mbus_parser.h
# End Source File
# Begin Source File

SOURCE=.\md5.h
# End Source File
# Begin Source File

SOURCE=.\memory.h
# End Source File
# Begin Source File

SOURCE=.\net_udp.h
# End Source File
# Begin Source File

SOURCE=.\ntp.h
# End Source File
# Begin Source File

SOURCE=.\qfDES.h
# End Source File
# Begin Source File

SOURCE=.\rtp.h
# End Source File
# Begin Source File

SOURCE=.\util.h
# End Source File
# Begin Source File

SOURCE=.\version.h
# End Source File
# End Group
# Begin Source File

SOURCE=..\VERSION

!IF  "$(CFG)" == "common - Win32 Release"

!ELSEIF  "$(CFG)" == "common - Win32 Debug IPv6 Musica"

!ELSEIF  "$(CFG)" == "common - Win32 Debug"

# Begin Custom Build
InputPath=..\VERSION

"version.h" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	copy win32\set.txt + ..\VERSION win32\vergen.bat 
	copy win32\vergen.bat + win32\null.txt win32\vergen.bat 
	copy win32\vergen.bat + win32\echo.txt win32\vergen.bat 
	win32\vergen.bat 
	move win32\version.h version.h 
	erase win32\version.h 
	erase win32\vergen.bat 
	
# End Custom Build

!ELSEIF  "$(CFG)" == "common - Win32 Debug IPv6 Win2000"

# Begin Custom Build
InputPath=..\VERSION

"version.h" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	copy win32\set.txt + ..\VERSION win32\vergen.bat 
	copy win32\vergen.bat + win32\null.txt win32\vergen.bat 
	copy win32\vergen.bat + win32\echo.txt win32\vergen.bat 
	win32\vergen.bat 
	move win32\version.h version.h 
	erase win32\version.h 
	erase win32\vergen.bat 
	
# End Custom Build

!ELSEIF  "$(CFG)" == "common - Win32 Debug IPv6 MSR"

# Begin Custom Build
InputPath=..\VERSION

"version.h" : $(SOURCE) "$(INTDIR)" "$(OUTDIR)"
	copy win32\set.txt + ..\VERSION win32\vergen.bat 
	copy win32\vergen.bat + win32\null.txt win32\vergen.bat 
	copy win32\vergen.bat + win32\echo.txt win32\vergen.bat 
	win32\vergen.bat 
	move win32\version.h version.h 
	erase win32\version.h 
	erase win32\vergen.bat 
	
# End Custom Build

!ENDIF 

# End Source File
# End Target
# End Project

/* This file is automatically generated by tcl2c, do NOT edit by hand! */
char lib_ldAout[] = "\
proc tclLdAout {{cc {}} {shlib_suffix {}} {shlib_cflags none}} {\n\
global env\n\
global argv\n\
if {$cc==\"\"} {\n\
set cc $env(CC)\n\
}\n\
if {$shlib_suffix==\"\"} {\n\
set shlib_cflags $env(SHLIB_CFLAGS)\n\
} else {\n\
if {$shlib_cflags==\"none\"} {\n\
set shlib_cflags $shlib_suffix\n\
}\n\
}\n\
set seenDotO 0\n\
set minusO 0\n\
set head {}\n\
set tail {}\n\
set nmCommand {|nm -g}\n\
set entryProtos {}\n\
set entryPoints {}\n\
set libraries {}\n\
set libdirs {}\n\
foreach a $argv {\n\
if {!$minusO && [regexp {\\.[ao]$} $a]} {\n\
set seenDotO 1\n\
lappend nmCommand $a\n\
}\n\
if {$minusO} {\n\
set outputFile $a\n\
set minusO 0\n\
} elseif {![string compare $a -o]} {\n\
set minusO 1\n\
}\n\
if [regexp {^-[lL]} $a] {\n\
lappend libraries $a\n\
if [regexp {^-L} $a] {\n\
lappend libdirs [string range $a 2 end]\n\
}\n\
} elseif {$seenDotO} {\n\
lappend tail $a\n\
} else {\n\
lappend head $a\n\
}\n\
}\n\
lappend libdirs /lib /usr/lib\n\
set libs {}\n\
foreach lib $libraries {\n\
if [regexp {^-l} $lib] {\n\
set lname [string range $lib 2 end]\n\
foreach dir $libdirs {\n\
if [file exists [file join $dir lib${lname}_G0.a]] {\n\
set lname ${lname}_G0\n\
break\n\
}\n\
}\n\
lappend libs -l$lname\n\
} else {\n\
lappend libs $lib\n\
}\n\
}\n\
set libraries $libs\n\
if {![info exists outputFile]} {\n\
error \"-o option must be supplied to link a Tcl load module\"\n\
}\n\
set m [file tail $outputFile]\n\
if [regexp {\\.a$} $outputFile] {\n\
set shlib_suffix .a\n\
} else {\n\
set shlib_suffix \"\"\n\
}\n\
if [regexp {\\..*$} $outputFile match] {\n\
set l [expr [string length $m] - [string length $match]]\n\
} else {\n\
error \"Output file does not appear to have a suffix\"\n\
}\n\
set modName [string tolower [string range $m 0 [expr $l-1]]]\n\
if [regexp {^lib} $modName] {\n\
set modName [string range $modName 3 end]\n\
}\n\
if [regexp {[0-9\\.]*(_g0)?$} $modName match] {\n\
set modName [string range $modName 0 [expr [string length $modName]-[string length $match]-1]]\n\
}\n\
set modName \"[string toupper [string index $modName 0]][string range $modName 1 end]\"\n\
set f [open $nmCommand r]\n\
while {[gets $f l] >= 0} {\n\
if [regexp {T[ 	]*_?([A-Z][a-z0-9_]*_(Safe)?Init(__FP10Tcl_Interp)?)$} $l trash symbol] {\n\
if {![regexp {_?([A-Z][a-z0-9_]*_(Safe)?Init)} $symbol trash s]} {\n\
set s $symbol\n\
}\n\
append entryProtos {extern int } $symbol { (); } \\n\n\
append entryPoints {  } \\{ { \"} $s {\", } $symbol { } \\} , \\n\n\
}\n\
}\n\
close $f\n\
if {$entryPoints==\"\"} {\n\
error \"No entry point found in objects\"\n\
}\n\
set C {#include <string.h>}\n\
append C \\n\n\
append C {char TclLoadLibraries_} $modName { [] =} \\n\n\
append C {  \"@LIBS: } $libraries {\";} \\n\n\
append C $entryProtos\n\
append C {static struct } \\{ \\n\n\
append C {  char * name;} \\n\n\
append C {  int (*value)();} \\n\n\
append C \\} {dictionary [] = } \\{ \\n\n\
append C $entryPoints\n\
append C {  0, 0 } \\n \\} \\; \\n\n\
append C {typedef struct Tcl_Interp Tcl_Interp;} \\n\n\
append C {typedef int Tcl_PackageInitProc (Tcl_Interp *);} \\n\n\
append C {Tcl_PackageInitProc *} \\n\n\
append C TclLoadDictionary_ $modName { (symbol)} \\n\n\
append C {    char * symbol;} \\n\n\
append C {{\n\
int i;\n\
for (i = 0; dictionary [i] . name != 0; ++i) {\n\
if (!strcmp (symbol, dictionary [i] . name)) {\n\
return dictionary [i].value;\n\
}\n\
}\n\
return 0;\n\
}} \\n\n\
set cFile tcl$modName.c\n\
set f [open $cFile w]\n\
puts -nonewline $f $C\n\
close $f\n\
set ccCommand \"$cc -c $shlib_cflags $cFile\"\n\
puts stderr $ccCommand\n\
eval exec $ccCommand\n\
if {$shlib_suffix == \".a\"} {\n\
set ldCommand \"ar cr $outputFile\"\n\
regsub { -o} $tail {} tail\n\
} else {\n\
set ldCommand ld\n\
foreach item $head {\n\
lappend ldCommand $item\n\
}\n\
}\n\
lappend ldCommand tcl$modName.o\n\
foreach item $tail {\n\
lappend ldCommand $item\n\
}\n\
puts stderr $ldCommand\n\
eval exec $ldCommand\n\
if {$shlib_suffix == \".a\"} {\n\
exec ranlib $outputFile\n\
}\n\
exec /bin/rm $cFile [file rootname $cFile].o\n\
}\n\
";

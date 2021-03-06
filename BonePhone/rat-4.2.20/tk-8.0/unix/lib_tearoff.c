/* This file is automatically generated by tcl2c, do NOT edit by hand! */
char lib_tearoff[] = "\
proc tkTearOffMenu {w {x 0} {y 0}} {\n\
if {$x == 0} {\n\
set x [winfo rootx $w]\n\
}\n\
if {$y == 0} {\n\
set y [winfo rooty $w]\n\
}\n\
set parent [winfo parent $w]\n\
while {([winfo toplevel $parent] != $parent)\n\
|| ([winfo class $parent] == \"Menu\")} {\n\
set parent [winfo parent $parent]\n\
}\n\
if {$parent == \".\"} {\n\
set parent \"\"\n\
}\n\
for {set i 1} 1 {incr i} {\n\
set menu $parent.tearoff$i\n\
if {![winfo exists $menu]} {\n\
break\n\
}\n\
}\n\
$w clone $menu tearoff\n\
set parent [winfo parent $w]\n\
if {[$menu cget -title] != \"\"} {\n\
wm title $menu [$menu cget -title]\n\
} else {\n\
switch [winfo class $parent] {\n\
Menubutton {\n\
wm title $menu [$parent cget -text]\n\
}\n\
Menu {\n\
wm title $menu [$parent entrycget active -label]\n\
}\n\
}\n\
}\n\
$menu post $x $y\n\
if {[winfo exists $menu] == 0} {\n\
return \"\"\n\
}\n\
bind $menu <Enter> {\n\
set tkPriv(focus) %W\n\
}\n\
set cmd [$w cget -tearoffcommand]\n\
if {$cmd != \"\"} {\n\
uplevel #0 $cmd $w $menu\n\
}\n\
return $menu\n\
}\n\
proc tkMenuDup {src dst type} {\n\
set cmd [list menu $dst -type $type]\n\
foreach option [$src configure] {\n\
if {[llength $option] == 2} {\n\
continue\n\
}\n\
if {[string compare [lindex $option 0] \"-type\"] == 0} {\n\
continue\n\
}\n\
lappend cmd [lindex $option 0] [lindex $option 4]\n\
}\n\
eval $cmd\n\
set last [$src index last]\n\
if {$last == \"none\"} {\n\
return\n\
}\n\
for {set i [$src cget -tearoff]} {$i <= $last} {incr i} {\n\
set cmd [list $dst add [$src type $i]]\n\
foreach option [$src entryconfigure $i]  {\n\
lappend cmd [lindex $option 0] [lindex $option 4]\n\
}\n\
eval $cmd\n\
}\n\
regsub -all . $src {\\\\&} quotedSrc\n\
regsub -all . $dst {\\\\&} quotedDst\n\
regsub -all $quotedSrc [bindtags $src] $dst x\n\
bindtags $dst $x\n\
foreach event [bind $src] {\n\
regsub -all $quotedSrc [bind $src $event] $dst x\n\
bind $dst $event $x\n\
}\n\
}\n\
";

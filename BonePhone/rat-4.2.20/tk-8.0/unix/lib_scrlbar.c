/* This file is automatically generated by tcl2c, do NOT edit by hand! */
char lib_scrlbar[] = "\
if {($tcl_platform(platform) != \"windows\") &&\n\
($tcl_platform(platform) != \"macintosh\")} {\n\
bind Scrollbar <Enter> {\n\
if {$tk_strictMotif} {\n\
set tkPriv(activeBg) [%W cget -activebackground]\n\
%W config -activebackground [%W cget -background]\n\
}\n\
%W activate [%W identify %x %y]\n\
}\n\
bind Scrollbar <Motion> {\n\
%W activate [%W identify %x %y]\n\
}\n\
bind Scrollbar <Leave> {\n\
if {$tk_strictMotif && [info exists tkPriv(activeBg)]} {\n\
%W config -activebackground $tkPriv(activeBg)\n\
}\n\
%W activate {}\n\
}\n\
bind Scrollbar <1> {\n\
tkScrollButtonDown %W %x %y\n\
}\n\
bind Scrollbar <B1-Motion> {\n\
tkScrollDrag %W %x %y\n\
}\n\
bind Scrollbar <B1-B2-Motion> {\n\
tkScrollDrag %W %x %y\n\
}\n\
bind Scrollbar <ButtonRelease-1> {\n\
tkScrollButtonUp %W %x %y\n\
}\n\
bind Scrollbar <B1-Leave> {\n\
}\n\
bind Scrollbar <B1-Enter> {\n\
}\n\
bind Scrollbar <2> {\n\
tkScrollButton2Down %W %x %y\n\
}\n\
bind Scrollbar <B1-2> {\n\
}\n\
bind Scrollbar <B2-1> {\n\
}\n\
bind Scrollbar <B2-Motion> {\n\
tkScrollDrag %W %x %y\n\
}\n\
bind Scrollbar <ButtonRelease-2> {\n\
tkScrollButtonUp %W %x %y\n\
}\n\
bind Scrollbar <B1-ButtonRelease-2> {\n\
}\n\
bind Scrollbar <B2-ButtonRelease-1> {\n\
}\n\
bind Scrollbar <B2-Leave> {\n\
}\n\
bind Scrollbar <B2-Enter> {\n\
}\n\
bind Scrollbar <Control-1> {\n\
tkScrollTopBottom %W %x %y\n\
}\n\
bind Scrollbar <Control-2> {\n\
tkScrollTopBottom %W %x %y\n\
}\n\
bind Scrollbar <Up> {\n\
tkScrollByUnits %W v -1\n\
}\n\
bind Scrollbar <Down> {\n\
tkScrollByUnits %W v 1\n\
}\n\
bind Scrollbar <Control-Up> {\n\
tkScrollByPages %W v -1\n\
}\n\
bind Scrollbar <Control-Down> {\n\
tkScrollByPages %W v 1\n\
}\n\
bind Scrollbar <Left> {\n\
tkScrollByUnits %W h -1\n\
}\n\
bind Scrollbar <Right> {\n\
tkScrollByUnits %W h 1\n\
}\n\
bind Scrollbar <Control-Left> {\n\
tkScrollByPages %W h -1\n\
}\n\
bind Scrollbar <Control-Right> {\n\
tkScrollByPages %W h 1\n\
}\n\
bind Scrollbar <Prior> {\n\
tkScrollByPages %W hv -1\n\
}\n\
bind Scrollbar <Next> {\n\
tkScrollByPages %W hv 1\n\
}\n\
bind Scrollbar <Home> {\n\
tkScrollToPos %W 0\n\
}\n\
bind Scrollbar <End> {\n\
tkScrollToPos %W 1\n\
}\n\
}\n\
proc tkScrollButtonDown {w x y} {\n\
global tkPriv\n\
set tkPriv(relief) [$w cget -activerelief]\n\
$w configure -activerelief sunken\n\
set element [$w identify $x $y]\n\
if {$element == \"slider\"} {\n\
tkScrollStartDrag $w $x $y\n\
} else {\n\
tkScrollSelect $w $element initial\n\
}\n\
}\n\
proc tkScrollButtonUp {w x y} {\n\
global tkPriv\n\
tkCancelRepeat\n\
$w configure -activerelief $tkPriv(relief)\n\
tkScrollEndDrag $w $x $y\n\
$w activate [$w identify $x $y]\n\
}\n\
proc tkScrollSelect {w element repeat} {\n\
global tkPriv\n\
if {![winfo exists $w]} return\n\
if {$element == \"arrow1\"} {\n\
tkScrollByUnits $w hv -1\n\
} elseif {$element == \"trough1\"} {\n\
tkScrollByPages $w hv -1\n\
} elseif {$element == \"trough2\"} {\n\
tkScrollByPages $w hv 1\n\
} elseif {$element == \"arrow2\"} {\n\
tkScrollByUnits $w hv 1\n\
} else {\n\
return\n\
}\n\
if {$repeat == \"again\"} {\n\
set tkPriv(afterId) [after [$w cget -repeatinterval] \\\n\
tkScrollSelect $w $element again]\n\
} elseif {$repeat == \"initial\"} {\n\
set delay [$w cget -repeatdelay]\n\
if {$delay > 0} {\n\
set tkPriv(afterId) [after $delay tkScrollSelect $w $element again]\n\
}\n\
}\n\
}\n\
proc tkScrollStartDrag {w x y} {\n\
global tkPriv\n\
if {[$w cget -command] == \"\"} {\n\
return\n\
}\n\
set tkPriv(pressX) $x\n\
set tkPriv(pressY) $y\n\
set tkPriv(initValues) [$w get]\n\
set iv0 [lindex $tkPriv(initValues) 0]\n\
if {[llength $tkPriv(initValues)] == 2} {\n\
set tkPriv(initPos) $iv0\n\
} else {\n\
if {$iv0 == 0} {\n\
set tkPriv(initPos) 0.0\n\
} else {\n\
set tkPriv(initPos) [expr {(double([lindex $tkPriv(initValues) 2])) \\\n\
/ [lindex $tkPriv(initValues) 0]}]\n\
}\n\
}\n\
}\n\
proc tkScrollDrag {w x y} {\n\
global tkPriv\n\
if {$tkPriv(initPos) == \"\"} {\n\
return\n\
}\n\
set delta [$w delta [expr {$x - $tkPriv(pressX)}] [expr {$y - $tkPriv(pressY)}]]\n\
if {[$w cget -jump]} {\n\
if {[llength $tkPriv(initValues)] == 2} {\n\
$w set [expr {[lindex $tkPriv(initValues) 0] + $delta}] \\\n\
[expr {[lindex $tkPriv(initValues) 1] + $delta}]\n\
} else {\n\
set delta [expr {round($delta * [lindex $tkPriv(initValues) 0])}]\n\
eval $w set [lreplace $tkPriv(initValues) 2 3 \\\n\
[expr {[lindex $tkPriv(initValues) 2] + $delta}] \\\n\
[expr {[lindex $tkPriv(initValues) 3] + $delta}]]\n\
}\n\
} else {\n\
tkScrollToPos $w [expr {$tkPriv(initPos) + $delta}]\n\
}\n\
}\n\
proc tkScrollEndDrag {w x y} {\n\
global tkPriv\n\
if {$tkPriv(initPos) == \"\"} {\n\
return\n\
}\n\
if {[$w cget -jump]} {\n\
set delta [$w delta [expr {$x - $tkPriv(pressX)}] \\\n\
[expr {$y - $tkPriv(pressY)}]]\n\
tkScrollToPos $w [expr {$tkPriv(initPos) + $delta}]\n\
}\n\
set tkPriv(initPos) \"\"\n\
}\n\
proc tkScrollByUnits {w orient amount} {\n\
set cmd [$w cget -command]\n\
if {($cmd == \"\") || ([string first \\\n\
[string index [$w cget -orient] 0] $orient] < 0)} {\n\
return\n\
}\n\
set info [$w get]\n\
if {[llength $info] == 2} {\n\
uplevel #0 $cmd scroll $amount units\n\
} else {\n\
uplevel #0 $cmd [expr [lindex $info 2] + $amount]\n\
}\n\
}\n\
proc tkScrollByPages {w orient amount} {\n\
set cmd [$w cget -command]\n\
if {($cmd == \"\") || ([string first \\\n\
[string index [$w cget -orient] 0] $orient] < 0)} {\n\
return\n\
}\n\
set info [$w get]\n\
if {[llength $info] == 2} {\n\
uplevel #0 $cmd scroll $amount pages\n\
} else {\n\
uplevel #0 $cmd [expr [lindex $info 2] + $amount*([lindex $info 1] - 1)]\n\
}\n\
}\n\
proc tkScrollToPos {w pos} {\n\
set cmd [$w cget -command]\n\
if {($cmd == \"\")} {\n\
return\n\
}\n\
set info [$w get]\n\
if {[llength $info] == 2} {\n\
uplevel #0 $cmd moveto $pos\n\
} else {\n\
uplevel #0 $cmd [expr round([lindex $info 0]*$pos)]\n\
}\n\
}\n\
proc tkScrollTopBottom {w x y} {\n\
global tkPriv\n\
set element [$w identify $x $y]\n\
if {[string match *1 $element]} {\n\
tkScrollToPos $w 0\n\
} elseif {[string match *2 $element]} {\n\
tkScrollToPos $w 1\n\
}\n\
set tkPriv(relief) [$w cget -activerelief]\n\
}\n\
proc tkScrollButton2Down {w x y} {\n\
global tkPriv\n\
set element [$w identify $x $y]\n\
if {($element == \"arrow1\") || ($element == \"arrow2\")} {\n\
tkScrollButtonDown $w $x $y\n\
return\n\
}\n\
tkScrollToPos $w [$w fraction $x $y]\n\
set tkPriv(relief) [$w cget -activerelief]\n\
update idletasks\n\
$w configure -activerelief sunken\n\
$w activate slider\n\
tkScrollStartDrag $w $x $y\n\
}\n\
";
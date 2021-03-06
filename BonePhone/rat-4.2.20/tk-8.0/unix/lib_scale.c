/* This file is automatically generated by tcl2c, do NOT edit by hand! */
char lib_scale[] = "\
bind Scale <Enter> {\n\
if {$tk_strictMotif} {\n\
set tkPriv(activeBg) [%W cget -activebackground]\n\
%W config -activebackground [%W cget -background]\n\
}\n\
tkScaleActivate %W %x %y\n\
}\n\
bind Scale <Motion> {\n\
tkScaleActivate %W %x %y\n\
}\n\
bind Scale <Leave> {\n\
if {$tk_strictMotif} {\n\
%W config -activebackground $tkPriv(activeBg)\n\
}\n\
if {[%W cget -state] == \"active\"} {\n\
%W configure -state normal\n\
}\n\
}\n\
bind Scale <1> {\n\
tkScaleButtonDown %W %x %y\n\
}\n\
bind Scale <B1-Motion> {\n\
tkScaleDrag %W %x %y\n\
}\n\
bind Scale <B1-Leave> { }\n\
bind Scale <B1-Enter> { }\n\
bind Scale <ButtonRelease-1> {\n\
tkCancelRepeat\n\
tkScaleEndDrag %W\n\
tkScaleActivate %W %x %y\n\
}\n\
bind Scale <2> {\n\
tkScaleButton2Down %W %x %y\n\
}\n\
bind Scale <B2-Motion> {\n\
tkScaleDrag %W %x %y\n\
}\n\
bind Scale <B2-Leave> { }\n\
bind Scale <B2-Enter> { }\n\
bind Scale <ButtonRelease-2> {\n\
tkCancelRepeat\n\
tkScaleEndDrag %W\n\
tkScaleActivate %W %x %y\n\
}\n\
bind Scale <Control-1> {\n\
tkScaleControlPress %W %x %y\n\
}\n\
bind Scale <Up> {\n\
tkScaleIncrement %W up little noRepeat\n\
}\n\
bind Scale <Down> {\n\
tkScaleIncrement %W down little noRepeat\n\
}\n\
bind Scale <Left> {\n\
tkScaleIncrement %W up little noRepeat\n\
}\n\
bind Scale <Right> {\n\
tkScaleIncrement %W down little noRepeat\n\
}\n\
bind Scale <Control-Up> {\n\
tkScaleIncrement %W up big noRepeat\n\
}\n\
bind Scale <Control-Down> {\n\
tkScaleIncrement %W down big noRepeat\n\
}\n\
bind Scale <Control-Left> {\n\
tkScaleIncrement %W up big noRepeat\n\
}\n\
bind Scale <Control-Right> {\n\
tkScaleIncrement %W down big noRepeat\n\
}\n\
bind Scale <Home> {\n\
%W set [%W cget -from]\n\
}\n\
bind Scale <End> {\n\
%W set [%W cget -to]\n\
}\n\
proc tkScaleActivate {w x y} {\n\
global tkPriv\n\
if {[$w cget -state] == \"disabled\"} {\n\
return;\n\
}\n\
if {[$w identify $x $y] == \"slider\"} {\n\
$w configure -state active\n\
} else {\n\
$w configure -state normal\n\
}\n\
}\n\
proc tkScaleButtonDown {w x y} {\n\
global tkPriv\n\
set tkPriv(dragging) 0\n\
set el [$w identify $x $y]\n\
if {$el == \"trough1\"} {\n\
tkScaleIncrement $w up little initial\n\
} elseif {$el == \"trough2\"} {\n\
tkScaleIncrement $w down little initial\n\
} elseif {$el == \"slider\"} {\n\
set tkPriv(dragging) 1\n\
set tkPriv(initValue) [$w get]\n\
set coords [$w coords]\n\
set tkPriv(deltaX) [expr {$x - [lindex $coords 0]}]\n\
set tkPriv(deltaY) [expr {$y - [lindex $coords 1]}]\n\
$w configure -sliderrelief sunken\n\
}\n\
}\n\
proc tkScaleDrag {w x y} {\n\
global tkPriv\n\
if {!$tkPriv(dragging)} {\n\
return\n\
}\n\
$w set [$w get [expr {$x - $tkPriv(deltaX)}] \\\n\
[expr {$y - $tkPriv(deltaY)}]]\n\
}\n\
proc tkScaleEndDrag {w} {\n\
global tkPriv\n\
set tkPriv(dragging) 0\n\
$w configure -sliderrelief raised\n\
}\n\
proc tkScaleIncrement {w dir big repeat} {\n\
global tkPriv\n\
if {![winfo exists $w]} return\n\
if {$big == \"big\"} {\n\
set inc [$w cget -bigincrement]\n\
if {$inc == 0} {\n\
set inc [expr {abs([$w cget -to] - [$w cget -from])/10.0}]\n\
}\n\
if {$inc < [$w cget -resolution]} {\n\
set inc [$w cget -resolution]\n\
}\n\
} else {\n\
set inc [$w cget -resolution]\n\
}\n\
if {([$w cget -from] > [$w cget -to]) ^ ($dir == \"up\")} {\n\
set inc [expr {-$inc}]\n\
}\n\
$w set [expr {[$w get] + $inc}]\n\
if {$repeat == \"again\"} {\n\
set tkPriv(afterId) [after [$w cget -repeatinterval] \\\n\
tkScaleIncrement $w $dir $big again]\n\
} elseif {$repeat == \"initial\"} {\n\
set delay [$w cget -repeatdelay]\n\
if {$delay > 0} {\n\
set tkPriv(afterId) [after $delay \\\n\
tkScaleIncrement $w $dir $big again]\n\
}\n\
}\n\
}\n\
proc tkScaleControlPress {w x y} {\n\
set el [$w identify $x $y]\n\
if {$el == \"trough1\"} {\n\
$w set [$w cget -from]\n\
} elseif {$el == \"trough2\"} {\n\
$w set [$w cget -to]\n\
}\n\
}\n\
proc tkScaleButton2Down {w x y} {\n\
global tkPriv\n\
if {[$w cget -state] == \"disabled\"} {\n\
return;\n\
}\n\
$w configure -state active\n\
$w set [$w get $x $y]\n\
set tkPriv(dragging) 1\n\
set tkPriv(initValue) [$w get]\n\
set coords \"$x $y\"\n\
set tkPriv(deltaX) 0\n\
set tkPriv(deltaY) 0\n\
}\n\
";

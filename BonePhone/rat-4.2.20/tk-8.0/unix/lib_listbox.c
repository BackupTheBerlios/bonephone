/* This file is automatically generated by tcl2c, do NOT edit by hand! */
char lib_listbox[] = "\
bind Listbox <1> {\n\
if {[winfo exists %W]} {\n\
tkListboxBeginSelect %W [%W index @%x,%y]\n\
}\n\
}\n\
bind Listbox <Double-1> {\n\
}\n\
bind Listbox <B1-Motion> {\n\
set tkPriv(x) %x\n\
set tkPriv(y) %y\n\
tkListboxMotion %W [%W index @%x,%y]\n\
}\n\
bind Listbox <ButtonRelease-1> {\n\
tkCancelRepeat\n\
%W activate @%x,%y\n\
}\n\
bind Listbox <Shift-1> {\n\
tkListboxBeginExtend %W [%W index @%x,%y]\n\
}\n\
bind Listbox <Control-1> {\n\
tkListboxBeginToggle %W [%W index @%x,%y]\n\
}\n\
bind Listbox <B1-Leave> {\n\
set tkPriv(x) %x\n\
set tkPriv(y) %y\n\
tkListboxAutoScan %W\n\
}\n\
bind Listbox <B1-Enter> {\n\
tkCancelRepeat\n\
}\n\
bind Listbox <Up> {\n\
tkListboxUpDown %W -1\n\
}\n\
bind Listbox <Shift-Up> {\n\
tkListboxExtendUpDown %W -1\n\
}\n\
bind Listbox <Down> {\n\
tkListboxUpDown %W 1\n\
}\n\
bind Listbox <Shift-Down> {\n\
tkListboxExtendUpDown %W 1\n\
}\n\
bind Listbox <Left> {\n\
%W xview scroll -1 units\n\
}\n\
bind Listbox <Control-Left> {\n\
%W xview scroll -1 pages\n\
}\n\
bind Listbox <Right> {\n\
%W xview scroll 1 units\n\
}\n\
bind Listbox <Control-Right> {\n\
%W xview scroll 1 pages\n\
}\n\
bind Listbox <Prior> {\n\
%W yview scroll -1 pages\n\
%W activate @0,0\n\
}\n\
bind Listbox <Next> {\n\
%W yview scroll 1 pages\n\
%W activate @0,0\n\
}\n\
bind Listbox <Control-Prior> {\n\
%W xview scroll -1 pages\n\
}\n\
bind Listbox <Control-Next> {\n\
%W xview scroll 1 pages\n\
}\n\
bind Listbox <Home> {\n\
%W xview moveto 0\n\
}\n\
bind Listbox <End> {\n\
%W xview moveto 1\n\
}\n\
bind Listbox <Control-Home> {\n\
%W activate 0\n\
%W see 0\n\
%W selection clear 0 end\n\
%W selection set 0\n\
}\n\
bind Listbox <Shift-Control-Home> {\n\
tkListboxDataExtend %W 0\n\
}\n\
bind Listbox <Control-End> {\n\
%W activate end\n\
%W see end\n\
%W selection clear 0 end\n\
%W selection set end\n\
}\n\
bind Listbox <Shift-Control-End> {\n\
tkListboxDataExtend %W [%W index end]\n\
}\n\
bind Listbox <<Copy>> {\n\
if {[selection own -displayof %W] == \"%W\"} {\n\
clipboard clear -displayof %W\n\
clipboard append -displayof %W [selection get -displayof %W]\n\
}\n\
}\n\
bind Listbox <space> {\n\
tkListboxBeginSelect %W [%W index active]\n\
}\n\
bind Listbox <Select> {\n\
tkListboxBeginSelect %W [%W index active]\n\
}\n\
bind Listbox <Control-Shift-space> {\n\
tkListboxBeginExtend %W [%W index active]\n\
}\n\
bind Listbox <Shift-Select> {\n\
tkListboxBeginExtend %W [%W index active]\n\
}\n\
bind Listbox <Escape> {\n\
tkListboxCancel %W\n\
}\n\
bind Listbox <Control-slash> {\n\
tkListboxSelectAll %W\n\
}\n\
bind Listbox <Control-backslash> {\n\
if {[%W cget -selectmode] != \"browse\"} {\n\
%W selection clear 0 end\n\
}\n\
}\n\
bind Listbox <2> {\n\
%W scan mark %x %y\n\
}\n\
bind Listbox <B2-Motion> {\n\
%W scan dragto %x %y\n\
}\n\
proc tkListboxBeginSelect {w el} {\n\
global tkPriv\n\
if {[$w cget -selectmode]  == \"multiple\"} {\n\
if {[$w selection includes $el]} {\n\
$w selection clear $el\n\
} else {\n\
$w selection set $el\n\
}\n\
} else {\n\
$w selection clear 0 end\n\
$w selection set $el\n\
$w selection anchor $el\n\
set tkPriv(listboxSelection) {}\n\
set tkPriv(listboxPrev) $el\n\
}\n\
}\n\
proc tkListboxMotion {w el} {\n\
global tkPriv\n\
if {$el == $tkPriv(listboxPrev)} {\n\
return\n\
}\n\
set anchor [$w index anchor]\n\
switch [$w cget -selectmode] {\n\
browse {\n\
$w selection clear 0 end\n\
$w selection set $el\n\
set tkPriv(listboxPrev) $el\n\
}\n\
extended {\n\
set i $tkPriv(listboxPrev)\n\
if {[$w selection includes anchor]} {\n\
$w selection clear $i $el\n\
$w selection set anchor $el\n\
} else {\n\
$w selection clear $i $el\n\
$w selection clear anchor $el\n\
}\n\
while {($i < $el) && ($i < $anchor)} {\n\
if {[lsearch $tkPriv(listboxSelection) $i] >= 0} {\n\
$w selection set $i\n\
}\n\
incr i\n\
}\n\
while {($i > $el) && ($i > $anchor)} {\n\
if {[lsearch $tkPriv(listboxSelection) $i] >= 0} {\n\
$w selection set $i\n\
}\n\
incr i -1\n\
}\n\
set tkPriv(listboxPrev) $el\n\
}\n\
}\n\
}\n\
proc tkListboxBeginExtend {w el} {\n\
if {[$w cget -selectmode] == \"extended\"} {\n\
if {[$w selection includes anchor]} {\n\
tkListboxMotion $w $el\n\
} else {\n\
tkListboxBeginSelect $w $el\n\
}\n\
}\n\
}\n\
proc tkListboxBeginToggle {w el} {\n\
global tkPriv\n\
if {[$w cget -selectmode] == \"extended\"} {\n\
set tkPriv(listboxSelection) [$w curselection]\n\
set tkPriv(listboxPrev) $el\n\
$w selection anchor $el\n\
if {[$w selection includes $el]} {\n\
$w selection clear $el\n\
} else {\n\
$w selection set $el\n\
}\n\
}\n\
}\n\
proc tkListboxAutoScan {w} {\n\
global tkPriv\n\
if {![winfo exists $w]} return\n\
set x $tkPriv(x)\n\
set y $tkPriv(y)\n\
if {$y >= [winfo height $w]} {\n\
$w yview scroll 1 units\n\
} elseif {$y < 0} {\n\
$w yview scroll -1 units\n\
} elseif {$x >= [winfo width $w]} {\n\
$w xview scroll 2 units\n\
} elseif {$x < 0} {\n\
$w xview scroll -2 units\n\
} else {\n\
return\n\
}\n\
tkListboxMotion $w [$w index @$x,$y]\n\
set tkPriv(afterId) [after 50 tkListboxAutoScan $w]\n\
}\n\
proc tkListboxUpDown {w amount} {\n\
global tkPriv\n\
$w activate [expr {[$w index active] + $amount}]\n\
$w see active\n\
switch [$w cget -selectmode] {\n\
browse {\n\
$w selection clear 0 end\n\
$w selection set active\n\
}\n\
extended {\n\
$w selection clear 0 end\n\
$w selection set active\n\
$w selection anchor active\n\
set tkPriv(listboxPrev) [$w index active]\n\
set tkPriv(listboxSelection) {}\n\
}\n\
}\n\
}\n\
proc tkListboxExtendUpDown {w amount} {\n\
if {[$w cget -selectmode] != \"extended\"} {\n\
return\n\
}\n\
$w activate [expr {[$w index active] + $amount}]\n\
$w see active\n\
tkListboxMotion $w [$w index active]\n\
}\n\
proc tkListboxDataExtend {w el} {\n\
set mode [$w cget -selectmode]\n\
if {$mode == \"extended\"} {\n\
$w activate $el\n\
$w see $el\n\
if {[$w selection includes anchor]} {\n\
tkListboxMotion $w $el\n\
}\n\
} elseif {$mode == \"multiple\"} {\n\
$w activate $el\n\
$w see $el\n\
}\n\
}\n\
proc tkListboxCancel w {\n\
global tkPriv\n\
if {[$w cget -selectmode] != \"extended\"} {\n\
return\n\
}\n\
set first [$w index anchor]\n\
set last $tkPriv(listboxPrev)\n\
if {$first > $last} {\n\
set tmp $first\n\
set first $last\n\
set last $tmp\n\
}\n\
$w selection clear $first $last\n\
while {$first <= $last} {\n\
if {[lsearch $tkPriv(listboxSelection) $first] >= 0} {\n\
$w selection set $first\n\
}\n\
incr first\n\
}\n\
}\n\
proc tkListboxSelectAll w {\n\
set mode [$w cget -selectmode]\n\
if {($mode == \"single\") || ($mode == \"browse\")} {\n\
$w selection clear 0 end\n\
$w selection set active\n\
} else {\n\
$w selection set 0 end\n\
}\n\
}\n\
";

/* This file is automatically generated by tcl2c, do NOT edit by hand! */
char lib_entry[] = "\
bind Entry <<Cut>> {\n\
if {![catch {set data [string range [%W get] [%W index sel.first]\\\n\
[expr {[%W index sel.last] - 1}]]}]} {\n\
clipboard clear -displayof %W\n\
clipboard append -displayof %W $data\n\
%W delete sel.first sel.last\n\
}\n\
}\n\
bind Entry <<Copy>> {\n\
if {![catch {set data [string range [%W get] [%W index sel.first]\\\n\
[expr {[%W index sel.last] - 1}]]}]} {\n\
clipboard clear -displayof %W\n\
clipboard append -displayof %W $data\n\
}\n\
}\n\
bind Entry <<Paste>> {\n\
global tcl_platform\n\
catch {\n\
if {\"$tcl_platform(platform)\" != \"unix\"} {\n\
catch {\n\
%W delete sel.first sel.last\n\
}\n\
}\n\
%W insert insert [selection get -displayof %W -selection CLIPBOARD]\n\
tkEntrySeeInsert %W\n\
}\n\
}\n\
bind Entry <<Clear>> {\n\
%W delete sel.first sel.last\n\
}\n\
bind Entry <<PasteSelection>> {\n\
if {!$tkPriv(mouseMoved) || $tk_strictMotif} {\n\
tkEntryPaste %W %x\n\
}\n\
}\n\
bind Entry <1> {\n\
tkEntryButton1 %W %x\n\
%W selection clear\n\
}\n\
bind Entry <B1-Motion> {\n\
set tkPriv(x) %x\n\
tkEntryMouseSelect %W %x\n\
}\n\
bind Entry <Double-1> {\n\
set tkPriv(selectMode) word\n\
tkEntryMouseSelect %W %x\n\
catch {%W icursor sel.first}\n\
}\n\
bind Entry <Triple-1> {\n\
set tkPriv(selectMode) line\n\
tkEntryMouseSelect %W %x\n\
%W icursor 0\n\
}\n\
bind Entry <Shift-1> {\n\
set tkPriv(selectMode) char\n\
%W selection adjust @%x\n\
}\n\
bind Entry <Double-Shift-1>	{\n\
set tkPriv(selectMode) word\n\
tkEntryMouseSelect %W %x\n\
}\n\
bind Entry <Triple-Shift-1>	{\n\
set tkPriv(selectMode) line\n\
tkEntryMouseSelect %W %x\n\
}\n\
bind Entry <B1-Leave> {\n\
set tkPriv(x) %x\n\
tkEntryAutoScan %W\n\
}\n\
bind Entry <B1-Enter> {\n\
tkCancelRepeat\n\
}\n\
bind Entry <ButtonRelease-1> {\n\
tkCancelRepeat\n\
}\n\
bind Entry <Control-1> {\n\
%W icursor @%x\n\
}\n\
bind Entry <Left> {\n\
tkEntrySetCursor %W [expr {[%W index insert] - 1}]\n\
}\n\
bind Entry <Right> {\n\
tkEntrySetCursor %W [expr {[%W index insert] + 1}]\n\
}\n\
bind Entry <Shift-Left> {\n\
tkEntryKeySelect %W [expr {[%W index insert] - 1}]\n\
tkEntrySeeInsert %W\n\
}\n\
bind Entry <Shift-Right> {\n\
tkEntryKeySelect %W [expr {[%W index insert] + 1}]\n\
tkEntrySeeInsert %W\n\
}\n\
bind Entry <Control-Left> {\n\
tkEntrySetCursor %W [tkEntryPreviousWord %W insert]\n\
}\n\
bind Entry <Control-Right> {\n\
tkEntrySetCursor %W [tkEntryNextWord %W insert]\n\
}\n\
bind Entry <Shift-Control-Left> {\n\
tkEntryKeySelect %W [tkEntryPreviousWord %W insert]\n\
tkEntrySeeInsert %W\n\
}\n\
bind Entry <Shift-Control-Right> {\n\
tkEntryKeySelect %W [tkEntryNextWord %W insert]\n\
tkEntrySeeInsert %W\n\
}\n\
bind Entry <Home> {\n\
tkEntrySetCursor %W 0\n\
}\n\
bind Entry <Shift-Home> {\n\
tkEntryKeySelect %W 0\n\
tkEntrySeeInsert %W\n\
}\n\
bind Entry <End> {\n\
tkEntrySetCursor %W end\n\
}\n\
bind Entry <Shift-End> {\n\
tkEntryKeySelect %W end\n\
tkEntrySeeInsert %W\n\
}\n\
bind Entry <Delete> {\n\
if {[%W selection present]} {\n\
%W delete sel.first sel.last\n\
} else {\n\
%W delete insert\n\
}\n\
}\n\
bind Entry <BackSpace> {\n\
tkEntryBackspace %W\n\
}\n\
bind Entry <Control-space> {\n\
%W selection from insert\n\
}\n\
bind Entry <Select> {\n\
%W selection from insert\n\
}\n\
bind Entry <Control-Shift-space> {\n\
%W selection adjust insert\n\
}\n\
bind Entry <Shift-Select> {\n\
%W selection adjust insert\n\
}\n\
bind Entry <Control-slash> {\n\
%W selection range 0 end\n\
}\n\
bind Entry <Control-backslash> {\n\
%W selection clear\n\
}\n\
bind Entry <KeyPress> {\n\
tkEntryInsert %W %A\n\
}\n\
bind Entry <Alt-KeyPress> {# nothing}\n\
bind Entry <Meta-KeyPress> {# nothing}\n\
bind Entry <Control-KeyPress> {# nothing}\n\
bind Entry <Escape> {# nothing}\n\
bind Entry <Return> {# nothing}\n\
bind Entry <KP_Enter> {# nothing}\n\
bind Entry <Tab> {# nothing}\n\
if {$tcl_platform(platform) == \"macintosh\"} {\n\
bind Entry <Command-KeyPress> {# nothing}\n\
}\n\
if {$tcl_platform(platform) != \"windows\"} {\n\
bind Entry <Insert> {\n\
catch {tkEntryInsert %W [selection get -displayof %W]}\n\
}\n\
}\n\
bind Entry <Control-a> {\n\
if {!$tk_strictMotif} {\n\
tkEntrySetCursor %W 0\n\
}\n\
}\n\
bind Entry <Control-b> {\n\
if {!$tk_strictMotif} {\n\
tkEntrySetCursor %W [expr {[%W index insert] - 1}]\n\
}\n\
}\n\
bind Entry <Control-d> {\n\
if {!$tk_strictMotif} {\n\
%W delete insert\n\
}\n\
}\n\
bind Entry <Control-e> {\n\
if {!$tk_strictMotif} {\n\
tkEntrySetCursor %W end\n\
}\n\
}\n\
bind Entry <Control-f> {\n\
if {!$tk_strictMotif} {\n\
tkEntrySetCursor %W [expr {[%W index insert] + 1}]\n\
}\n\
}\n\
bind Entry <Control-h> {\n\
if {!$tk_strictMotif} {\n\
tkEntryBackspace %W\n\
}\n\
}\n\
bind Entry <Control-k> {\n\
if {!$tk_strictMotif} {\n\
%W delete insert end\n\
}\n\
}\n\
bind Entry <Control-t> {\n\
if {!$tk_strictMotif} {\n\
tkEntryTranspose %W\n\
}\n\
}\n\
bind Entry <Meta-b> {\n\
if {!$tk_strictMotif} {\n\
tkEntrySetCursor %W [tkEntryPreviousWord %W insert]\n\
}\n\
}\n\
bind Entry <Meta-d> {\n\
if {!$tk_strictMotif} {\n\
%W delete insert [tkEntryNextWord %W insert]\n\
}\n\
}\n\
bind Entry <Meta-f> {\n\
if {!$tk_strictMotif} {\n\
tkEntrySetCursor %W [tkEntryNextWord %W insert]\n\
}\n\
}\n\
bind Entry <Meta-BackSpace> {\n\
if {!$tk_strictMotif} {\n\
%W delete [tkEntryPreviousWord %W insert] insert\n\
}\n\
}\n\
bind Entry <Meta-Delete> {\n\
if {!$tk_strictMotif} {\n\
%W delete [tkEntryPreviousWord %W insert] insert\n\
}\n\
}\n\
bind Entry <2> {\n\
if {!$tk_strictMotif} {\n\
%W scan mark %x\n\
set tkPriv(x) %x\n\
set tkPriv(y) %y\n\
set tkPriv(mouseMoved) 0\n\
}\n\
}\n\
bind Entry <B2-Motion> {\n\
if {!$tk_strictMotif} {\n\
if {abs(%x-$tkPriv(x)) > 2} {\n\
set tkPriv(mouseMoved) 1\n\
}\n\
%W scan dragto %x\n\
}\n\
}\n\
proc tkEntryClosestGap {w x} {\n\
set pos [$w index @$x]\n\
set bbox [$w bbox $pos]\n\
if {($x - [lindex $bbox 0]) < ([lindex $bbox 2]/2)} {\n\
return $pos\n\
}\n\
incr pos\n\
}\n\
proc tkEntryButton1 {w x} {\n\
global tkPriv\n\
set tkPriv(selectMode) char\n\
set tkPriv(mouseMoved) 0\n\
set tkPriv(pressX) $x\n\
$w icursor [tkEntryClosestGap $w $x]\n\
$w selection from insert\n\
if {[lindex [$w configure -state] 4] == \"normal\"} {focus $w}\n\
}\n\
proc tkEntryMouseSelect {w x} {\n\
global tkPriv\n\
set cur [tkEntryClosestGap $w $x]\n\
set anchor [$w index anchor]\n\
if {($cur != $anchor) || (abs($tkPriv(pressX) - $x) >= 3)} {\n\
set tkPriv(mouseMoved) 1\n\
}\n\
switch $tkPriv(selectMode) {\n\
char {\n\
if {$tkPriv(mouseMoved)} {\n\
if {$cur < $anchor} {\n\
$w selection range $cur $anchor\n\
} elseif {$cur > $anchor} {\n\
$w selection range $anchor $cur\n\
} else {\n\
$w selection clear\n\
}\n\
}\n\
}\n\
word {\n\
if {$cur < [$w index anchor]} {\n\
set before [tcl_wordBreakBefore [$w get] $cur]\n\
set after [tcl_wordBreakAfter [$w get] [expr {$anchor-1}]]\n\
} else {\n\
set before [tcl_wordBreakBefore [$w get] $anchor]\n\
set after [tcl_wordBreakAfter [$w get] [expr {$cur - 1}]]\n\
}\n\
if {$before < 0} {\n\
set before 0\n\
}\n\
if {$after < 0} {\n\
set after end\n\
}\n\
$w selection range $before $after\n\
}\n\
line {\n\
$w selection range 0 end\n\
}\n\
}\n\
update idletasks\n\
}\n\
proc tkEntryPaste {w x} {\n\
global tkPriv\n\
$w icursor [tkEntryClosestGap $w $x]\n\
catch {$w insert insert [selection get -displayof $w]}\n\
if {[lindex [$w configure -state] 4] == \"normal\"} {focus $w}\n\
}\n\
proc tkEntryAutoScan {w} {\n\
global tkPriv\n\
set x $tkPriv(x)\n\
if {![winfo exists $w]} return\n\
if {$x >= [winfo width $w]} {\n\
$w xview scroll 2 units\n\
tkEntryMouseSelect $w $x\n\
} elseif {$x < 0} {\n\
$w xview scroll -2 units\n\
tkEntryMouseSelect $w $x\n\
}\n\
set tkPriv(afterId) [after 50 tkEntryAutoScan $w]\n\
}\n\
proc tkEntryKeySelect {w new} {\n\
if {![$w selection present]} {\n\
$w selection from insert\n\
$w selection to $new\n\
} else {\n\
$w selection adjust $new\n\
}\n\
$w icursor $new\n\
}\n\
proc tkEntryInsert {w s} {\n\
if {$s == \"\"} {\n\
return\n\
}\n\
catch {\n\
set insert [$w index insert]\n\
if {([$w index sel.first] <= $insert)\n\
&& ([$w index sel.last] >= $insert)} {\n\
$w delete sel.first sel.last\n\
}\n\
}\n\
$w insert insert $s\n\
tkEntrySeeInsert $w\n\
}\n\
proc tkEntryBackspace w {\n\
if {[$w selection present]} {\n\
$w delete sel.first sel.last\n\
} else {\n\
set x [expr {[$w index insert] - 1}]\n\
if {$x >= 0} {$w delete $x}\n\
if {[$w index @0] >= [$w index insert]} {\n\
set range [$w xview]\n\
set left [lindex $range 0]\n\
set right [lindex $range 1]\n\
$w xview moveto [expr {$left - ($right - $left)/2.0}]\n\
}\n\
}\n\
}\n\
proc tkEntrySeeInsert w {\n\
set c [$w index insert]\n\
set left [$w index @0]\n\
if {$left > $c} {\n\
$w xview $c\n\
return\n\
}\n\
set x [winfo width $w]\n\
while {([$w index @$x] <= $c) && ($left < $c)} {\n\
incr left\n\
$w xview $left\n\
}\n\
}\n\
proc tkEntrySetCursor {w pos} {\n\
$w icursor $pos\n\
$w selection clear\n\
tkEntrySeeInsert $w\n\
}\n\
proc tkEntryTranspose w {\n\
set i [$w index insert]\n\
if {$i < [$w index end]} {\n\
incr i\n\
}\n\
set first [expr {$i-2}]\n\
if {$first < 0} {\n\
return\n\
}\n\
set new [string index [$w get] [expr {$i-1}]][string index [$w get] $first]\n\
$w delete $first $i\n\
$w insert insert $new\n\
tkEntrySeeInsert $w\n\
}\n\
if {$tcl_platform(platform) == \"windows\"}  {\n\
proc tkEntryNextWord {w start} {\n\
set pos [tcl_endOfWord [$w get] [$w index $start]]\n\
if {$pos >= 0} {\n\
set pos [tcl_startOfNextWord [$w get] $pos]\n\
}\n\
if {$pos < 0} {\n\
return end\n\
}\n\
return $pos\n\
}\n\
} else {\n\
proc tkEntryNextWord {w start} {\n\
set pos [tcl_endOfWord [$w get] [$w index $start]]\n\
if {$pos < 0} {\n\
return end\n\
}\n\
return $pos\n\
}\n\
}\n\
proc tkEntryPreviousWord {w start} {\n\
set pos [tcl_startOfPreviousWord [$w get] [$w index $start]]\n\
if {$pos < 0} {\n\
return 0\n\
}\n\
return $pos\n\
}\n\
";

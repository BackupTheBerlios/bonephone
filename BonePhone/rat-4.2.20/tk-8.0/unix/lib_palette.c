/* This file is automatically generated by tcl2c, do NOT edit by hand! */
char lib_palette[] = "\
proc tk_setPalette {args} {\n\
global tkPalette\n\
if {[llength $args] == 1} {\n\
set new(background) [lindex $args 0]\n\
} else {\n\
array set new $args\n\
}\n\
if {![info exists new(background)]} {\n\
error \"must specify a background color\"\n\
}\n\
if {![info exists new(foreground)]} {\n\
set new(foreground) black\n\
}\n\
set bg [winfo rgb . $new(background)]\n\
set fg [winfo rgb . $new(foreground)]\n\
set darkerBg [format #%02x%02x%02x [expr {(9*[lindex $bg 0])/2560}] \\\n\
[expr {(9*[lindex $bg 1])/2560}] [expr {(9*[lindex $bg 2])/2560}]]\n\
foreach i {activeForeground insertBackground selectForeground \\\n\
highlightColor} {\n\
if {![info exists new($i)]} {\n\
set new($i) $new(foreground)\n\
}\n\
}\n\
if {![info exists new(disabledForeground)]} {\n\
set new(disabledForeground) [format #%02x%02x%02x \\\n\
[expr {(3*[lindex $bg 0] + [lindex $fg 0])/1024}] \\\n\
[expr {(3*[lindex $bg 1] + [lindex $fg 1])/1024}] \\\n\
[expr {(3*[lindex $bg 2] + [lindex $fg 2])/1024}]]\n\
}\n\
if {![info exists new(highlightBackground)]} {\n\
set new(highlightBackground) $new(background)\n\
}\n\
if {![info exists new(activeBackground)]} {\n\
foreach i {0 1 2} {\n\
set light($i) [expr {[lindex $bg $i]/256}]\n\
set inc1 [expr {($light($i)*15)/100}]\n\
set inc2 [expr {(255-$light($i))/3}]\n\
if {$inc1 > $inc2} {\n\
incr light($i) $inc1\n\
} else {\n\
incr light($i) $inc2\n\
}\n\
if {$light($i) > 255} {\n\
set light($i) 255\n\
}\n\
}\n\
set new(activeBackground) [format #%02x%02x%02x $light(0) \\\n\
$light(1) $light(2)]\n\
}\n\
if {![info exists new(selectBackground)]} {\n\
set new(selectBackground) $darkerBg\n\
}\n\
if {![info exists new(troughColor)]} {\n\
set new(troughColor) $darkerBg\n\
}\n\
if {![info exists new(selectColor)]} {\n\
set new(selectColor) #b03060\n\
}\n\
toplevel .___tk_set_palette\n\
wm withdraw .___tk_set_palette\n\
foreach q {button canvas checkbutton entry frame label listbox menubutton menu message \\\n\
radiobutton scale scrollbar text} {\n\
$q .___tk_set_palette.$q\n\
}\n\
eval [tkRecolorTree . new]\n\
catch {destroy .___tk_set_palette}\n\
foreach option [array names new] {\n\
option add *$option $new($option) widgetDefault\n\
}\n\
array set tkPalette [array get new]\n\
}\n\
proc tkRecolorTree {w colors} {\n\
global tkPalette\n\
upvar $colors c\n\
set result {}\n\
foreach dbOption [array names c] {\n\
set option -[string tolower $dbOption]\n\
if {![catch {$w config $option} value]} {\n\
set defaultcolor [option get $w $dbOption widgetDefault]\n\
if {[string match {} $defaultcolor]} {\n\
set defaultcolor [winfo rgb . [lindex $value 3]]\n\
} else {\n\
set defaultcolor [winfo rgb . $defaultcolor]\n\
}\n\
set chosencolor [winfo rgb . [lindex $value 4]]\n\
if {[string match $defaultcolor $chosencolor]} {\n\
append result \";\\noption add [list \\\n\
*[winfo class $w].$dbOption $c($dbOption) 60]\"\n\
$w configure $option $c($dbOption)\n\
}\n\
}\n\
}\n\
foreach child [winfo children $w] {\n\
append result \";\\n[tkRecolorTree $child c]\"\n\
}\n\
return $result\n\
}\n\
proc tkDarken {color percent} {\n\
set l [winfo rgb . $color]\n\
set red [expr {[lindex $l 0]/256}]\n\
set green [expr {[lindex $l 1]/256}]\n\
set blue [expr {[lindex $l 2]/256}]\n\
set red [expr {($red*$percent)/100}]\n\
if {$red > 255} {\n\
set red 255\n\
}\n\
set green [expr {($green*$percent)/100}]\n\
if {$green > 255} {\n\
set green 255\n\
}\n\
set blue [expr {($blue*$percent)/100}]\n\
if {$blue > 255} {\n\
set blue 255\n\
}\n\
format #%02x%02x%02x $red $green $blue\n\
}\n\
proc tk_bisque {} {\n\
tk_setPalette activeBackground #e6ceb1 activeForeground black \\\n\
background #ffe4c4 disabledForeground #b0b0b0 foreground black \\\n\
highlightBackground #ffe4c4 highlightColor black \\\n\
insertBackground black selectColor #b03060 \\\n\
selectBackground #e6ceb1 selectForeground black \\\n\
troughColor #cdb79e\n\
}\n\
";
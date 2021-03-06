/* This file is automatically generated by tcl2c, do NOT edit by hand! */
char lib_dialog[] = "\
proc tk_dialog {w title text bitmap default args} {\n\
global tkPriv tcl_platform\n\
catch {destroy $w}\n\
toplevel $w -class Dialog\n\
wm title $w $title\n\
wm iconname $w Dialog\n\
wm protocol $w WM_DELETE_WINDOW { }\n\
wm transient $w [winfo toplevel [winfo parent $w]]\n\
if {$tcl_platform(platform) == \"macintosh\"} {\n\
unsupported1 style $w dBoxProc\n\
}\n\
frame $w.bot\n\
frame $w.top\n\
if {$tcl_platform(platform) == \"unix\"} {\n\
$w.bot configure -relief raised -bd 1\n\
$w.top configure -relief raised -bd 1\n\
}\n\
pack $w.bot -side bottom -fill both\n\
pack $w.top -side top -fill both -expand 1\n\
option add *Dialog.msg.wrapLength 3i widgetDefault\n\
label $w.msg -justify left -text $text\n\
if {$tcl_platform(platform) == \"macintosh\"} {\n\
$w.msg configure -font system\n\
} else {\n\
$w.msg configure -font {Times 18}\n\
}\n\
pack $w.msg -in $w.top -side right -expand 1 -fill both -padx 3m -pady 3m\n\
if {$bitmap != \"\"} {\n\
if {($tcl_platform(platform) == \"macintosh\") && ($bitmap == \"error\")} {\n\
set bitmap \"stop\"\n\
}\n\
label $w.bitmap -bitmap $bitmap\n\
pack $w.bitmap -in $w.top -side left -padx 3m -pady 3m\n\
}\n\
set i 0\n\
foreach but $args {\n\
button $w.button$i -text $but -command \"set tkPriv(button) $i\"\n\
if {$i == $default} {\n\
$w.button$i configure -default active\n\
} else {\n\
$w.button$i configure -default normal\n\
}\n\
grid $w.button$i -in $w.bot -column $i -row 0 -sticky ew -padx 10\n\
grid columnconfigure $w.bot $i\n\
if {$tcl_platform(platform) == \"macintosh\"} {\n\
set tmp [string tolower $but]\n\
if {($tmp == \"ok\") || ($tmp == \"cancel\")} {\n\
grid columnconfigure $w.bot $i -minsize [expr 59 + 20]\n\
}\n\
}\n\
incr i\n\
}\n\
if {$default >= 0} {\n\
bind $w <Return> \"\n\
$w.button$default configure -state active -relief sunken\n\
update idletasks\n\
after 100\n\
set tkPriv(button) $default\n\
\"\n\
}\n\
bind $w <Destroy> {set tkPriv(button) -1}\n\
wm withdraw $w\n\
update idletasks\n\
set x [expr {[winfo screenwidth $w]/2 - [winfo reqwidth $w]/2 \\\n\
- [winfo vrootx [winfo parent $w]]}]\n\
set y [expr {[winfo screenheight $w]/2 - [winfo reqheight $w]/2 \\\n\
- [winfo vrooty [winfo parent $w]]}]\n\
wm geom $w +$x+$y\n\
wm deiconify $w\n\
set oldFocus [focus]\n\
set oldGrab [grab current $w]\n\
if {$oldGrab != \"\"} {\n\
set grabStatus [grab status $oldGrab]\n\
}\n\
grab $w\n\
if {$default >= 0} {\n\
focus $w.button$default\n\
} else {\n\
focus $w\n\
}\n\
tkwait variable tkPriv(button)\n\
catch {focus $oldFocus}\n\
catch {\n\
bind $w <Destroy> {}\n\
destroy $w\n\
}\n\
if {$oldGrab != \"\"} {\n\
if {$grabStatus == \"global\"} {\n\
grab -global $oldGrab\n\
} else {\n\
grab $oldGrab\n\
}\n\
}\n\
return $tkPriv(button)\n\
}\n\
";

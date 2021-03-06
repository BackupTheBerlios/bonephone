/* This file is automatically generated by tcl2c, do NOT edit by hand! */
char lib_optparse[] = "\
package provide opt 0.3\n\
namespace eval ::tcl {\n\
namespace export OptKeyRegister OptKeyDelete OptKeyError OptKeyParse \\\n\
OptProc OptProcArgGiven OptParse \\\n\
Lassign Lvarpop Lvarset Lvarincr Lfirst \\\n\
SetMax SetMin\n\
proc OptCreateTestProc {} {\n\
OptProc OptParseTest {\n\
{subcommand -choice {save print} \"sub command\"}\n\
{arg1 3 \"some number\"}\n\
{-aflag}\n\
{-intflag      7}\n\
{-weirdflag                    \"help string\"}\n\
{-noStatics                    \"Not ok to load static packages\"}\n\
{-nestedloading1 true           \"OK to load into nested slaves\"}\n\
{-nestedloading2 -boolean true \"OK to load into nested slaves\"}\n\
{-libsOK        -choice {Tk SybTcl}\n\
\"List of packages that can be loaded\"}\n\
{-precision     -int 12        \"Number of digits of precision\"}\n\
{-intval        7               \"An integer\"}\n\
{-scale         -float 1.0     \"Scale factor\"}\n\
{-zoom          1.0             \"Zoom factor\"}\n\
{-arbitrary     foobar          \"Arbitrary string\"}\n\
{-random        -string 12   \"Random string\"}\n\
{-listval       -list {}       \"List value\"}\n\
{-blahflag       -blah abc       \"Funny type\"}\n\
{arg2 -boolean \"a boolean\"}\n\
{arg3 -choice \"ch1 ch2\"}\n\
{?optarg? -list {} \"optional argument\"}\n\
} {\n\
foreach v [info locals] {\n\
puts stderr [format \"%14s : %s\" $v [set $v]]\n\
}\n\
}\n\
}\n\
variable OptDesc;\n\
array set OptDesc {};\n\
variable OptDescN 0;\n\
proc ::tcl::OptKeyRegister {desc {key \"\"}} {\n\
variable OptDesc;\n\
variable OptDescN;\n\
if {[string compare $key \"\"] == 0} {\n\
while {[info exists OptDesc($OptDescN)]} {incr OptDescN}\n\
set key $OptDescN;\n\
incr OptDescN;\n\
}\n\
set program [list [list \"P\" 1]];\n\
set inflags 0;\n\
set state {};\n\
set empty 1;\n\
foreach item $desc {\n\
if {$state == \"args\"} {\n\
return -code error \"'args' special argument must be the last one\";\n\
}\n\
set res [OptNormalizeOne $item];\n\
set state [Lfirst $res];\n\
if {$inflags} {\n\
if {$state == \"flags\"} {\n\
lappend flagsprg $res;\n\
} else {\n\
lappend program $flagsprg;\n\
lappend program $res;\n\
set inflags 0;\n\
set empty 0;\n\
}\n\
} else {\n\
if {$state == \"flags\"} {\n\
set inflags 1;\n\
set flagsprg [list [list \"P\" 1] $res];\n\
} else {\n\
lappend program $res;\n\
set empty 0;\n\
}\n\
}\n\
}\n\
if {$inflags} {\n\
if {$empty} {\n\
set program $flagsprg;\n\
} else {\n\
lappend program $flagsprg;\n\
}\n\
}\n\
set OptDesc($key) $program;\n\
return $key;\n\
}\n\
proc ::tcl::OptKeyDelete {key} {\n\
variable OptDesc;\n\
unset OptDesc($key);\n\
}\n\
proc OptKeyGetDesc {descKey} {\n\
variable OptDesc;\n\
if {![info exists OptDesc($descKey)]} {\n\
return -code error \"Unknown option description key \\\"$descKey\\\"\";\n\
}\n\
set OptDesc($descKey);\n\
}\n\
proc ::tcl::OptParse {desc arglist} {\n\
set tempkey [OptKeyRegister $desc];\n\
set ret [catch {uplevel [list ::tcl::OptKeyParse $tempkey $arglist]} res];\n\
OptKeyDelete $tempkey;\n\
return -code $ret $res;\n\
}\n\
proc ::tcl::OptProc {name desc body} {\n\
set namespace [uplevel namespace current];\n\
if {   ([string match $name \"::*\"]) \n\
|| ([string compare $namespace \"::\"]==0)} {\n\
set key $name;\n\
} else {\n\
set key \"${namespace}::${name}\";\n\
}\n\
OptKeyRegister $desc $key;\n\
uplevel [list proc $name args \"set Args \\[::tcl::OptKeyParse $key \\$args\\]\\n$body\"];\n\
return $key;\n\
}\n\
proc ::tcl::OptProcArgGiven {argname} {\n\
upvar Args alist;\n\
expr {[lsearch $alist $argname] >=0}\n\
}\n\
proc OptInstr {lst} {\n\
Lfirst $lst;\n\
}\n\
proc OptIsPrg {lst} {\n\
expr {[llength [OptInstr $lst]]>=2}\n\
}\n\
proc OptIsCounter {item} {\n\
expr {[Lfirst $item]==\"P\"}\n\
}\n\
proc OptGetPrgCounter {lst} {\n\
Lget $lst {0 1}\n\
}\n\
proc OptSetPrgCounter {lstName newValue} {\n\
upvar $lstName lst;\n\
set lst [lreplace $lst 0 0 [concat \"P\" $newValue]];\n\
}\n\
proc OptSelection {lst} {\n\
set res {};\n\
foreach idx [lrange [Lfirst $lst] 1 end] {\n\
lappend res [Lget $lst $idx];\n\
}\n\
return $res;\n\
}\n\
proc OptNextDesc {descName} {\n\
uplevel [list Lvarincr $descName {0 1}];\n\
}\n\
proc OptCurDesc {descriptions} {\n\
lindex $descriptions [OptGetPrgCounter $descriptions];\n\
}\n\
proc OptCurDescFinal {descriptions} {\n\
set item [OptCurDesc $descriptions];\n\
while {[OptIsPrg $item]} {\n\
set item [OptCurDesc $item];\n\
}\n\
return $item;\n\
}\n\
proc OptCurAddr {descriptions {start {}}} {\n\
set adress [OptGetPrgCounter $descriptions];\n\
lappend start $adress;\n\
set item [lindex $descriptions $adress];\n\
if {[OptIsPrg $item]} {\n\
return [OptCurAddr $item $start];\n\
} else {\n\
return $start;\n\
}\n\
}\n\
proc OptCurSetValue {descriptionsName value} {\n\
upvar $descriptionsName descriptions\n\
set adress [OptCurAddr $descriptions];\n\
lappend adress 2\n\
Lvarset descriptions $adress [list 1 $value];\n\
}\n\
proc OptState {item} {\n\
Lfirst $item\n\
}\n\
proc OptCurState {descriptions} {\n\
OptState [OptCurDesc $descriptions];\n\
}\n\
proc OptCurrentArg {lst} {\n\
Lfirst $lst;\n\
}\n\
proc OptNextArg {argsName} {\n\
uplevel [list Lvarpop $argsName];\n\
}\n\
proc OptDoAll {descriptionsName argumentsName} {\n\
upvar $descriptionsName descriptions\n\
upvar $argumentsName arguments;\n\
set state [OptCurState $descriptions];\n\
while 1 {\n\
set curitem [OptCurDesc $descriptions];\n\
while {[OptIsPrg $curitem]} {\n\
OptDoAll curitem arguments\n\
Lvarset1nc descriptions [OptGetPrgCounter $descriptions]\\\n\
$curitem;\n\
OptNextDesc descriptions;\n\
set curitem [OptCurDesc $descriptions];\n\
set state [OptCurState $descriptions];\n\
}\n\
if {[Lempty $state]} {\n\
break;\n\
}\n\
OptDoOne descriptions state arguments;\n\
OptNextDesc descriptions;\n\
set state [OptCurState $descriptions];\n\
}\n\
}\n\
proc OptDoOne {descriptionsName stateName argumentsName} {\n\
upvar $argumentsName arguments;\n\
upvar $descriptionsName descriptions;\n\
upvar $stateName state;\n\
if {($state == \"args\")} {\n\
if {![Lempty $arguments]} {\n\
OptCurSetValue descriptions $arguments;\n\
set arguments {};\n\
}\n\
return -code break;\n\
}\n\
if {[Lempty $arguments]} {\n\
if {$state == \"flags\"} {\n\
return -code return;\n\
} elseif {$state == \"optValue\"} {\n\
set state next; # not used, for debug only\n\
return ;\n\
} else {\n\
return -code error [OptMissingValue $descriptions];\n\
}\n\
} else {\n\
set arg [OptCurrentArg $arguments];\n\
}\n\
switch $state {\n\
flags {\n\
if {![OptIsFlag $arg]} {\n\
return -code return;\n\
}\n\
OptNextArg arguments;\n\
if {[string compare \"--\" $arg] == 0} {\n\
return -code return;\n\
}\n\
set hits [OptHits descriptions $arg];\n\
if {$hits > 1} {\n\
return -code error [OptAmbigous $descriptions $arg]\n\
} elseif {$hits == 0} {\n\
return -code error [OptFlagUsage $descriptions $arg]\n\
}\n\
set item [OptCurDesc $descriptions];\n\
if {[OptNeedValue $item]} {\n\
set state flagValue;\n\
} else {\n\
OptCurSetValue descriptions 1;\n\
}\n\
return -code continue;\n\
}\n\
flagValue -\n\
value {\n\
set item [OptCurDesc $descriptions];\n\
if {[catch {OptCheckType $arg\\\n\
[OptType $item] [OptTypeArgs $item]} val]} {\n\
return -code error [OptBadValue $item $arg $val]\n\
}\n\
OptNextArg arguments;\n\
OptCurSetValue descriptions $val;\n\
if {$state == \"flagValue\"} {\n\
set state flags\n\
return -code continue;\n\
} else {\n\
set state next; # not used, for debug only\n\
return ; # will go on next step\n\
}\n\
}\n\
optValue {\n\
set item [OptCurDesc $descriptions];\n\
if {![catch {OptCheckType $arg\\\n\
[OptType $item] [OptTypeArgs $item]} val]} {\n\
OptNextArg arguments;\n\
OptCurSetValue descriptions $val;\n\
}\n\
set state next; # not used, for debug only\n\
return ; # will go on next step\n\
}\n\
}\n\
return -code error \"Bug! unknown state in DoOne \\\"$state\\\"\\\n\
(prg counter [OptGetPrgCounter $descriptions]:\\\n\
[OptCurDesc $descriptions])\";\n\
}\n\
proc ::tcl::OptKeyParse {descKey arglist} {\n\
set desc [OptKeyGetDesc $descKey];\n\
if {[string compare \"-help\" [string tolower $arglist]] == 0} {\n\
return -code error [OptError \"Usage information:\" $desc 1];\n\
}\n\
OptDoAll desc arglist;\n\
if {![Lempty $arglist]} {\n\
return -code error [OptTooManyArgs $desc $arglist];\n\
}\n\
OptTreeVars $desc \"#[expr {[info level]-1}]\" ;\n\
}\n\
proc OptTreeVars {desc level {vnamesLst {}}} {\n\
foreach item $desc {\n\
if {[OptIsCounter $item]} continue;\n\
if {[OptIsPrg $item]} {\n\
set vnamesLst [OptTreeVars $item $level $vnamesLst];\n\
} else {\n\
set vname [OptVarName $item];\n\
upvar $level $vname var\n\
if {[OptHasBeenSet $item]} {\n\
lappend vnamesLst [OptName $item];\n\
set var [OptValue $item];\n\
} else {\n\
set var [OptDefaultValue $item];\n\
}\n\
}\n\
}\n\
return $vnamesLst\n\
}\n\
proc ::tcl::OptCheckType {arg type {typeArgs \"\"}} {\n\
switch -exact -- $type {\n\
int {\n\
if {![regexp {^(-+)?[0-9]+$} $arg]} {\n\
error \"not an integer\"\n\
}\n\
return $arg;\n\
}\n\
float {\n\
return [expr {double($arg)}]\n\
}\n\
script -\n\
list {\n\
if {[llength $arg]==0} {\n\
if {[OptIsFlag $arg]} {\n\
error \"no values with leading -\"\n\
}\n\
}\n\
return $arg;\n\
}\n\
boolean {\n\
if {![regexp -nocase {^(true|false|0|1)$} $arg]} {\n\
error \"non canonic boolean\"\n\
}\n\
if {$arg} {\n\
return 1\n\
} else {\n\
return 0\n\
}\n\
}\n\
choice {\n\
if {[lsearch -exact $typeArgs $arg] < 0} {\n\
error \"invalid choice\"\n\
}\n\
return $arg;\n\
}\n\
any {\n\
return $arg;\n\
}\n\
string -\n\
default {\n\
if {[OptIsFlag $arg]} {\n\
error \"no values with leading -\"\n\
}\n\
return $arg\n\
}\n\
}\n\
return neverReached;\n\
}\n\
proc OptHits {descName arg} {\n\
upvar $descName desc;\n\
set hits 0\n\
set hitems {}\n\
set i 1;\n\
set larg [string tolower $arg];\n\
set len  [string length $larg];\n\
set last [expr {$len-1}];\n\
foreach item [lrange $desc 1 end] {\n\
set flag [OptName $item]\n\
set lflag [string tolower $flag];\n\
if {$len == [string length $lflag]} {\n\
if {[string compare $larg $lflag]==0} {\n\
OptSetPrgCounter desc $i;\n\
return 1;\n\
}\n\
} else {\n\
if {[string compare $larg [string range $lflag 0 $last]]==0} {\n\
lappend hitems $i;\n\
incr hits;\n\
}\n\
}\n\
incr i;\n\
}\n\
if {$hits} {\n\
OptSetPrgCounter desc $hitems;\n\
}\n\
return $hits\n\
}\n\
proc OptName {item} {\n\
lindex $item 1;\n\
}\n\
proc OptHasBeenSet {item} {\n\
Lget $item {2 0};\n\
}\n\
proc OptValue {item} {\n\
Lget $item {2 1};\n\
}\n\
proc OptIsFlag {name} {\n\
string match \"-*\" $name;\n\
}\n\
proc OptIsOpt {name} {\n\
string match {\\?*} $name;\n\
}\n\
proc OptVarName {item} {\n\
set name [OptName $item];\n\
if {[OptIsFlag $name]} {\n\
return [string range $name 1 end];\n\
} elseif {[OptIsOpt $name]} {\n\
return [string trim $name \"?\"];\n\
} else {\n\
return $name;\n\
}\n\
}\n\
proc OptType {item} {\n\
lindex $item 3\n\
}\n\
proc OptTypeArgs {item} {\n\
lindex $item 4\n\
}\n\
proc OptHelp {item} {\n\
lindex $item 5\n\
}\n\
proc OptNeedValue {item} {\n\
string compare [OptType $item] boolflag\n\
}\n\
proc OptDefaultValue {item} {\n\
set val [OptTypeArgs $item]\n\
switch -exact -- [OptType $item] {\n\
choice {return [lindex $val 0]}\n\
boolean -\n\
boolflag {\n\
if {$val} {\n\
return 1\n\
} else {\n\
return 0\n\
}\n\
}\n\
}\n\
return $val\n\
}\n\
proc OptOptUsage {item {what \"\"}} {\n\
return -code error \"invalid description format$what: $item\\n\\\n\
should be a list of {varname|-flagname ?-type? ?defaultvalue?\\\n\
?helpstring?}\";\n\
}\n\
proc OptNewInst {state varname type typeArgs help} {\n\
list $state $varname [list 0 {}] $type $typeArgs $help;\n\
}\n\
proc OptNormalizeOne {item} {\n\
set lg [Lassign $item varname arg1 arg2 arg3];\n\
set isflag [OptIsFlag $varname];\n\
set isopt  [OptIsOpt  $varname];\n\
if {$isflag} {\n\
set state \"flags\";\n\
} elseif {$isopt} {\n\
set state \"optValue\";\n\
} elseif {[string compare $varname \"args\"]} {\n\
set state \"value\";\n\
} else {\n\
set state \"args\";\n\
}\n\
switch $lg {\n\
1 {\n\
if {$isflag} {\n\
return [OptNewInst $state $varname boolflag false \"\"];\n\
} else {\n\
return [OptNewInst $state $varname any \"\" \"\"];\n\
}\n\
}\n\
2 {\n\
set type [OptGuessType $arg1]\n\
if {[string compare $type \"string\"] == 0} {\n\
if {$isflag} {\n\
set type boolflag\n\
set def false\n\
} else {\n\
set type any\n\
set def \"\"\n\
}\n\
set help $arg1\n\
} else {\n\
set help \"\"\n\
set def $arg1\n\
}\n\
return [OptNewInst $state $varname $type $def $help];\n\
}\n\
3 {\n\
if {[regexp {^-(.+)$} $arg1 x type]} {\n\
if {$isflag || $isopt || ($type == \"choice\")} {\n\
return [OptNewInst $state $varname $type $arg2 \"\"];\n\
} else {\n\
return [OptNewInst $state $varname $type \"\" $arg2];\n\
}\n\
} else {\n\
return [OptNewInst $state $varname\\\n\
[OptGuessType $arg1] $arg1 $arg2]\n\
}\n\
}\n\
4 {\n\
if {[regexp {^-(.+)$} $arg1 x type]} {\n\
return [OptNewInst $state $varname $type $arg2 $arg3];\n\
} else {\n\
return -code error [OptOptUsage $item];\n\
}\n\
}\n\
default {\n\
return -code error [OptOptUsage $item];\n\
}\n\
}\n\
}\n\
proc OptGuessType {arg} {\n\
if {[regexp -nocase {^(true|false)$} $arg]} {\n\
return boolean\n\
}\n\
if {[regexp {^(-+)?[0-9]+$} $arg]} {\n\
return int\n\
}\n\
if {![catch {expr {double($arg)}}]} {\n\
return float\n\
}\n\
return string\n\
}\n\
proc OptAmbigous {desc arg} {\n\
OptError \"ambigous option \\\"$arg\\\", choose from:\" [OptSelection $desc]\n\
}\n\
proc OptFlagUsage {desc arg} {\n\
OptError \"bad flag \\\"$arg\\\", must be one of\" $desc;\n\
}\n\
proc OptTooManyArgs {desc arguments} {\n\
OptError \"too many arguments (unexpected argument(s): $arguments),\\\n\
usage:\"\\\n\
$desc 1\n\
}\n\
proc OptParamType {item} {\n\
if {[OptIsFlag $item]} {\n\
return \"flag\";\n\
} else {\n\
return \"parameter\";\n\
}\n\
}\n\
proc OptBadValue {item arg {err {}}} {\n\
OptError \"bad value \\\"$arg\\\" for [OptParamType $item]\"\\\n\
[list $item]\n\
}\n\
proc OptMissingValue {descriptions} {\n\
set item [OptCurDesc $descriptions];\n\
OptError \"no value given for [OptParamType $item] \\\"[OptName $item]\\\"\\\n\
(use -help for full usage) :\"\\\n\
[list $item]\n\
}\n\
proc ::tcl::OptKeyError {prefix descKey {header 0}} {\n\
OptError $prefix [OptKeyGetDesc $descKey] $header;\n\
}\n\
proc OptLengths {desc nlName tlName dlName} {\n\
upvar $nlName nl;\n\
upvar $tlName tl;\n\
upvar $dlName dl;\n\
foreach item $desc {\n\
if {[OptIsCounter $item]} continue;\n\
if {[OptIsPrg $item]} {\n\
OptLengths $item nl tl dl\n\
} else {\n\
SetMax nl [string length [OptName $item]]\n\
SetMax tl [string length [OptType $item]]\n\
set dv [OptTypeArgs $item];\n\
if {[OptState $item] != \"header\"} {\n\
set dv \"($dv)\";\n\
}\n\
set l [string length $dv];\n\
if {([OptType $item] != \"choice\") || ($l<=12)} {\n\
SetMax dl $l\n\
} else {\n\
if {![info exists dl]} {\n\
set dl 0\n\
}\n\
}\n\
}\n\
}\n\
}\n\
proc OptTree {desc nl tl dl} {\n\
set res \"\";\n\
foreach item $desc {\n\
if {[OptIsCounter $item]} continue;\n\
if {[OptIsPrg $item]} {\n\
append res [OptTree $item $nl $tl $dl];\n\
} else {\n\
set dv [OptTypeArgs $item];\n\
if {[OptState $item] != \"header\"} {\n\
set dv \"($dv)\";\n\
}\n\
append res [format \"\\n    %-*s %-*s %-*s %s\" \\\n\
$nl [OptName $item] $tl [OptType $item] \\\n\
$dl $dv [OptHelp $item]]\n\
}\n\
}\n\
return $res;\n\
}\n\
proc ::tcl::OptError {prefix desc {header 0}} {\n\
if {$header} {\n\
set h [list [OptNewInst header Var/FlagName Type Value Help]];\n\
lappend h   [OptNewInst header ------------ ---- ----- ----];\n\
lappend h   [OptNewInst header {( -help} \"\" \"\" {gives this help )}]\n\
set desc [concat $h $desc]\n\
}\n\
OptLengths $desc nl tl dl\n\
return \"$prefix[OptTree $desc $nl $tl $dl]\"\n\
}\n\
proc ::tcl::Lempty {list} {\n\
expr {[llength $list]==0}\n\
}\n\
proc ::tcl::Lget {list indexLst} {\n\
if {[llength $indexLst] <= 1} {\n\
return [lindex $list $indexLst];\n\
}\n\
Lget [lindex $list [Lfirst $indexLst]] [Lrest $indexLst];\n\
}\n\
proc ::tcl::Lvarset {listName indexLst newValue} {\n\
upvar $listName list;\n\
if {[llength $indexLst] <= 1} {\n\
Lvarset1nc list $indexLst $newValue;\n\
} else {\n\
set idx [Lfirst $indexLst];\n\
set targetList [lindex $list $idx];\n\
Lvarset targetList [Lrest $indexLst] $newValue;\n\
Lvarset1nc list $idx $targetList;\n\
}\n\
}\n\
variable emptyList {}\n\
proc ::tcl::Lvarset1 {listName index newValue} {\n\
upvar $listName list;\n\
if {$index < 0} {return -code error \"invalid negative index\"}\n\
set lg [llength $list];\n\
if {$index >= $lg} {\n\
variable emptyList;\n\
for {set i $lg} {$i<$index} {incr i} {\n\
lappend list $emptyList;\n\
}\n\
lappend list $newValue;\n\
} else {\n\
set list [lreplace $list $index $index $newValue];\n\
}\n\
}\n\
proc ::tcl::Lvarset1nc {listName index newValue} {\n\
upvar $listName list;\n\
set list [lreplace $list $index $index $newValue];\n\
}\n\
proc ::tcl::Lvarincr {listName indexLst {howMuch 1}} {\n\
upvar $listName list;\n\
if {[llength $indexLst] <= 1} {\n\
Lvarincr1 list $indexLst $howMuch;\n\
} else {\n\
set idx [Lfirst $indexLst];\n\
set targetList [lindex $list $idx];\n\
Lvarset1nc list $idx {};\n\
Lvarincr targetList [Lrest $indexLst] $howMuch;\n\
Lvarset1nc list $idx $targetList;\n\
}\n\
}\n\
proc ::tcl::Lvarincr1 {listName index {howMuch 1}} {\n\
upvar $listName list;\n\
set newValue [expr {[lindex $list $index]+$howMuch}];\n\
set list [lreplace $list $index $index $newValue];\n\
return $newValue;\n\
}\n\
proc ::tcl::Lfirst {list} {\n\
lindex $list 0\n\
}\n\
proc ::tcl::Lrest {list} {\n\
lrange $list 1 end\n\
}\n\
proc ::tcl::Lvarpop {listName} {\n\
upvar $listName list;\n\
set list [lrange $list 1 end];\n\
}\n\
proc ::tcl::Lvarpop2 {listName} {\n\
upvar $listName list;\n\
set el [Lfirst $list];\n\
set list [lrange $list 1 end];\n\
return $el;\n\
}\n\
proc ::tcl::Lassign {list args} {\n\
set i 0;\n\
set lg [llength $list];\n\
foreach vname $args {\n\
if {$i>=$lg} break\n\
uplevel [list set $vname [lindex $list $i]];\n\
incr i;\n\
}\n\
return $lg;\n\
}\n\
proc ::tcl::SetMax {varname value} {\n\
upvar 1 $varname var\n\
if {![info exists var] || $value > $var} {\n\
set var $value\n\
}\n\
}\n\
proc ::tcl::SetMin {varname value} {\n\
upvar 1 $varname var\n\
if {![info exists var] || $value < $var} {\n\
set var $value\n\
}\n\
}\n\
OptCreateTestProc\n\
rename OptCreateTestProc {}\n\
}\n\
";

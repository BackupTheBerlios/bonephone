/* This file is automatically generated by tcl2c, do NOT edit by hand! */
char lib_http[] = "\
package provide http 2.0	;# This uses Tcl namespaces\n\
namespace eval http {\n\
variable http\n\
array set http {\n\
-accept */*\n\
-proxyhost {}\n\
-proxyport {}\n\
-useragent {Tcl http client package 2.0}\n\
-proxyfilter http::ProxyRequired\n\
}\n\
variable formMap\n\
set alphanumeric	a-zA-Z0-9\n\
for {set i 1} {$i <= 256} {incr i} {\n\
set c [format %c $i]\n\
if {![string match \\[$alphanumeric\\] $c]} {\n\
set formMap($c) %[format %.2x $i]\n\
}\n\
}\n\
array set formMap {\n\
\" \" +   \\n %0d%0a\n\
}\n\
namespace export geturl config reset wait formatQuery \n\
}\n\
proc http::config {args} {\n\
variable http\n\
set options [lsort [array names http -*]]\n\
set usage [join $options \", \"]\n\
if {[llength $args] == 0} {\n\
set result {}\n\
foreach name $options {\n\
lappend result $name $http($name)\n\
}\n\
return $result\n\
}\n\
regsub -all -- - $options {} options\n\
set pat ^-([join $options |])$\n\
if {[llength $args] == 1} {\n\
set flag [lindex $args 0]\n\
if {[regexp -- $pat $flag]} {\n\
return $http($flag)\n\
} else {\n\
return -code error \"Unknown option $flag, must be: $usage\"\n\
}\n\
} else {\n\
foreach {flag value} $args {\n\
if {[regexp -- $pat $flag]} {\n\
set http($flag) $value\n\
} else {\n\
return -code error \"Unknown option $flag, must be: $usage\"\n\
}\n\
}\n\
}\n\
}\n\
proc http::Finish { token {errormsg \"\"} } {\n\
variable $token\n\
upvar 0 $token state\n\
global errorInfo errorCode\n\
if {[string length $errormsg] != 0} {\n\
set state(error) [list $errormsg $errorInfo $errorCode]\n\
set state(status) error\n\
}\n\
catch {close $state(sock)}\n\
catch {after cancel $state(after)}\n\
if {[info exists state(-command)]} {\n\
if {[catch {eval $state(-command) {$token}} err]} {\n\
if {[string length $errormsg] == 0} {\n\
set state(error) [list $err $errorInfo $errorCode]\n\
set state(status) error\n\
}\n\
}\n\
unset state(-command)\n\
}\n\
}\n\
proc http::reset { token {why reset} } {\n\
variable $token\n\
upvar 0 $token state\n\
set state(status) $why\n\
catch {fileevent $state(sock) readable {}}\n\
Finish $token\n\
if {[info exists state(error)]} {\n\
set errorlist $state(error)\n\
unset state(error)\n\
eval error $errorlist\n\
}\n\
}\n\
proc http::geturl { url args } {\n\
variable http\n\
if {![info exists http(uid)]} {\n\
set http(uid) 0\n\
}\n\
set token [namespace current]::[incr http(uid)]\n\
variable $token\n\
upvar 0 $token state\n\
reset $token\n\
array set state {\n\
-blocksize 	8192\n\
-validate 	0\n\
-headers 	{}\n\
-timeout 	0\n\
state		header\n\
meta		{}\n\
currentsize	0\n\
totalsize	0\n\
type            text/html\n\
body            {}\n\
status		\"\"\n\
}\n\
set options {-blocksize -channel -command -handler -headers \\\n\
-progress -query -validate -timeout}\n\
set usage [join $options \", \"]\n\
regsub -all -- - $options {} options\n\
set pat ^-([join $options |])$\n\
foreach {flag value} $args {\n\
if {[regexp $pat $flag]} {\n\
if {[info exists state($flag)] && \\\n\
[regexp {^[0-9]+$} $state($flag)] && \\\n\
![regexp {^[0-9]+$} $value]} {\n\
return -code error \"Bad value for $flag ($value), must be integer\"\n\
}\n\
set state($flag) $value\n\
} else {\n\
return -code error \"Unknown option $flag, can be: $usage\"\n\
}\n\
}\n\
if {! [regexp -nocase {^(http://)?([^/:]+)(:([0-9]+))?(/.*)?$} $url \\\n\
x proto host y port srvurl]} {\n\
error \"Unsupported URL: $url\"\n\
}\n\
if {[string length $port] == 0} {\n\
set port 80\n\
}\n\
if {[string length $srvurl] == 0} {\n\
set srvurl /\n\
}\n\
if {[string length $proto] == 0} {\n\
set url http://$url\n\
}\n\
set state(url) $url\n\
if {![catch {$http(-proxyfilter) $host} proxy]} {\n\
set phost [lindex $proxy 0]\n\
set pport [lindex $proxy 1]\n\
}\n\
if {$state(-timeout) > 0} {\n\
set state(after) [after $state(-timeout) [list http::reset $token timeout]]\n\
}\n\
if {[info exists phost] && [string length $phost]} {\n\
set srvurl $url\n\
set s [socket $phost $pport]\n\
} else {\n\
set s [socket $host $port]\n\
}\n\
set state(sock) $s\n\
fconfigure $s -translation {auto crlf} -buffersize $state(-blocksize)\n\
catch {fconfigure $s -blocking off}\n\
set len 0\n\
set how GET\n\
if {[info exists state(-query)]} {\n\
set len [string length $state(-query)]\n\
if {$len > 0} {\n\
set how POST\n\
}\n\
} elseif {$state(-validate)} {\n\
set how HEAD\n\
}\n\
puts $s \"$how $srvurl HTTP/1.0\"\n\
puts $s \"Accept: $http(-accept)\"\n\
puts $s \"Host: $host\"\n\
puts $s \"User-Agent: $http(-useragent)\"\n\
foreach {key value} $state(-headers) {\n\
regsub -all \\[\\n\\r\\]  $value {} value\n\
set key [string trim $key]\n\
if {[string length $key]} {\n\
puts $s \"$key: $value\"\n\
}\n\
}\n\
if {$len > 0} {\n\
puts $s \"Content-Length: $len\"\n\
puts $s \"Content-Type: application/x-www-form-urlencoded\"\n\
puts $s \"\"\n\
fconfigure $s -translation {auto binary}\n\
puts $s $state(-query)\n\
} else {\n\
puts $s \"\"\n\
}\n\
flush $s\n\
fileevent $s readable [list http::Event $token]\n\
if {! [info exists state(-command)]} {\n\
wait $token\n\
}\n\
return $token\n\
}\n\
proc http::data {token} {\n\
variable $token\n\
upvar 0 $token state\n\
return $state(body)\n\
}\n\
proc http::status {token} {\n\
variable $token\n\
upvar 0 $token state\n\
return $state(status)\n\
}\n\
proc http::code {token} {\n\
variable $token\n\
upvar 0 $token state\n\
return $state(http)\n\
}\n\
proc http::size {token} {\n\
variable $token\n\
upvar 0 $token state\n\
return $state(currentsize)\n\
}\n\
proc http::Event {token} {\n\
variable $token\n\
upvar 0 $token state\n\
set s $state(sock)\n\
if {[::eof $s]} {\n\
Eof $token\n\
return\n\
}\n\
if {$state(state) == \"header\"} {\n\
set n [gets $s line]\n\
if {$n == 0} {\n\
set state(state) body\n\
if {![regexp -nocase ^text $state(type)]} {\n\
fconfigure $s -translation binary\n\
if {[info exists state(-channel)]} {\n\
fconfigure $state(-channel) -translation binary\n\
}\n\
}\n\
if {[info exists state(-channel)] &&\n\
![info exists state(-handler)]} {\n\
fileevent $s readable {}\n\
CopyStart $s $token\n\
}\n\
} elseif {$n > 0} {\n\
if {[regexp -nocase {^content-type:(.+)$} $line x type]} {\n\
set state(type) [string trim $type]\n\
}\n\
if {[regexp -nocase {^content-length:(.+)$} $line x length]} {\n\
set state(totalsize) [string trim $length]\n\
}\n\
if {[regexp -nocase {^([^:]+):(.+)$} $line x key value]} {\n\
lappend state(meta) $key $value\n\
} elseif {[regexp ^HTTP $line]} {\n\
set state(http) $line\n\
}\n\
}\n\
} else {\n\
if {[catch {\n\
if {[info exists state(-handler)]} {\n\
set n [eval $state(-handler) {$s $token}]\n\
} else {\n\
set block [read $s $state(-blocksize)]\n\
set n [string length $block]\n\
if {$n >= 0} {\n\
append state(body) $block\n\
}\n\
}\n\
if {$n >= 0} {\n\
incr state(currentsize) $n\n\
}\n\
} err]} {\n\
Finish $token $err\n\
} else {\n\
if {[info exists state(-progress)]} {\n\
eval $state(-progress) {$token $state(totalsize) $state(currentsize)}\n\
}\n\
}\n\
}\n\
}\n\
proc http::CopyStart {s token} {\n\
variable $token\n\
upvar 0 $token state\n\
if {[catch {\n\
fcopy $s $state(-channel) -size $state(-blocksize) -command \\\n\
[list http::CopyDone $token]\n\
} err]} {\n\
Finish $token $err\n\
}\n\
}\n\
proc http::CopyDone {token count {error {}}} {\n\
variable $token\n\
upvar 0 $token state\n\
set s $state(sock)\n\
incr state(currentsize) $count\n\
if {[info exists state(-progress)]} {\n\
eval $state(-progress) {$token $state(totalsize) $state(currentsize)}\n\
}\n\
if {([string length $error] != 0)} {\n\
Finish $token $error\n\
} elseif {[::eof $s]} {\n\
Eof $token\n\
} else {\n\
CopyStart $s $token\n\
}\n\
}\n\
proc http::Eof {token} {\n\
variable $token\n\
upvar 0 $token state\n\
if {$state(state) == \"header\"} {\n\
set state(status) eof\n\
} else {\n\
set state(status) ok\n\
}\n\
set state(state) eof\n\
Finish $token\n\
}\n\
proc http::wait {token} {\n\
variable $token\n\
upvar 0 $token state\n\
if {![info exists state(status)] || [string length $state(status)] == 0} {\n\
vwait $token\\(status)\n\
}\n\
if {[info exists state(error)]} {\n\
set errorlist $state(error)\n\
unset state(error)\n\
eval error $errorlist\n\
}\n\
return $state(status)\n\
}\n\
proc http::formatQuery {args} {\n\
set result \"\"\n\
set sep \"\"\n\
foreach i $args {\n\
append result  $sep [mapReply $i]\n\
if {$sep != \"=\"} {\n\
set sep =\n\
} else {\n\
set sep &\n\
}\n\
}\n\
return $result\n\
}\n\
proc http::mapReply {string} {\n\
variable formMap\n\
set alphanumeric	a-zA-Z0-9\n\
regsub -all \\[^$alphanumeric\\] $string {$formMap(&)} string\n\
regsub -all \\n $string {\\\\n} string\n\
regsub -all \\t $string {\\\\t} string\n\
regsub -all {[][{})\\\\]\\)} $string {\\\\&} string\n\
return [subst $string]\n\
}\n\
proc http::ProxyRequired {host} {\n\
variable http\n\
if {[info exists http(-proxyhost)] && [string length $http(-proxyhost)]} {\n\
if {![info exists http(-proxyport)] || ![string length $http(-proxyport)]} {\n\
set http(-proxyport) 8080\n\
}\n\
return [list $http(-proxyhost) $http(-proxyport)]\n\
} else {\n\
return {}\n\
}\n\
}\n\
";

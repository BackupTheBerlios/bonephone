#
# Please download the xerces parser from xml.apache.org for this to build.
#


ROOT=../
include $(ROOT)/build-config

all:	
	cd traceviewer ; $(MAKE) all
	$(JDK_HOME)/bin/jar cvf traceviewer.jar traceviewer/*.class
	
	cd traceviewerapp ; $(MAKE) all
	cd responder; $(MAKE) all


tests: all

clean: emacsclean
	cd traceviewerapp; $(MAKE) clean
	cd responder; $(MAKE) clean
	cd traceviewer; $(MAKE) clean
	\rm -f *.*~
	\rm -f *.class
	\rm -f *.bak*
	\rm -f *.jar

traceviewerlib:
	
	cd traceviewer ; $(MAKE) all
	$(JDK_HOME)/bin/jar cvf traceviewer.jar traceviewer/*.class	
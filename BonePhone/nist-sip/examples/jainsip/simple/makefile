# Please download the xerces parser from xml.apache.org for this to build.
#


ROOT=../../../
include $(ROOT)/build-config


clean: emacsclean
	\rm -f *.class
	\rm -f shoot.exe
	\rm -f shoot
	\rm -f myserverlog.txt

all:	
	$(JAVAC) *.java

test:
	$(JAVA) -Djava.security.policy=test.policy examples.jainsip.simple.Example

test1:
	$(JAVA) examples.jainsip.simple.Main

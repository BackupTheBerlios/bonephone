#
# Please download the xerces parser from xml.apache.org for this to build.
#



ROOT=../../
include $(ROOT)/build-config

XML_PARSER="$(XERCES_HOME)/xerces.jar"

ifeq (${OS},Windows_NT)
	JAVAC=$(JDK_HOME)/bin/javac -classpath "$(JDK_HOME);$(XML_PARSER);$(ROOT);$(ANTLRPATH);$(ROOT);../"
	JAVA=$(JDK_HOME)/bin/java -classpath "$(JDK_HOME);$(XML_PARSER);$(ROOT);$(ANTLRPATH);$(ROOT);../"
else
	JAVAC=$(JDK_HOME)/bin/javac  -deprecation -classpath "$(JDK_HOME):$(XML_PARSER):$(ROOT):$(ANTLRPATH):../"
	JAVA=$(JDK_HOME)/bin/java -classpath "$(JDK_HOME):$(XML_PARSER):$(ROOT):$(ANTLRPATH):../"
endif


all:	
	$(JAVAC) *.java

tests: all
	\rm  -f testout.xml
	$(JAVA) examples.torture.Torture torture.xml

clean: emacsclean
	\rm -f testout.xml
	\rm -f *.class



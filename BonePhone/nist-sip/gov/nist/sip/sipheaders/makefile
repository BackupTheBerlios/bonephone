# this works with the gnu make tool. If you are working with windows, please install cygwin to get
# gnumake (goto http://www.sourceware.cygnus.com).

ROOT=../../../../
#override settings of makefile if needed
-include  $(ROOT)/build-config

# use different delimiters for windows and linux (windows takes semicolons)
ifeq (${OS},Windows_NT)
	JAVAC=$(JDK_HOME)/bin/javac -classpath "$(JDK_HOME);$(ANTLRPATH);$(ROOT)"
	JAVA=$(JDK_HOME)/bin/java -classpath "$(JDK_HOME);$(ANTLRPATH);$(ROOT)"
else
	JAVAC=$(JDK_HOME)/bin/javac -classpath "$(JDK_HOME):$(ANTLRPATH):$(ROOT)"
	JAVA=$(JDK_HOME)/bin/java -classpath "$(JDK_HOME):$(ANTLRPATH):$(ROOT)"
endif

#uncomment for copious amounts of parser tracing.
# ANTLR_FLAGS =  -trace 


%.class: %.java
	$(JAVAC) $<


clean:
	\rm -f *.class

all: 
	$(JAVAC) *.java

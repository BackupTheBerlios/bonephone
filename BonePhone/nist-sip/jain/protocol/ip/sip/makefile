
# this works with the gnu make tool. If you are working with windows, please install cygwin to get
# gnumake (goto http://www.sourceware.cygnus.com).

ROOT=../../../../
ifeq (${OS},Windows_NT) 
	JDK_HOME=c:/jdk1.3
else
	JDK_HOME=/usr/local/jdk1.3
endif

-include  $(ROOT)/build-config

# use different delimiters for windows and linux (windows takes semicolons)
ifeq (${OS},Windows_NT)
	JAVAC=$(JDK_HOME)/bin/javac -classpath "$(JDK_HOME);$(ROOT)"
	JAVA=$(JDK_HOME)/bin/java -classpath "$(JDK_HOME);$(ROOT)"
else
	JAVAC=$(JDK_HOME)/bin/javac -classpath "$(JDK_HOME):$(ROOT)"
	JAVA=$(JDK_HOME)/bin/java -classpath "$(JDK_HOME):$(ROOT)"
endif



%.class: %.java
	$(JAVAC) $<


all: 
	$(JAVAC) *.java

clean: emacsclean
	\rm -f *.class


# this works with the gnu make tool
ROOT=../../../../../
# override these settings if needed.
-include $(ROOT)/build-config


ifeq (${OS},Windows_NT)
	JAVAC=$(JDK_HOME)/bin/javac -classpath "$(JDK_HOME);$(ROOT)" $(JAVA_FLAGS)
	JAVA=$(JDK_HOME)/bin/java -classpath "$(JDK_HOME);$(ROOT)" $(JAVA_FLAGS)
else
	JAVAC=$(JDK_HOME)/bin/javac -classpath "$(JDK_HOME):$(ROOT)" $(JAVA_FLAGS)
	JAVA=$(JDK_HOME)/bin/java -classpath "$(JDK_HOME):$(ROOT)"  $(JAVA_FLAGS) 
endif

default: all

%.class: %.java
	$(JAVAC) $<

all:
	$(JAVAC) *.java

clean: emacsclean
	rm -f *.class


JAVAFILES = $(wildcard *.java)
CLASSFILES = $(subst .java,.class,$(JAVAFILES))
JARFILE = ../SipUserAgent.jar

all: $(JARFILE)

$(JARFILE): $(CLASSFILES)
	cd .. ; $(JAR) cvf SipUserAgent.jar SipUserAgent/*.class

%.class: %.java
	echo $(CLASSPATH)
	$(JAVAC) $<

clean: 
	rm -f *.class

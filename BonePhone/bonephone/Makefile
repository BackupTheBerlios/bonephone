JAVAFILES = $(wildcard *.java)
CLASSFILES = $(subst .java,.class,$(JAVAFILES))
JARFILE = ../bonephone.jar

all: $(JARFILE)

$(JARFILE): $(CLASSFILES)
	cd .. ; $(JAR) cvf bonephone.jar bonephone/*.class

%.class: %.java
	$(JAVAC) $<

clean: 
	rm -f *.class


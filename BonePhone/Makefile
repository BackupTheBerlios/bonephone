JHOME = $(HOME)/jdk1.4
PJH   = $(HOME)/Studienarbeit/PJ/osip/berlios/bonephone


#######################################################################
# There are no changes necessary below when moving to another system
# with different home and/or java installation location

RATMEDIA=rat-4.2.20/rat/rat-4.2.20-media

CLASSPATH:=${JHOME}/jre/lib
CLASSPATH:=${CLASSPATH}:${PJH}/mbus.jar
CLASSPATH:=${CLASSPATH}:${PJH}/nist-sip.jar
CLASSPATH:=${CLASSPATH}:${PJH}/antlrall.jar
CLASSPATH:=${CLASSPATH}:${PJH}/SipUserAgent.jar
CLASSPATH:=${CLASSPATH}:${PJH}

RUNPATH:=${JHOME}/jre/lib
RUNPATH:=${RUNPATH}:${PJH}/mbus.jar
RUNPATH:=${RUNPATH}:${PJH}/nist-sip.jar
RUNPATH:=${RUNPATH}:${PJH}/antlrall.jar
RUNPATH:=${RUNPATH}:${PJH}/bonephone.jar
RUNPATH:=${RUNPATH}:${PJH}/SipUserAgent.jar


JAVAC = $(JHOME)/bin/javac
JAVA  = $(JHOME)/bin/java
JAR   = $(JHOME)/bin/jar
JDOC  = $(JHOME)/bin/javadoc

export CLASSPATH JHOME JAVAC JAVA JAR RUNPATH JDOC


all: boneph 

doc: javadoc

dis: 
	./makedis

javadoc: 
	$(JDOC) -author -classpath ${CLASSPATH} -d bpdoc.html -windowtitle "BonePhone 0.8.x JavaDoc" bonephone
	tar cvzf bpjdoc.tgz bpdoc.html

sipua: nistsip
	make -C SipUserAgent

mbus:
	make -C mbus.jfi
	cp mbus.jfi/mbus.jar .

boneph: rat mbus nistsip sipua
	make -C bonephone

rat: $(RATMEDIA)
	cp $(RATMEDIA) .

nistsip: nist-sip.jar

nist-sip.jar: nist-sip/nist-sip.jar
	cp nist-sip/nist-sip.jar nist-sip/lib/antlr/antlrall.jar .

nist-sip/nist-sip.jar: 
	make -C nist-sip

run: 
	$(JAVA) -classpath $(RUNPATH) bonephone.main

$(RATMEDIA): 
	@echo ""
	@echo "please read README.compile to generate a rat media engine. !!!"
	@echo ""
	@exit -1

clean:
	rm -f *.jar
	rm -f rat-4.2.20-media
	make -C mbus.jfi clean
	make -C SipUserAgent clean
	make -C bonephone clean
	make -C nist-sip clean


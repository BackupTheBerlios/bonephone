
ROOT=../../../../

include $(ROOT)/build-config


all: 
	$(JAVAC) *.java
	$(RMIC) -classpath $(ROOT) -d $(ROOT)  gov.nist.sip.stack.MessageLogTableImpl

clean: emacsclean
	\rm -f *.class


# this works with the gnu make tool


ROOT=../../

include $(ROOT)/build-config

XML_PARSER="$(XERCES_HOME)/xerces.jar"
JYTHON="$(JYTHON_HOME)/jython.jar"
WANT_JMF=0

ifeq ($(WANT_JMF),1)
ifeq (${OS},Windows_NT)
	JMF="$(JMF_HOME)/lib/jmf.jar;$(JMF_HOME)/lib/sound.jar;$(JMF_HOME)/lib/mediaplayer.jar;$(JMF_HOME)/lib/multiplayer.jar;$(JMF_HOME)/lib"
else
	JMF="$(JMF_HOME)/lib/jmf.jar:$(JMF_HOME)/lib/sound.jar:$(JMF_HOME)/lib/mediaplayer.jar:$(JMF_HOME)/lib/multiplayer.jar:$(JMF_HOME)/lib"
endif
endif

ifeq (${OS},Windows_NT)
	JAVAC=$(JDK_HOME)/bin/javac -classpath "$(JDK_HOME);$(XML_PARSER);$(ROOT);$(ANTLRPATH);$(ROOT);$(JYTHON);../"
	JAVA=$(JDK_HOME)/bin/java -classpath "$(JDK_HOME);$(XML_PARSER);$(ROOT);$(ANTLRPATH);$(ROOT);$(JYTHON);../;./"
else
# Make sure LD_LIBRARY_PATH includes $(JMF_HOME)/lib
	JAVAC=$(JDK_HOME)/bin/javac  -deprecation -classpath "$(JDK_HOME):$(XML_PARSER):$(ROOT):$(ANTLRPATH):$(JYTHON):$(JMF)"
	JAVA=$(JDK_HOME)/bin/java -classpath "$(JDK_HOME):$(XML_PARSER):$(ROOT):$(ANTLRPATH):$(JYTHON):$(JMF)"
endif

classes: Agent.class \
	Attr.class \
	Expect.class \
	TagNames.class \
	CallFlow.class \
	GeneratedMessage.class \
	Transaction.class \
	Debug.class \
	JythonInterp.class \
	Trigger.class \
	DeferredSend.class   \
	MessageFactoryImpl.class   \
	XMLContentHandler.class \
	EventEngine.class   \
	MessageTemplate.class


%.class: %.java
	$(JAVAC) $<

all: classes	
ifeq ($(WANT_JMF),1)
	$(JAVAC) PlayerRTP.java
endif
	

clean: emacsclean
	\rm -f *.class
	\rm -f *~
	\rm -f *.log


# UAS Sections :

uas:
	\rm -f uas.log
	$(JAVA) tools.responder.EventEngine -eventSpec uas.xml  -agentSpec agents.xml -enableUDP 5070 -stackAddress 127.0.0.1 -stackName antd.nist.gov -peerAddress 127.0.0.1:2000/UDP -logFile uas.log -traceLevel 32
	
uas_busy:
	\rm -f uas_busy.log
	$(JAVA) tools.responder.EventEngine -eventSpec uas_busy.xml  -agentSpec agents.xml -enableUDP 5070 -stackAddress 127.0.0.1 -stackName antd.nist.gov -peerAddress 127.0.0.1:2000/UDP -logFile uas_busy.log -traceLevel 32	
	
uas_numb:
	\rm -f uas_numb.log
	$(JAVA) tools.responder.EventEngine -eventSpec uas_numb.xml  -agentSpec agents.xml -enableUDP 5070 -stackAddress 127.0.0.1 -stackName antd.nist.gov -peerAddress 127.0.0.1:2000/UDP -logFile uas_numb.log -traceLevel 32		

uas_unsuccessful_invite:
	\rm -f uas_unsuccessful_invite.log
	$(JAVA) tools.responder.EventEngine     \
	-eventSpec uas_unsuccesful_invite.xml   \
	-agentSpec agents.xml -enableUDP 5070   \
	-stackAddress 127.0.0.1 		\
	-stackName antd.nist.gov 		\
	-peerAddress 127.0.0.1:2000/UDP 	\
	-logFile uas_unsuccessful_invite.log -traceLevel 32

uas_numb_cancel:
	\rm -f uas_numb_cancel.log
	$(JAVA) tools.responder.EventEngine \
	-eventSpec  uas_numb_cancel.xml \
	-agentSpec agents.xml  		\
	-enableUDP 5070 		\
	-stackAddress 127.0.0.1 	\
	-stackName antd.nist.gov 	\
	-peerAddress 127.0.0.1:2000/UDP \
	-logFile uas_numb_cancel.log -traceLevel 32


# UAC sections :

uac:
	\rm -f uac.log
	$(JAVA) tools.responder.EventEngine -eventSpec uac.xml -enableUDP 5071 -stackAddress 127.0.0.1 -stackName antd.nist.gov -logFile uac.log -peerAddress 127.0.0.1:2000/UDP -agentSpec agents.xml -traceLevel 32

uac1:
	\rm -f uac.log
	$(JAVA) tools.responder.EventEngine -eventSpec uac.xml -enableUDP 5071 -stackAddress 127.0.0.1 -stackName antd.nist.gov -logFile uac.log -peerAddress 127.0.0.1:2000/UDP -agentSpec agents1.xml -traceLevel 32

uac_send_cancel:
	\rm -f uac_send_cancel.log
	$(JAVA) tools.responder.EventEngine -eventSpec uac_send_cancel.xml \
	-enableUDP 5071 		\
	-stackAddress 127.0.0.1 	\
	-stackName antd.nist.gov 	\
	-logFile uac_send_cancel.log 	\
	-peerAddress 127.0.0.1:2000/UDP \
	-agentSpec agents.xml -traceLevel 32



ROOT=../../../
include $(ROOT)/build-config


clean: emacsclean
	\rm -f *.class

all: Shootist.class

shootme: Shootist.class
	$(JAVA) examples.jainsip.shootist.Shootist message_simple.txt



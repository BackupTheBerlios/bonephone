
ROOT=../../

include $(ROOT)/build-config

default: all tests


%.class: %.java
	$(JAVAC) $<

all:	NewHeaderTest.class 	\
	SDPParserTest.class 	\
       	SIPURLParserTest.class 	\
	PipelinedParserTest.class \
	SIPHeaderParserTest.class \
	StringParserTest.class    \
	MatchTest.class    	  \
	EditHeaderTest.class	

clean: emacsclean
	\rm -f *.class


extensionparse: NewHeaderTest.class
	$(JAVA) examples.msgparser.NewHeaderTest NewHeaderTestInput.txt

urlparse: SIPURLParserTest.class
	$(JAVA) examples.msgparser.SIPURLParserTest SIPURLTestInput.txt

stringparse: StringParserTest.class
	$(JAVA) examples.msgparser.StringParserTest 

headerparse: SIPHeaderParserTest.class  
	$(JAVA) examples.msgparser.SIPHeaderParserTest SIPHeaders.txt

sdpparse: SDPParserTest.class
	$(JAVA) examples.msgparser.SDPParserTest SDPParserTestInput.txt

badheaders: StringParserTest.class
	$(JAVA) examples.msgparser.StringParserTest badheaders.txt

editheader: EditHeaderTest.class
	$(JAVA) examples.msgparser.EditHeaderTest 

matchtest: MatchTest.class
	$(JAVA) examples.msgparser.MatchTest

pipeline: PipelinedParserTest.class
	$(JAVA) examples.msgparser.PipelinedParserTest
	

tests: extensionparse urlparse stringparse headerparse badheaders editheader matchtest

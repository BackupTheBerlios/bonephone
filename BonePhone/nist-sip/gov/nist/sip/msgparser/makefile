# This works with the gnu make tool. 
# If you are working with windows, please install cygwin to get
# gnumake (goto http://www.sourceware.cygnus.com).

ROOT=../../../../
include  $(ROOT)/build-config
DOCDIR=$(ROOT)/docs/api

ANTLRTOOL=$(JAVA) antlr.Tool  
# ANTLR_FLAGS = -trace


%.java:  %.g
	$(ANTLRTOOL) $(ANTLR_FLAGS)  -glib charLexer.g $<

%.class: %.java
	$(JAVAC) $<


charLexer.java: charLexer.g
hide_Lexer.java: sdp_announceParserTokenTypes.txt charLexer.g
via_parmsLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
retry_afterLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
authorization_Lexer.java: sdp_announceParserTokenTypes.txt charLexer.g
authentication_Lexer.java: sdp_announceParserTokenTypes.txt charLexer.g
priority_Lexer.java: sdp_announceParserTokenTypes.txt charLexer.g
pgp_Lexer.java: sdp_announceParserTokenTypes.txt charLexer.g
basic_Lexer.java: sdp_announceParserTokenTypes.txt charLexer.g
digest_Lexer.java: sdp_announceParserTokenTypes.txt charLexer.g
timeLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
dateLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
contact_parmsLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
accept_languageLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
addr_parmsLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
sip_urlbodyLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
command_keywordLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
status_lineLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
method_keywordLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
sip_urlLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
sdpLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
url_parmsLexer.java: sdp_announceParserTokenTypes.txt charLexer.g
transport_Lexer.java: sdp_announceParserTokenTypes.txt charLexer.g
call_infoLexer.java: sdp_announceParserTokenTypes.txt call_infoLexer.g
skipLexer.java: skipLexer.g charLexer.g
content_dispositionLexer.java: sdp_announceParserTokenTypes.txt content_dispositionLexer.g
tel_urlLexer.java: sdp_announceParserTokenTypes.txt tel_urlLexer.g

charLexer.java: sdp_announceParserTokenTypes.txt 
	$(ANTLRTOOL) $(ANTLR_FLAGS) charLexer.g

parser_utilParserTokenTypes.txt parser_utilParser.java:  parser_utilParser.g
	$(ANTLRTOOL) $(ANTLR_FLAGS) parser_utilParser.g


# The following is to fool make to use the default rule for java file

# parser_utilParserTokenTypes.txt: parser_utilParser.java

host_nameParserTokenTypes.txt host_nameParser.java: host_nameParser.g \
	parser_utilParserTokenTypes.txt 
	$(ANTLRTOOL) $(ANTLR_FLAGS) \
	-glib parser_utilParser.g host_nameParser.g

# host_nameParserTokenTypes.txt: host_nameParser.java

sip_urlParserTokenTypes.txt sip_urlParser.java : sip_urlParser.g \
	parser_utilParserTokenTypes.txt host_nameParserTokenTypes.txt \
	tel_ParserTokenTypes.txt 
	$(ANTLRTOOL) $(ANTLR_FLAGS) 			\
	-glib parser_utilParser.g\;host_nameParser.g  	\
	sip_urlParser.g

tel_ParserTokenTypes.txt tel_Parser.java: tel_Parser.g 
	$(ANTLRTOOL) $(ANTLR_FLAGS) 			\
	-glib parser_utilParser.g\;host_nameParser.g  	\
	tel_Parser.g

sip_messageParserTokenTypes.txt sip_messageParser.java: sip_messageParser.g  \
		sip_urlParserTokenTypes.txt
	$(ANTLRTOOL) $(ANTLR_FLAGS) 					\
	-glib sip_urlParser.g\;parser_utilParser.g\;host_nameParser.g  	\
	sip_messageParser.g

# sip_messageParserTokenTypes.txt: sip_messageParser.java

sdp_announceParserTokenTypes.txt sdp_announceParser.java: 		\
	sdp_announceParser.g  sip_urlParser.g  host_nameParser.g 	\
	parser_utilParser.g sip_messageParserTokenTypes.txt
	$(ANTLRTOOL) $(ANTLR_FLAGS) 					\
	-glib sip_urlParser.g\;host_nameParser.g\;parser_utilParser.g 	\
	sdp_announceParser.g

# sdp_announceParserTokenTypes.txt : sdp_announceParser.g host_

clean:
	\rm -f *.class
	\rm -f  *.txt 
	\rm -f  *~
	\rm -f  expanded*.g
	\rm -f basic_LexerTokenTypes.java  
	\rm -f digest_LexerTokenTypes.java  
	\rm -f accept_languageLexerTokenTypes.java  
	\rm -f retry_afterLexerTokenTypes.java
	\rm -f addr_parmsLexerTokenTypes.java       
	\rm -f sdpLexerTokenTypes.java
	\rm -f authorization_LexerTokenTypes.java   
	\rm -f authentication_LexerTokenTypes.java   
	\rm -f sdp_announceParserTokenTypes.java
	\rm -f charLexerTokenTypes.java             
	\rm -f sip_messageParserTokenTypes.java
	\rm -f command_keywordLexerTokenTypes.java  
	\rm -f sip_urlLexerTokenTypes.java
	\rm -f contact_parmsLexerTokenTypes.java    
	\rm -f sip_urlParserTokenTypes.java
	\rm -f dateLexerTokenTypes.java             
	\rm -f sip_urlbodyLexerTokenTypes.java
	\rm -f hide_LexerTokenTypes.java            
	\rm -f skipLexerTokenTypes.java
	\rm -f host_nameParserTokenTypes.java       
	\rm -f status_lineLexerTokenTypes.java
	\rm -f key_fieldLexerTokenTypes.java        
	\rm -f content_dispositionLexerTokenTypes.java        
	\rm -f tel_urlLexerTokenTypes.java        
	\rm -f timeLexerTokenTypes.java
	\rm -f method_keywordLexerTokenTypes.java   
	\rm -f parser_utilParserTokenTypes.java     
	\rm -f url_parmsLexerTokenTypes.java
	\rm -f transport_LexerTokenTypes.java
	\rm -f call_infoLexerTokenTypes.java
	\rm -f pgp_LexerTokenTypes.java             
	\rm -f via_parmsLexerTokenTypes.java
	\rm -f priority_LexerTokenTypes.java 
	\rm -f tel_ParserTokenTypes.java 
	\rm -f sdp_announceParser.java  	
	\rm -f sip_messageParser.java   	
	\rm -f parser_utilParser.java   	
	\rm -f host_nameParser.java     	
	\rm -f sip_urlParser.java		
	\rm -f charLexer.java                  	
	\rm -f command_keywordLexer.java       	
	\rm -f via_parmsLexer.java       	
	\rm -f method_keywordLexer.java        	
	\rm -f status_lineLexer.java           	
	\rm -f url_parmsLexer.java             	
	\rm -f transport_Lexer.java             	
	\rm -f call_infoLexer.java             	
	\rm -f sip_urlLexer.java               	
	\rm -f sip_urlbodyLexer.java           	
	\rm -f addr_parmsLexer.java            	
	\rm -f accept_languageLexer.java       	
	\rm -f dateLexer.java                  	
	\rm -f sdpLexer.java                   	
	\rm -f timeLexer.java                  	
	\rm -f authorization_Lexer.java         
	\rm -f authentication_Lexer.java         
	\rm -f key_fieldLexer.java              
	\rm -f content_dispositionLexer.java              
	\rm -f tel_urlLexer.java              
	\rm -f tel_Parser.java              
	\rm -f retry_afterLexer.java            
	\rm -f skipLexer.java                  	
	\rm -f pgp_Lexer.java                  	
	\rm -f digest_Lexer.java                  	
	\rm -f basic_Lexer.java                  	
	\rm -f priority_Lexer.java              
	\rm -f hide_Lexer.java              
	\rm -f contact_parmsLexer.java


alljava: sdp_announceParser.java sip_messageParser.java \
	sip_urlParser.java		\
	tel_Parser.java		\
	charLexer.java			\
   	via_parmsLexer.java 		\
        command_keywordLexer.java 	\
  	method_keywordLexer.java 	\
  	status_lineLexer.java 		\
  	url_parmsLexer.java  		\
	digest_Lexer.java		\
	basic_Lexer.java		\
  	transport_Lexer.java  		\
  	call_infoLexer.java  		\
  	sip_urlLexer.java 		\
	sip_urlbodyLexer.java 		\
	addr_parmsLexer.java  		\
	accept_languageLexer.java 	\
	dateLexer.java  		\
	sdpLexer.java 			\
	timeLexer.java 			\
	key_fieldLexer.java 		\
	content_dispositionLexer.java 	\
	tel_urlLexer.java 		\
	pgp_Lexer.java 			\
	authorization_Lexer.java 	\
	authentication_Lexer.java 	\
	retry_afterLexer.java 		\
	priority_Lexer.java 		\
	skipLexer.java 			\
	contact_parmsLexer.java 	\
	hide_Lexer.java 

# for development only
classes:
	$(JAVAC) *.java

all: alljava
	$(JAVAC) *.java
#	cd ../../../../;jar -cvf sipmsgparser.jar gov/nist/sip/msgparser/*.class

grammardoc: all
	$(ANTLRTOOL)  -html expandedsip_messageParser.g
	$(ANTLRTOOL)  -html expandedsdp_announceParser.g
	mv sip_messageParser.html $(DOCDIR)
	mv sdp_announceParser.html $(DOCDIR)



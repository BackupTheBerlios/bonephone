# this works with the gnu make tool. If you are working
# with windows, please install cygwin to get  gnumake (goto
# http://www.sourceware.cygnus.com).
#

ROOT=./

include  $(ROOT)/build-config


REV=revE
DOCDIR= $(ROOT)/docs/api
WINDOWTITLE="SIP Tools For the People!"
HDR= '<b> NIST SIP/SDP Parser and Stack (v1.1) REV API </b>'
HEADER=$(subst REV,$(REV),$(HDR))
DATE= $(shell date)
TITL= 'NIST SIP/SDP Parser and Stack (v1.1) REV (DATE)'
TITLE=$(subst REV,$(REV),$(TITL))
DOCTITLE=$(subst DATE,$(DATE),$(TITLE))
BOTTOM='<font size="-1"> \
<a href="http://w3.antd.nist.gov/"> A product of the NIST/ITL Advanced Networking Technologies Division. </a><br>  \
<a href="{@docRoot}/uncopyright.html"> See conditions of use. </a> <br> \
<a href="mailto:nist-sip-dev@antd.nist.gov">Submit a bug report or feature request. </a><br> \
</font>'
GROUPCORE = '"Core Packages" "gov.nist.sip.msgparser.*" "gov.nist.sip.*" \
"gov.nist.sip.sipheaders.*" "gov.nist.sip.sdpfields.*" '

javadoc: 
	cd $(ROOT)/gov/nist/sip/msgparser;$(MAKE) clean
	${JDK_HOME}/bin/javadoc -sourcepath $(ROOT)  			\
		-overview $(DOCDIR)/overview.html 	\
		-d  $(DOCDIR)				\
		-use					\
		-splitindex				\
		-windowtitle $(WINDOWTITLE) 		\
		-doctitle $(DOCTITLE)			\
		-header $(HEADER)			\
		-public					\
		-bottom $(BOTTOM)			\
		gov.nist.sip				\
		gov.nist.sip.net			\
		gov.nist.sip.msgparser			\
		gov.nist.sip.sipheaders			\
		gov.nist.sip.sdpfields			\
		gov.nist.sip.stack			\
		gov.nist.sip.stack.security		\
		gov.nist.jain.protocol.ip.sip		\
		gov.nist.jain.protocol.ip.sip.header	\
		gov.nist.jain.protocol.ip.sip.message	\
		gov.nist.jain.protocol.ip.sip.address	\
		jain.protocol.ip.sip		        \
		jain.protocol.ip.sip.header		\
		jain.protocol.ip.sip.message		

DDATE=$(shell date +%D)
DATE=$(subst /,-,$(DDATE))
FILE_NAME = 'nist-sip-1.1.REV.DATE.tar.gz'
TARFILE_NAME = $(subst REV,$(REV),$(subst DATE,$(DATE),$(FILE_NAME)))

all: 
	$(MAKE) clean
	$(MAKE) javadoc
	$(MAKE) classes


classes:
	cd gov/nist/sip/;\rm *.class;$(MAKE) all 
	cd gov/nist/sip/net;\rm *.class;$(MAKE) all
	cd gov/nist/sip/sipheaders;\rm *.class;$(MAKE) all
	cd gov/nist/sip/sdpfields;\rm *.class;$(MAKE) all
	cd gov/nist/sip/msgparser;\rm *.class;$(MAKE) all
	cd gov/nist/sip/stack;\rm *.class;$(MAKE) all
	cd gov/nist/sip/stack/security;\rm *.class;$(MAKE) all
	cd gov/nist/sip/msgparser;$(MAKE) grammardoc
	cd examples/torture;\rm *.class;$(MAKE) all
	cd examples/msgparser;\rm *.class;$(MAKE) all
	cd examples/jainsip/simple;\rm *.class;$(MAKE) all
	cd examples/jainsip/container;\rm *.class;$(MAKE) all
	cd examples/jainsip/shootist;\rm *.class;$(MAKE) all
	cd examples/proxy;\rm *.class;$(MAKE) all
	cd gov/nist/jain/protocol/ip/sip;\rm *.class;$(MAKE) all
	cd gov/nist/jain/protocol/ip/sip/address;\rm *.class;$(MAKE) all
	cd gov/nist/jain/protocol/ip/sip/header;\rm *.class;$(MAKE) all
	cd gov/nist/jain/protocol/ip/sip/message;\rm *.class;$(MAKE) all
	cd gov/nist/log;\rm *.class;$(MAKE) all
	cd tools/;\rm *.class;$(MAKE) all
	cd examples/torture;\rm *.class;$(MAKE) tests
	make jarfile

jarfile:
	jar -cvf nist-sip.jar				 \
	./gov/nist/sip/net/*.class  			 \
	./gov/nist/sip/msgparser/*.class  		 \
	./gov/nist/sip/sipheaders/*.class 		 \
	./gov/nist/sip/*.class  			 \
	./gov/nist/sip/sdpfields/*.class  		 \
	./gov/nist/sip/stack/*.class  			 \
	./gov/nist/sip/stack/security/*.class  		 \
	./jain/protocol/ip/sip/*.class 	 		 \
	./jain/protocol/ip/sip/header/*.class   	 \
	./jain/protocol/ip/sip/message/*.class   	 \
	./jain/protocol/ip/sip/address/*.class  	 \
	./gov/nist/jain/protocol/ip/sip/message/*.class  \
	./gov/nist/jain/protocol/ip/sip/*.class 	 \
	./gov/nist/jain/protocol/ip/sip/header/*.class   \
	./gov/nist/jain/protocol/ip/sip/address/*.class  \
	./gov/nist/jain/protocol/ip/sip/message/*.class  \
	./gov/nist/log/*.class 


clean: emacsclean
	cd gov/nist/sip/;$(MAKE)  clean 
	cd gov/nist/sip/net;$(MAKE) clean
	cd gov/nist/sip/sipheaders;$(MAKE) clean
	cd gov/nist/sip/sdpfields;$(MAKE) clean
	cd gov/nist/sip/msgparser;$(MAKE) clean
	cd gov/nist/log;$(MAKE) clean
	cd examples/msgparser;$(MAKE) clean
	cd examples/torture;$(MAKE) clean
	cd examples/jainsip/simple;$(MAKE) clean
	cd examples/jainsip/container;$(MAKE) clean
	cd examples/jainsip/shootist;$(MAKE) clean
	cd examples/proxy;$(MAKE) clean
	cd gov/nist/sip/stack;$(MAKE) clean
	cd gov/nist/sip/stack/security;$(MAKE) clean
	cd docs/api;$(MAKE) clean
	cd gov/nist/jain/protocol/ip/sip/;$(MAKE) clean	
	cd gov/nist/jain/protocol/ip/sip/address;$(MAKE) clean
	cd gov/nist/jain/protocol/ip/sip/header;$(MAKE) clean
	cd gov/nist/jain/protocol/ip/sip/message;$(MAKE) clean
	cd jain/protocol/ip/sip/;$(MAKE) clean	
	cd jain/protocol/ip/sip/address;$(MAKE) clean
	cd jain/protocol/ip/sip/header;$(MAKE) clean
	cd jain/protocol/ip/sip/message;$(MAKE) clean
	cd tools;$(MAKE) clean
	rm -f nist-sip.jar



tarfile:
	cd ../;							\
	tar -cvzf $(TARFILE_NAME)  --exclude CVS		\
	--exclude filesystem.attributes				\
	--exclude callflows2xml					\
	--exclude jainlog.txt				        \
	--exclude *.mp3						\
	./nist-sip/gov/nist/sip/net				\
	./nist-sip/gov/nist/sip/msgparser			\
	./nist-sip/gov/nist/sip/sipheaders			\
	./nist-sip/gov/nist/sip/sdpfields			\
	./nist-sip/gov/nist/sip/*.class				\
	./nist-sip/gov/nist/sip/*.java				\
	./nist-sip/gov/nist/sip/makefile			\
	./nist-sip/gov/nist/sip/stack				\
	./nist-sip/gov/nist/jain/				\
	./nist-sip/gov/nist/log/				\
	./nist-sip/tools/traceviewer/			        \
	./nist-sip/tools/traceviewerapp/			\
	./nist-sip/tools/responder/			        \
	./nist-sip/tools/makefile		                \
	./nist-sip/tools/README			                \
	./nist-sip/jain						\
	./nist-sip/README					\
	./nist-sip/examples/torture				\
	./nist-sip/examples/msgparser				\
	./nist-sip/examples/jainsip				\
	./nist-sip/examples/proxy				\
	./nist-sip/makefile					\
	./nist-sip/docs/api					\
	./nist-sip/lib						\
	./nist-sip/build-config					\
	./nist-sip/nist-sip.jar					\
	
# build the ship tar.gz image.
ship: 
	$(MAKE) clean
	$(MAKE) all 
	$(MAKE) tarfile



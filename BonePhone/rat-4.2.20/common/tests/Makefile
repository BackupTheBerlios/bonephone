# Generated automatically from Makefile.in by configure.
#
# Makefile for the common code library project. 
# This probably requires GNU make.
#

COMMONLIB=uclmmbase
COMMONSRC=../src
COMMONTGT = $(COMMONSRC)/lib$(COMMONLIB).a

DEFS   = -DHAVE_CONFIG_H
CFLAGS = -I$(COMMONSRC) -g -O2 -W -Wall -Wwrite-strings -Wbad-function-cast -Wmissing-prototypes -Wcast-qual -Wmissing-declarations -Werror $(DEFS) 
LIBS   = -L../src  
CC     = gcc

OBJS = test_base64.o test_des.o test_md5.o test_net_udp.o \
       test_memory.o test_mbus_parser.o test_mbus_addr.o  \
       test.o

SRCS = $(OBJS:%.o=%.c)

all: test-libcommon

test-libcommon: $(OBJS) $(COMMONTGT)
	$(CC) $(OBJS) $(CFLAGS) $(COMMONTGT) $(LIBS) -o test-libcommon

.c.o:
	$(CC) $(CFLAGS) $(INC) -c $<

clean:
	-rm -f $(OBJS) tags test-libcommon

distclean:	clean
	-rm -f Makefile

etags:
	etags *.[ch]

ctags:
	ctags *.[ch]

depend: $(SRCS)
	makedepend $(DEFS) $(INC) $(SRCS)


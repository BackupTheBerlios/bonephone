PWD=$(shell pwd)
MBUSLIBDIR = $(PWD)/lib
LIBDIR= ${JHOME}/jre/lib
MAKE= make

CLASSPATH= $(LIBDIR):$(MBUSLIBDIR)
RUNPATH= $(LIBDIR):$(SRCDIR)/mbus.jar
CFLAGS= -d $(MBUSLIBDIR) -classpath $(CLASSPATH) -deprecation
RFLAGS= -classpath $(RUNPATH) -Djava.compiler=

export JAVAC
export JAVA
export CFLAGS

all:	base apps bin 

base:
	$(MAKE) -C org/mbus
apps:
	$(MAKE) -C org/mbus/apps

doc:
	$(JAVAD) -d javadoc -use org.mbus org.mbus.apps

bin:
	cd $(MBUSLIBDIR) ; $(JAR) -cvf ../mbus.jar org/mbus/* org/mbus/apps/*

runspy:
	java $(RFLAGS) org.mbus.apps.MSpy

clean:
	rm -rf $(MBUSLIBDIR)/org
	rm -rf javadoc/*
	rm -f mbus.jar
	$(MAKE) -C org/mbus clean
	$(MAKE) -C org/mbus/apps clean




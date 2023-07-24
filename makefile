JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $*.java

SOURCES = gatorTaxi.java

default: classes

classes: $(SOURCES:.java=.class)
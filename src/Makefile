
JFLAGS = -g
JC = javac
JVM= java

# .java de les classes
CLASSES_INPUT = \
	./Main/CapaDomini/*.java \
	./Main/CapaPresentacio/*.java \
	./Main/CapaDades/*.java

# .class de les classes
OUTPUT_CLASSES = \
    ../PROVA/src/Main/CapaDomini/*.class \
    ../PROVA/src/Main/CapaPresentacio/*.class \
    ../PROVA/src/Main/CapaDades/*.class \

LIB_FORM = ../lib/forms_rt.jar:../lib/junit-4.13.2.jar

#LIB = ../llibreries/hamcrest-core-1.3.jar  -cp ../llibreries/junit-4.13.jar


exec:
	javac -cp $(LIB_FORM) -d ../PROVA/src $(CLASSES_INPUT)
	cp -R ../lib ../PROVA
	cp -R ../EXE ../PROVA/src
	cp -R ./META-INF ../PROVA/src
	cd ../PROVA/src
	jar cvmf META-INF/MANIFEST.MF prog.jar  $(OUTPUT_CLASSES)

clean:
	rm -r Main/CapaDomini/*.class
	rm -r Main/CapaPresentacio/*.class
	rm -r Main/CapaDades/*.class
	rm -r PROVA

COMPILER=javac

SOURCES = \
	Main.java \
	ImagePreprocessor.java \
	PixelExtractor.java \
	Visualisation.java \
	Window.java

OPTIONS = -classpath ../bin/javaGeom-0.11.2.jar

all: shape.jar clean

class:
	$(COMPILER) $(OPTIONS) $(SOURCES)

shape.jar: class
	jar cmvf MANIFEST.MF ../bin/shape.jar $(SOURCES:.java=.class) -C .. img

install:
	echo "Nothing to install."

clean:
	rm *.class

COMPILER=javac

SOURCES = \
	Main.java \
	Point.java \
	ImagePreprocessor.java \
	PixelExtractor.java \
	Visualisation.java \
	Window.java

OPTIONS =

all: shape.jar clean

class:
	$(COMPILER) $(OPTIONS) $(SOURCES)

shape.jar: class
	jar cmvf MANIFEST.MF ../bin/shape.jar $(SOURCES:.java=.class) -C .. img

install:
	echo "Nothing to install."

clean:
	rm *.class

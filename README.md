**This code is not intended for public use.**

Fork of [SETH](http://rockt.github.com/SETH/) with some few modifications that were necessary for the development and evaluation of [nala](https://github.com/Rostlab/nala).

The relevant modifications were done to the file [SETHNERAppMut](https://github.com/juanmirocks/SETH/blob/master/src/main/java/seth/ner/wrapper/SETHNERAppMut.java) to:

* Allow running the method as a simple REST API server
* Write the output in [brat format](http://brat.nlplab.org/standoff.html)

## Build

`mvn clean compile assembly:single`

For simplification, add the generated jar temporallily to your CLASSPATH, as in:

CLASSPATH=$CLASSPATH:${SETH_FOLDER}/target/seth-1.3-Snapshot-juanmirocks-jar-with-dependencies.jar


## Run

* REST API server, example: `java seth.ner.wrapper.SETHNERAppMut -p 8000`

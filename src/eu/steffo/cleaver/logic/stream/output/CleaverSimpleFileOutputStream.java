package eu.steffo.cleaver.logic.stream.output;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CleaverSimpleFileOutputStream extends FileOutputStream implements ICleaverOutputStream {
    private final String fileBaseName;

    public CleaverSimpleFileOutputStream(String fileBaseName) throws FileNotFoundException {
        super(String.format("%s.c0", fileBaseName));
        this.fileBaseName = fileBaseName;
    }

    @Override
    public Element toElement(Document doc) {
        Element element = doc.createElement("Simple");
        element.setTextContent(fileBaseName);

        return element;
    }
}

package eu.steffo.cleaver.logic.stream.output;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * A {@link ICleaverOutputStream} that writes data to a single file with a *.c0 extension.
 *
 * @see FileOutputStream
 */
public class CleaverSimpleFileOutputStream extends FileOutputStream implements ICleaverOutputStream {
    /**
     * The base (the {@link File} without *.c0 extension) of the file to write to.
     */
    private final File baseFile;

    /**
     * Create a new CleaverSimpleFileOutputStream.
     * @param baseFile The base (the {@link File} without *.c0 extension) of the file to write to.
     * @throws FileNotFoundException If a required file isn't found.
     */
    public CleaverSimpleFileOutputStream(File baseFile) throws FileNotFoundException {
        super(String.format("%s.c0", baseFile));
        this.baseFile = baseFile;
    }

    @Override
    public Element toElement(Document doc) {
        Element element = doc.createElement("Simple");

        Element fileElement = doc.createElement("OriginalFile");
        fileElement.setTextContent(baseFile.getName());
        element.appendChild(fileElement);

        return element;
    }

    /**
     * @return The base (the {@link File} without *.c0 extension) of the file to write to.
     */
    public File getBaseFile() {
        return baseFile;
    }
}

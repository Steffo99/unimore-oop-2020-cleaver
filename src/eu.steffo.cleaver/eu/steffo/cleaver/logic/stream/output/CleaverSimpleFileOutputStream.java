package eu.steffo.cleaver.logic.stream.output;

import org.w3c.dom.*;
import java.io.*;

/**
 * A {@link ICleaverOutputStream} that writes data to a single ̧{@link File} with a *.c0 extension through a {@link BufferedOutputStream} wrapping a
 * {@link FileOutputStream}.
 */
public class CleaverSimpleFileOutputStream extends FilterOutputStream implements ICleaverOutputStream {
    /**
     * The base (the {@link File} without *.c0 extension) of the file to write to.
     */
    private final File baseFile;

    /**
     * The buffer size in bytes of the {@link BufferedOutputStream} created by this object (currently {@value}).
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * Create a new CleaverSimpleFileOutputStream.
     * @param baseFile The base (the {@link File} without *.c0 extension) of the file to write to.
     * @throws FileNotFoundException If a required file isn't found.
     */
    public CleaverSimpleFileOutputStream(File baseFile) throws FileNotFoundException {
        super(new BufferedOutputStream(new FileOutputStream(String.format("%s.c0", baseFile)), BUFFER_SIZE));
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

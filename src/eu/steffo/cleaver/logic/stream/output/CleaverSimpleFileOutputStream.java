package eu.steffo.cleaver.logic.stream.output;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * A custom {@link OutputStream} that writes the bytes received in input to a single file with a *.c0 extension.
 *
 * @see FileOutputStream
 */
public class CleaverSimpleFileOutputStream extends FileOutputStream implements ICleaverOutputStream {
    private final File baseFile;

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
     * Get the base {@link File}.
     *
     * The base {@link File} is the one that gives the name to all generated files, including the chopped file (*.c0) and the reconstructed file.
     *
     * For example, if it is {@literal foo.txt}, the created file will be {@literal foo.txt.c0}.
     */
    public File getBaseFile() {
        return baseFile;
    }
}

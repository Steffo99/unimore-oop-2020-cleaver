package eu.steffo.cleaver.logic.stream.output;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A custom {@link OutputStream} that writes the bytes received in input in multiple files with a progressively increasing number (.c1, .c2, .c3, and so on).
 *
 * Bytes are written to a file until its length reaches {@link #maximumByteCount}, then the program switches to the following file (.c2 if .c1 is full, .c3 if .c2 is full, and so on).
 */
public class CleaverSplitFileOutputStream extends OutputStream implements ICleaverOutputStream {
    private final File baseFile;
    private long currentByteCount;
    private long maximumByteCount;
    private int currentFileCount;

    /**
     * The {@link FileOutputStream} this {@link OutputStream} is currently writing to.
     */
    protected FileOutputStream currentFileOutputStream;

    /**
     * Construct a CleaverSplitFileOutputStream.
     * @param baseFile The {@link File} that will be reconstructed after reversing the Split operation.
     *                 The split files will have the same name with the addition of a .cXX extension.
     * @param maximumByteCount The number of bytes that should be written to a file before switching to the next one.
     */
    public CleaverSplitFileOutputStream(File baseFile, long maximumByteCount) {
        this.baseFile = baseFile;
        this.maximumByteCount = maximumByteCount;
        this.currentByteCount = 0;
        this.currentFileCount = 0;
        this.currentFileOutputStream = null;
    }

    /**
     * Create the following file in the sequence, and update the {@link #currentFileOutputStream}.
     * @throws IOException If for some reason the program cannot create the file.
     */
    protected void createNextFileOutputStream() throws IOException {
        if(currentFileOutputStream != null) {
            currentFileOutputStream.close();
        }

        currentFileCount += 1;
        currentFileOutputStream = new FileOutputStream(String.format("%s.c%d", baseFile.getAbsolutePath(), currentFileCount));
        currentByteCount = 0;
    }

    @Override
    public void write(int b) throws IOException {
        // Can be optimized using the modulo operation, not doing it now for clarity
        if(currentFileOutputStream == null || currentByteCount >= maximumByteCount) {
            createNextFileOutputStream();
        }
        currentFileOutputStream.write(b);
        currentByteCount += 1;
    }

    @Override
    public void close() throws IOException {
        currentFileOutputStream.close();
    }

    /**
     * Get the base {@link File}.
     *
     * The base {@link File} is the one that gives the name to all generated files, including the file parts (*.cXX) and the reconstructed file.
     *
     * For example, if it is {@literal foo.txt}, the created files will be {@literal foo.txt.c1}, {@literal foo.txt.c2}, and so on.
     *
     * @return The base file.
     */
    public File getBaseFile() {
        return baseFile;
    }

    /**
     * @return The number of bytes that have already been written to the current file.
     */
    public long getCurrentByteCount() {
        return currentByteCount;
    }

    /**
     * @return The number of bytes that should be written to a file before switching to the next one.
     */
    public long getMaximumByteCount() {
        return maximumByteCount;
    }

    /**
     * @return The number of files that have already been created.
     */
    public int getCurrentFileCount() {
        return currentFileCount;
    }

    @Override
    public Element toElement(Document doc) {
        Element element = doc.createElement("Split");

        Element fileElement = doc.createElement("OriginalFile");
        fileElement.setTextContent(baseFile.getName());
        element.appendChild(fileElement);

        Attr partSizeAttr = doc.createAttribute("part-size");
        partSizeAttr.setValue(Long.toString(maximumByteCount));
        element.setAttributeNode(partSizeAttr);

        Attr partCountAttr = doc.createAttribute("parts");
        partCountAttr.setValue(Long.toString(currentFileCount));
        element.setAttributeNode(partCountAttr);

        return element;
    }
}

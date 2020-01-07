package eu.steffo.cleaver.logic.stream.output;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A custom {@link OutputStream} that writes the bytes received in input in multiple files with a progressively increasing number (.c1, .c2, .c3, and so on).
 *
 * Bytes are written to a file until its length reaches {@link #maximumByteCount}, then the program switches to the following file (.c2 if .c1 is full, .c3 if .c2 is full, and so on).
 */
public class CleaverSplitFileOutputStream extends OutputStream implements ICleaverOutputStream {
    private final String fileBaseName;
    private long currentByteCount;
    private long maximumByteCount;
    private long totalByteCount;
    private int currentFileCount;

    /**
     * The {@link FileOutputStream} this {@link OutputStream} is currently writing to.
     */
    protected FileOutputStream currentFileOutputStream;

    /**
     * Construct a CleaverSplitFileOutputStream.
     * @param fileBaseName The name of the files without the extension. If it is {@literal example}, the created files will be {@literal example.c1}, {@literal example.c2}, and so on.
     * @param maximumByteCount The number of bytes that should be written to a file before switching to the next one.
     */
    public CleaverSplitFileOutputStream(String fileBaseName, long maximumByteCount) {
        this.fileBaseName = fileBaseName;
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
        currentFileOutputStream = new FileOutputStream(String.format("%s.c%d", fileBaseName, currentFileCount));
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
        totalByteCount += 1;
    }

    @Override
    public void close() throws IOException {
        currentFileOutputStream.close();
    }

    /**
     * @return The name of the files without the extension. If it is {@literal example}, the created files will be {@literal example.c1}, {@literal example.c2}, and so on.
     */
    public String getFileBaseName() {
        return fileBaseName;
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

    /**
     * @return The number of bytes that have already been written.
     */
    public long getTotalByteCount() {
        return totalByteCount;
    }

    @Override
    public Element toElement(Document doc) {
        Element element = doc.createElement("Split");
        element.setTextContent(fileBaseName);

        Attr partSizeAttr = doc.createAttribute("part-size");
        partSizeAttr.setValue(Long.toString(maximumByteCount));
        element.setAttributeNode(partSizeAttr);

        Attr partCountAttr = doc.createAttribute("parts");
        partCountAttr.setValue(Long.toString(currentFileCount));
        element.setAttributeNode(partCountAttr);

        Attr totalSizeAttr = doc.createAttribute("total-size");
        totalSizeAttr.setValue(Long.toString(totalByteCount));
        element.setAttributeNode(totalSizeAttr);

        return element;
    }
}

package eu.steffo.cleaver.logic.stream.output;

import eu.steffo.cleaver.logic.utils.SafeLongToInt;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@link ICleaverOutputStream} that writes data to a series of files having a predefined size.
 *
 * Bytes are written to a file until its length reaches {@link #maximumByteCount}, then the program switches to the following file (.c2 if .c1 is full, .c3 if
 * .c2 is full, and so on).
 */
public class CleaverSplitFileOutputStream extends OutputStream implements ICleaverOutputStream {
    /**
     * @see #getBaseFile()
     */
    private final File baseFile;

    /**
     * The number of bytes that have already been written to the current file.
     */
    private long currentByteCount;

    /**
     * The number of bytes that should be written to a file before switching to the following one.
     */
    private final long maximumByteCount;

    /**
     * The number of files that have been opened so far.
     */
    private int currentFileCount;

    /**
     * The {@link OutputStream} this object is currently writing to.
     *
     * It is a {@link BufferedOutputStream} wrapping a {@link FileOutputStream}.
     */
    private BufferedOutputStream currentOutputStream;

    /**
     * The maximum buffer size in bytes of the {@link BufferedOutputStream BufferedOutputStreams} created by this object (currently {@value}).
     *
     * If the {@link #maximumByteCount} is lower than this value, that is used instead.
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * Construct a CleaverSplitFileOutputStream.
     * @param baseFile {@link #getBaseFile() Please see getBaseFile().}
     * @param maximumByteCount The number of bytes that should be written to a file before switching to the next one.
     */
    public CleaverSplitFileOutputStream(File baseFile, long maximumByteCount) {
        this.baseFile = baseFile;
        this.maximumByteCount = maximumByteCount;
        this.currentByteCount = 0;
        this.currentFileCount = 0;
        this.currentOutputStream = null;
    }

    /**
     * Create the following file in the sequence, and update the {@link #currentOutputStream}.
     * @throws IOException If for some reason the program cannot create the file.
     */
    private void createNextFileOutputStream() throws IOException {
        if(currentOutputStream != null) {
            currentOutputStream.close();
        }

        currentFileCount += 1;
        currentOutputStream = new BufferedOutputStream(new FileOutputStream(String.format("%s.c%d", baseFile.getAbsolutePath(), currentFileCount)),
                                                       Math.min(BUFFER_SIZE, SafeLongToInt.longToInt(maximumByteCount)));
        currentByteCount = 0;
    }

    @Override
    public void write(int b) throws IOException {
        // Can be optimized using the modulo operation, not doing it now for clarity
        if(currentOutputStream == null || currentByteCount >= maximumByteCount) {
            createNextFileOutputStream();
        }
        currentOutputStream.write(b);
        currentByteCount += 1;
    }

    @Override
    public void close() throws IOException {
        currentOutputStream.close();
    }

    /**
     * Get the base {@link File}.
     *
     * The base file is the {@link File} will be reconstructed after reversing the Split operation with a
     * {@link eu.steffo.cleaver.logic.stream.input.CleaverSplitFileInputStream}.
     *
     * The files created by this stream will have the same name of the base file, with the addition of a .cXX extension.
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

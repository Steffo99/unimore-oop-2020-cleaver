package eu.steffo.cleaver.logic.stream.input;

import java.io.*;

/**
 * A {@link ICleaverInputStream} that reads data from a series of files having a predefined size.
 *
 * Bytes are read from a file until {@link #maximumByteCount} bytes are read, then the program switches to the following file (.c2 if .c1 is full, .c3 if .c2
 * is full, and so on).
 */
public class CleaverSplitFileInputStream extends InputStream implements ICleaverInputStream {
    /**
     * @see #getBaseFile()
     */
    private final File baseFile;

    /**
     * The number of bytes that have already been read from the current file.
     */
    private long currentByteCount;

    /**
     * The number of bytes that should be read from a file before switching to the following one.
     */
    private final long maximumByteCount;

    /**
     * The number of files that have been opened so far.
     */
    private int currentFileCount;

    /**
     * The {@link FileInputStream} this {@link InputStream} is currently reading from.
     */
    private FileInputStream currentFileInputStream;

    /**
     * Construct a CleaverSplitFileInputStream.
     *
     * @param baseFile {@link #getBaseFile() Please see getBaseFile().}
     * @param maximumByteCount The number of bytes that should be read from a file before switching to the next one.
     */
    public CleaverSplitFileInputStream(File baseFile, long maximumByteCount) {
        this.baseFile = baseFile;
        this.maximumByteCount = maximumByteCount;
        this.currentByteCount = 0;
        this.currentFileCount = 0;
        this.currentFileInputStream = null;
    }

    /**
     * Open the following file in the sequence, and update the {@link #currentFileInputStream}.
     * @throws IOException If a problem is encountered while opening or closing a {@link FileInputStream}.
     */
    private void createNextFileInputStream() throws IOException {
        if(currentFileInputStream != null) {
            currentFileInputStream.close();
        }

        currentFileCount += 1;
        currentFileInputStream = new FileInputStream(String.format("%s.c%d", baseFile.getAbsolutePath(), currentFileCount));
        currentByteCount = 0;
    }

    @Override
    public int read() throws IOException {
        if(currentFileInputStream == null || currentByteCount >= maximumByteCount) {
            createNextFileInputStream();
        }
        int result = currentFileInputStream.read();
        currentByteCount += 1;
        return result;
    }

    @Override
    public void close() throws IOException {
        currentFileInputStream.close();
    }

    /**
     * Get the base {@link File}.
     *
     * The base file is the {@link File} that was split by a {@link eu.steffo.cleaver.logic.stream.output.CleaverSplitFileOutputStream} and will be now
     * reconstructed by this object.
     *
     * The files read by this stream have the same name of the base file with the addition of a .cXX extension.
     *
     * @return The base file.
     */
    public File getBaseFile() {
        return baseFile;
    }

    /**
     * @return The number of bytes that have already been read from the current file.
     */
    public long getCurrentByteCount() {
        return currentByteCount;
    }

    /**
     * @return The number of bytes that should be read from a file before switching to the following one.
     */
    public long getMaximumByteCount() {
        return maximumByteCount;
    }

    /**
     * @return The number of files that have been opened so far.
     */
    public int getCurrentFileCount() {
        return currentFileCount;
    }
}

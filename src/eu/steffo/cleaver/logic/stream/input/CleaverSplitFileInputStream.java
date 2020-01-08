package eu.steffo.cleaver.logic.stream.input;

import org.w3c.dom.Element;

import java.io.*;

public class CleaverSplitFileInputStream extends InputStream implements ICleaverInputStream {
    private final File baseFile;
    private long currentByteCount;
    private long maximumByteCount;
    private int currentFileCount;

    /**
     * The {@link FileInputStream} this {@link InputStream} is currently reading from.
     */
    protected FileInputStream currentFileInputStream;

    /**
     * Construct a SplitFileInputStream.
     * It will read data from the files having the same name as the {@link #baseFile} and a *.cXX extension.
     *
     * @param baseFile The {@link File} to be reconstructed.
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
     * @throws IOException If for some reason the program cannot open the file.
     */
    protected void createNextFileInputStream() throws IOException {
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
     * The stream will read from multiple files having a name constituted by the base {@link File} name and a *.cXX extension.
     *
     * For example, if it is {@literal foo.txt}, the stream will read from {@literal foo.txt.c1}, {@literal foo.txt.c2}, and so on.
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
     * @return The number of bytes that should be read from a file before switching to the next one.
     */
    public long getMaximumByteCount() {
        return maximumByteCount;
    }

    /**
     * @return The number of files that have already been read.
     */
    public int getCurrentFileCount() {
        return currentFileCount;
    }
}

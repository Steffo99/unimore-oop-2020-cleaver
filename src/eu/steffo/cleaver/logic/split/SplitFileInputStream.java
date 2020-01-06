package eu.steffo.cleaver.logic.split;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SplitFileInputStream extends InputStream {
    private final String fileBaseName;
    private long currentByteCount;
    private long maximumByteCount;
    private int currentFileCount;

    /**
     * The {@link FileInputStream} this {@link InputStream} is currently reading from.
     */
    protected FileInputStream currentFileInputStream;

    /**
     * Construct a SplitFileInputStream.
     * @param fileBaseName The name of the files without the extension. If it is {@literal example}, the opened files will be {@literal example.c1}, {@literal example.c2}, and so on.
     * @param maximumByteCount The number of bytes that should be read from a file before switching to the next one.
     */
    public SplitFileInputStream(String fileBaseName, long maximumByteCount) {
        this.fileBaseName = fileBaseName;
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
        currentFileInputStream = new FileInputStream(String.format("%s.c%d", fileBaseName, currentFileCount));
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
     * @return The name of the files without the extension. If it is {@literal example}, the opened files will be {@literal example.c1}, {@literal example.c2}, and so on.
     */
    public String getFileBaseName() {
        return fileBaseName;
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

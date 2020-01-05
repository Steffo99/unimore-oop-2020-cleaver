package eu.steffo.cleaver.logic.split;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SplitFileOutputStream extends OutputStream {
    //TODO: possibly use a BufferedOutputStream to improve performance?

    protected String fileBaseName;
    protected FileOutputStream currentFileOutputStream;
    protected long currentByteCount;
    protected long maximumByteCount;
    protected int currentFileCount;

    public SplitFileOutputStream(String fileBaseName, long maximumByteCount) {
        this.fileBaseName = fileBaseName;
        this.maximumByteCount = maximumByteCount;
        this.currentByteCount = 0;
        this.currentFileCount = 0;
        this.currentFileOutputStream = null;
    }

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
}

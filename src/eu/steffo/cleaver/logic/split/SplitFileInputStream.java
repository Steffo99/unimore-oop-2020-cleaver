package eu.steffo.cleaver.logic.split;

import java.io.IOException;
import java.io.InputStream;

public class SplitFileInputStream extends InputStream {
    protected String fileBaseName;
    protected int partSize;
    protected int currentFileCount;

    public SplitFileInputStream(String fileBaseName, int partSize) {
        this.fileBaseName = fileBaseName;
        this.partSize = partSize;
        this.currentFileCount = 0;
    }

    @Override
    public int read() throws IOException {
        throw new IOException("Not implemented yet");
    }
}

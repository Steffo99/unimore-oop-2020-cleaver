package eu.steffo.cleaver.logic.stream.input;

import java.io.*;

/**
 * A {@link ICleaverInputStream} that reads split data from a specific number of files having the same size.
 *
 * Bytes are read one at a time from the files in a round-robin format until their streams are exausted.
 */
public class CleaverForkFileInputStream extends InputStream implements ICleaverInputStream {
    /**
     * @see #getBaseFile()
     */
    private final File baseFile;

    /**
     * The {@link FileInputStream FileInputStreams} from where bytes are read.
     */
    private FileInputStream[] fileInputStreams;

    /**
     * The index of the next {@link #fileInputStreams FileInputStream} to read a byte from.
     */
    private int readFrom;

    /**
     * The number of bytes that have been read from a single part.
     */
    private long partSize;

    /**
     * Construct a new CleaverForkFileInputStream.
     * @param baseFile {@link #getBaseFile() Please see getBaseFile().}
     * @param parts The number of parts the original file is split into.
     * @throws FileNotFoundException If a required file isn't found.
     */
    public CleaverForkFileInputStream(File baseFile, int parts) throws FileNotFoundException {
        this.baseFile = baseFile;
        this.fileInputStreams = new FileInputStream[parts];
        for(int i = 0; i < parts; i++) {
            File file = new File(String.format("%s.c%s", baseFile.getAbsolutePath(), i));
            this.fileInputStreams[i] = new FileInputStream(file);
        }
        this.readFrom = 0;
        this.partSize = 1;
    }

    /**
     * Get the base {@link File}.
     *
     * The base file is the {@link File} that was split by a {@link eu.steffo.cleaver.logic.stream.output.CleaverForkFileOutputStream} and will be now
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
     * @return The number of bytes that have been read from a single part.
     */
    public long getPartSize() {
        return partSize;
    }

    /**
     * @return The number of file parts to read from.
     */
    public int getParts() {
        return fileInputStreams.length;
    }

    /**
     * @return The index of the next {@link #fileInputStreams FileInputStream} to read from.
     */
    public int getReadFrom() {
        return readFrom;
    }

    /**
     * @return The number of bytes that have been read, in total.
     */
    public long getTotalReadBytes() {
        return getParts() * getPartSize() + getReadFrom();
    }

    @Override
    public int read() throws IOException {
        int b = fileInputStreams[readFrom].read();
        readFrom += 1;
        if(readFrom >= fileInputStreams.length) {
            readFrom = 0;
            partSize += 1;
        }
        return b;
    }

    @Override
    public void close() throws IOException {
        super.close();
        for(FileInputStream fileInputStream : fileInputStreams) {
            fileInputStream.close();
        }
    }
}

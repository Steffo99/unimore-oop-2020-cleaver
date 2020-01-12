package eu.steffo.cleaver.logic.stream.input;

import java.io.*;

/**
 * A {@link ICleaverInputStream} that reads data from a single {@link File} with a *.c0 extension through a {@link BufferedInputStream} wrapping a
 * {@link FileInputStream}.
 */
public class CleaverSimpleFileInputStream extends FilterInputStream implements ICleaverInputStream {
    /**
     * The base (the {@link File} without *.c0 extension) of the file to read from.
     */
    private final File baseFile;

    /**
     * The buffer size in bytes of the {@link BufferedInputStream} created by this object (currently {@value}).
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * Create a new CleaverSimpleFileInputStream.
     * @param baseFile The base (the {@link File} without *.c0 extension) of the file to read from.
     * @throws FileNotFoundException If a required file isn't found.
     */
    public CleaverSimpleFileInputStream(File baseFile) throws FileNotFoundException {
        super(new BufferedInputStream(new FileInputStream(String.format("%s.c0", baseFile)), BUFFER_SIZE));
        this.baseFile = baseFile;
    }

    /**
     * @return The base (the {@link File} without *.c0 extension) of the file to read from.
     */
    public File getBaseFile() {
        return baseFile;
    }
}

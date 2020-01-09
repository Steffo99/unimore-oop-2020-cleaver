package eu.steffo.cleaver.logic.stream.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * A {@link ICleaverInputStream} that reads data from a {@link File} with a *.c0 extension.
 *
 * @see FileInputStream
 */
public class CleaverSimpleFileInputStream extends FileInputStream implements ICleaverInputStream {
    /**
     * The base (the {@link File} without *.c0 extension) of the file to read from.
     */
    private final File baseFile;

    /**
     * Create a new CleaverSimpleFileInputStream.
     * @param baseFile The base (the {@link File} without *.c0 extension) of the file to read from.
     * @throws FileNotFoundException If a required file isn't found.
     */
    public CleaverSimpleFileInputStream(File baseFile) throws FileNotFoundException {
        super(String.format("%s.c0", baseFile));
        this.baseFile = baseFile;
    }

    /**
     * @return The base (the {@link File} without *.c0 extension) of the file to read from.
     */
    public File getBaseFile() {
        return baseFile;
    }
}

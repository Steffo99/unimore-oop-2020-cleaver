package eu.steffo.cleaver.logic.stream.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CleaverSimpleFileInputStream extends FileInputStream implements ICleaverInputStream {
    private final File baseFile;

    public CleaverSimpleFileInputStream(File baseFile) throws FileNotFoundException {
        super(String.format("%s.c0", baseFile));
        this.baseFile = baseFile;
    }
}

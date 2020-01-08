package eu.steffo.cleaver.logic.stream.input;

import java.io.FilterInputStream;
import java.io.InputStream;

public class CleaverCryptInputStream extends FilterInputStream implements ICleaverInputStream {

    protected CleaverCryptInputStream(InputStream in) {
        super(in);
    }
}

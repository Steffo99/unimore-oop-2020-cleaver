package eu.steffo.cleaver.logic.crypt;

import java.io.FilterInputStream;
import java.io.InputStream;

public class CryptInputStream extends FilterInputStream {
    protected CryptInputStream(InputStream in) {
        super(in);
    }

    //TODO: This doesn't do anything... yet.
}

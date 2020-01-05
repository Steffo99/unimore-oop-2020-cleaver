package eu.steffo.cleaver.logic.crypt;

import java.io.FilterOutputStream;
import java.io.OutputStream;

public class CryptOutputStream extends FilterOutputStream {
    public CryptOutputStream(OutputStream out) {
        super(out);
    }

    //TODO: This doesn't do anything... yet.
}

package eu.steffo.cleaver.logic.crypt;

import javax.crypto.Cipher;
import java.io.FilterInputStream;
import java.io.InputStream;

public class CryptInputStream extends FilterInputStream {

    public CryptInputStream(InputStream in) {
        super(in);
    }

    //TODO: This doesn't do anything... yet.
}

package eu.steffo.cleaver.logic.crypt;

import eu.steffo.cleaver.errors.ProgrammingError;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptOutputStream extends FilterOutputStream {
    private Cipher cipher;

    private final String algorithm;
    private final String modeOfOperation;
    private final String padding;

    public String getAlgorithm() {
        return algorithm;
    }

    public String getModeOfOperation() {
        return modeOfOperation;
    }

    public String getPadding() {
        return padding;
    }

    public String getTransformationString() {
        return String.format("%s/%s/%s", algorithm, modeOfOperation, padding);
    }

    public CryptOutputStream(OutputStream out, String key, String algorithm, String modeOfOperation, String padding) throws ProgrammingError {
        super(out);

        this.algorithm = algorithm;
        this.modeOfOperation = modeOfOperation;
        this.padding = padding;

        //Setup the cipher object
        try {
            cipher = Cipher.getInstance(getTransformationString());
        } catch (NoSuchAlgorithmException e) {
            // This should never happen.
            throw new ProgrammingError("Invalid algor specified in the CryptOutputStream.");
        } catch (NoSuchPaddingException e) {
            // This should never happen.
            throw new ProgrammingError("Invalid padding specified in the CryptOutputStream.");
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), this.algorithm));
        } catch (InvalidKeyException e) {
            // This should never happen.
            throw new ProgrammingError("Invalid key specified in the CryptOutputStream.");
        }
    }

    public CryptOutputStream(OutputStream out, String key) throws ProgrammingError {
        this(out, key, "AES", "CBC", "PKCS5Padding");
    }

    @Override
    public void write(int b) throws IOException {
        //TODO
        super.write(b);
    }
}

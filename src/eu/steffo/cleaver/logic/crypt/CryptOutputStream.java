package eu.steffo.cleaver.logic.crypt;

import eu.steffo.cleaver.errors.ProgrammingError;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class CryptOutputStream extends FilterOutputStream {
    private Cipher cipher;

    /**
     * @return The algorithm used for the encryption.
     */
    public String getAlgorithm() {
        return "AES";
    }

    /**
     * @return The mode of operation used for the encryption.
     */
    public String getModeOfOperation() {
        return "CFB8";
    }

    /**
     * @return The padding used for the encryption.
     */
    public String getPadding() {
        return "PKCS5Padding";
    }

    /**
     * @return The secret key algorithm used in the generation of the final key.
     */
    public String getKeyAlgorithm() {
        return "PBKDF2WithHmacSHA1";
    }

    /**
     * @return The full transformation string as required by {@link Cipher#getInstance(String)}.
     */
    public String getTransformationString() {
        return String.format("%s/%s/%s", getAlgorithm(), getModeOfOperation(), getPadding());
    }

    /**
     * Create a new CryptOutputStream with default {@link Cipher} parameters.
     * (AES algorithm in operation mode CFB8 with PKCS5 padding)
     *
     * Does not use this as {@literal try} and {@literal catch} are not supported.
     *
     * @param out The {@link OutputStream} to connect this {@link FilterOutputStream} to.
     * @param key The desired encryption key.
     */
    public CryptOutputStream(OutputStream out, String key) throws InvalidKeyException {
        super(out);

        //Setup the cipher object
        try {
            cipher = Cipher.getInstance(getTransformationString());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            //Should never happen, as it's predefined
            e.printStackTrace();
            return;
        }

        //Create the salt
        byte[] salt = new byte[8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

        //Create the KeySpec
        //Using the recommended 65536 as iteration count
        KeySpec spec = new PBEKeySpec(key.toCharArray(), salt, 65536, 256);

        SecretKeyFactory factory;
        try {
             factory = SecretKeyFactory.getInstance(getKeyAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            //Should never happen, as it's predefined
            e.printStackTrace();
            return;
        }

        //Create the pbkdf secret key
        SecretKey pbkdf;
        try {
             pbkdf = factory.generateSecret(spec);
        } catch (InvalidKeySpecException e) {
            //Should never happen, as it's predefined
            e.printStackTrace();
            return;
        }

        //"Convert" the secret key to a AES secret key
        SecretKey aes = new SecretKeySpec(pbkdf.getEncoded(), getAlgorithm());

        //Init the cipher instance
        cipher.init(Cipher.ENCRYPT_MODE, aes);
    }

    @Override
    public void write(int decryptedInt) throws IOException {
        byte[] decryptedByte = new byte[1];
        decryptedByte[0] = (byte)decryptedInt;
        byte[] encryptedByte = cipher.update(decryptedByte);
        int encryptedInt = encryptedByte[0];
        super.write(encryptedInt);
    }
}

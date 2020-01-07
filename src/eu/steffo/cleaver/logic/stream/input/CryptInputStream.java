package eu.steffo.cleaver.logic.stream.input;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class CryptInputStream extends FilterInputStream {
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


    public CryptInputStream(InputStream in, String key) throws InvalidKeyException {
        super(in);

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
        cipher.init(Cipher.DECRYPT_MODE, aes);
    }

    @Override
    public int read() throws IOException {
        int encryptedInt = super.read();
        byte[] encryptedByte = new byte[1];
        encryptedByte[0] = (byte)encryptedInt;
        byte[] decryptedByte = cipher.update(encryptedByte);
        return decryptedByte[0];
    }
}

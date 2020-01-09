package eu.steffo.cleaver.logic.stream.input;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class CleaverCryptInputStream extends FilterInputStream implements ICleaverInputStream {
    private Cipher cipher;

    /**
     * The algorithm used for the encryption (<a href="https://en.wikipedia.org/wiki/Advanced_Encryption_Standard">Advanced Encryption Standard</a>).
     */
    private final String encryptionAlgorithm = "AES";

    /**
     * The mode of operation used for the encryption (<a href="https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Cipher_Feedback_(CFB)">Cipher FeedBack</a> with 8-bit blocks).
     */
    private final String modeOfOperation = "CFB8";

    /**
     * The padding standard used for the encryption (none, as there's no need for it with 8-bit blocks).
     */
    private final String padding = "NoPadding";

    /**
     * The size in bytes of the <a href="https://en.wikipedia.org/wiki/Salt_(cryptography)">salt</a>.
     */
    private final int saltSize = 8;

    /**
     * The name of the key derivation algorithm to be used (<a href="https://en.wikipedia.org/wiki/PBKDF2">Password-Based Key Derivation Function 2</a> with <a href="https://en.wikipedia.org/wiki/HMAC">HMAC</a>-<a href="https://it.wikipedia.org/wiki/Secure_Hash_Algorithm">SHA512</a>).
     */
    private final String keyDerivationAlgorithm = "PBKDF2WithHmacSHA512";

    /**
     * The iteration count for the {@link #keyDerivationAlgorithm}.
     */
    private final int keyIterationCount = 65535;

    /**
     * The length in bits of the key to be generated with the {@link #keyDerivationAlgorithm}.
     */
    private final int keyLength = 256;

    /**
     * The size in bytes of the initialization vector.
     */
    private final int ivSize = 16;
    /**
     * @return The full transformation string as required by {@link Cipher#getInstance(String)}.
     */
    public String getTransformationString() {
        return String.format("%s/%s/%s", encryptionAlgorithm, modeOfOperation, padding);
    }

    /**
     * Generate a key starting from a character array.
     * @throws NoSuchAlgorithmException If the {@link #keyDerivationAlgorithm} is invalid.
     * @throws InvalidKeySpecException If the generated {@link KeySpec} is invalid.
     */
    private SecretKey generatePasswordKey(char[] key, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(key, salt, keyIterationCount, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(keyDerivationAlgorithm);
        return factory.generateSecret(spec);
    }

    /**
     * Create and initialize the {@link Cipher} {@link #cipher} to be used by the CleaverCryptOutputStream.
     * @param key The string to be used in the {@link Cipher} as encryption key.
     * @throws NoSuchPaddingException If the {@link #padding} is invalid.
     * @throws NoSuchAlgorithmException If the {@link #encryptionAlgorithm} is invalid.
     * @throws InvalidKeySpecException If the generated {@link KeySpec} is invalid.
     */
    private void initCipher(char[] key, byte[] salt, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException {
        //Setup the cipher object
        cipher = Cipher.getInstance(getTransformationString());

        //"Convert" the secret key to a AES secret key
        SecretKey aes = new SecretKeySpec(generatePasswordKey(key, salt).getEncoded(), encryptionAlgorithm);

        //Init the cipher instance
        cipher.init(Cipher.DECRYPT_MODE, aes, new IvParameterSpec(iv));
    }

    protected CleaverCryptInputStream(InputStream in, char[] key, byte[] salt, byte[] iv) {
        super(in);
        try {
            initCipher(key, salt, iv);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            //This should never happen...
            e.printStackTrace();
        }
    }

    @Override
    public int read() throws IOException {
        int encryptedInt = super.read();
        //End of file
        if(encryptedInt == -1) {
            return -1;
        }
        byte[] encryptedByte = new byte[1];
        encryptedByte[0] = (byte)encryptedInt;
        byte[] decryptedByte = cipher.update(encryptedByte);
        return decryptedByte[0];
    }
}

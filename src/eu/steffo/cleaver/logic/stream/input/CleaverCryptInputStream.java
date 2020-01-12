package eu.steffo.cleaver.logic.stream.input;

import eu.steffo.cleaver.errors.ProgrammingError;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.Objects;

/**
 * A {@link ICleaverInputStream} that decrypts incoming data using a {@link Cipher} object.
 */
public class CleaverCryptInputStream extends FilterInputStream implements ICleaverInputStream {
    /**
     * The {@link Cipher} to use to decrypt the data received in input.
     */
    private final Cipher cipher;

    /**
     * The algorithm used for the encryption (<a href="https://en.wikipedia.org/wiki/Advanced_Encryption_Standard">Advanced Encryption Standard</a>).
     */
    private static final String ENCRYPTION_ALGORITHM = "AES";

    /**
     * The mode of operation used for the encryption (<a href="https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Cipher_Feedback_(CFB)">Cipher FeedBack</a> with 8-bit blocks).
     */
    private static final String MODE_OF_OPERATION = "CFB8";

    /**
     * The padding standard used for the encryption (none, as there's no need for it when using 8-bit blocks).
     */
    private static final String PADDING = "NoPadding";

    /**
     * The name of the key derivation algorithm to be used (<a href="https://en.wikipedia.org/wiki/PBKDF2">Password-Based Key Derivation Function 2</a> with <a href="https://en.wikipedia.org/wiki/HMAC">HMAC</a>-<a href="https://it.wikipedia.org/wiki/Secure_Hash_Algorithm">SHA512</a>).
     */
    private static final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA512";

    /**
     * The iteration count for the {@link #KEY_DERIVATION_ALGORITHM}.
     */
    private static final int KEY_ITERATION_COUNT = 65535;

    /**
     * The length in bits of the key to be generated with the {@link #KEY_DERIVATION_ALGORITHM}.
     */
    private static final int KEY_LENGTH = 256;

    /**
     * @return The full transformation string as required by {@link Cipher#getInstance(String)}.
     */
    private static String getTransformationString() {
        return String.format("%s/%s/%s", ENCRYPTION_ALGORITHM, MODE_OF_OPERATION, PADDING);
    }

    /**
     * Generate a AES key from a password and a salt.
     * @param password The password to generate a key from.
     * @param salt The salt to use when generating the key.
     * @throws NoSuchAlgorithmException If the {@link #KEY_DERIVATION_ALGORITHM} is invalid.
     * @throws InvalidKeySpecException If the generated {@link KeySpec} is invalid.
     * @return The generated AES {@link SecretKey}.
     */
    private static SecretKey generatePasswordKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password, salt, KEY_ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
        return factory.generateSecret(spec);
    }

    /**
     * Create and initialize the {@link Cipher} to be used by the CleaverCryptOutputStream.
     * @param password The string to be used in the {@link Cipher} as encryption key.
     * @param salt The salt to use when generating the key.
     * @param iv The initialization vector to use when initializing the {@link Cipher}.
     * @return The initialized {@link Cipher}.
     * @throws ProgrammingError If something goes wrong while preparing the {@link Cipher}. (It should never happen.)
     */
    private static Cipher initCipher(char[] password, byte[] salt, byte[] iv) {
        //Setup the cipher object
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(getTransformationString());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {

            //Should never happen
            throw new ProgrammingError(e.toString());
        }

        //"Convert" the secret key to a AES secret key
        SecretKey aes;
        try {
            aes = new SecretKeySpec(generatePasswordKey(password, salt).getEncoded(), ENCRYPTION_ALGORITHM);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            //Should never happen
            throw new ProgrammingError(e.toString());
        }

        //Init the cipher instance
        try {
            cipher.init(Cipher.DECRYPT_MODE, aes, new IvParameterSpec(iv));
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            //Should never happen
            throw new ProgrammingError(e.toString());
        }

        return cipher;
    }

    /**
     * Create a new CleaverCryptInputStream wrapping another {@link InputStream}.
     * @param in The {@link InputStream} to wrap (it must implement {@link ICleaverInputStream}!).
     * @param password The password to decrypt the incoming data with.
     * @param salt The salt used to generate the AES key from the password. Should be 8 bytes long.
     * @param iv The initialization vector passed to the {@link Cipher} before starting the decryption. Should be 16 bytes long.
     */
    public CleaverCryptInputStream(InputStream in, char[] password, byte[] salt, byte[] iv) {
        super(in);

        if(!(in instanceof ICleaverInputStream)) {
            throw new IllegalArgumentException("The InputStream passed to the CleaverDeflateInputStream must implement ICleaverInputStream.");
        }

        cipher = initCipher(password, salt, iv);
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
        return ((int)decryptedByte[0] & 0xFF);
    }

    /**
     * Force reads of 1 byte at a time by overriding {@link FilterInputStream#read(byte[])} with the code from {@link InputStream#read(byte[])}.
     * @param b The buffer to read the data into.
     * @return The number of read bytes, or -1 if the end of the {@link #in} stream has been reached.
     * @throws IOException If an error occours during the read.
     */
    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    /**
     * Force reads of 1 byte at a time by overriding {@link FilterInputStream#read(byte[], int, int)} with the code from
     * {@link InputStream#read(byte[], int, int)}.
     * @param b The buffer to read the data into.
     * @param off The first position of the <code>b</code> buffer where the data should be read into.
     * @param len The maximum number of bytes to read.
     * @return The number of read bytes, or -1 if the end of the {@link #in} stream has been reached.
     * @throws IOException If an error occours during the read.
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte)c;

        int i = 1;
        try {
            for (; i < len ; i++) {
                c = read();
                if (c == -1) {
                    break;
                }
                b[off + i] = (byte)c;
            }
        } catch (IOException ignored) {
        }
        return i;
    }
}

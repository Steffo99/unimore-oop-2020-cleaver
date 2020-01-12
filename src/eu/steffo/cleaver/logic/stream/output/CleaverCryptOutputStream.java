package eu.steffo.cleaver.logic.stream.output;

import eu.steffo.cleaver.errors.ProgrammingError;
import eu.steffo.cleaver.logic.utils.SaltSerializer;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.Objects;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A {@link ICleaverOutputStream} that encrypts incoming data using a {@link Cipher} object.
 */
public class CleaverCryptOutputStream extends FilterOutputStream implements ICleaverOutputStream {
    /**
     * The {@link Cipher} to use to decrypt the data received in input.
     */
    private Cipher cipher;

    /**
     * A byte array to use as <a href="https://en.wikipedia.org/wiki/Salt_(cryptography)">salt</a> for the key generation.
     */
    private byte[] salt;

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
     * The size in bytes of the <a href="https://en.wikipedia.org/wiki/Salt_(cryptography)">salt</a>.
     */
    private static final int SALT_SIZE = 8;

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
     * The size in bytes of the initialization vector.
     */
    private static final int IV_SIZE = 16;

    /**
     * @return The full transformation string as required by {@link Cipher#getInstance(String)}.
     */
    public String getTransformationString() {
        return String.format("%s/%s/%s", ENCRYPTION_ALGORITHM, MODE_OF_OPERATION, PADDING);
    }

    /**
     * Generate an array of secure random bytes.
     * @param size The size of the array.
     * @return The generated array of secure random bytes.
     * @see SecureRandom
     */
    protected static byte[] generateSecure(int size) {
        byte[] salt = new byte[size];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        return salt;
    }

    /**
     * Generate a new <a href="https://en.wikipedia.org/wiki/Initialization_vector">Initialization Vector</a> with the specified size.
     * @param size The size in bytes of the initialization vector.
     * @return The generated IV.
     */
    protected static IvParameterSpec generateIV(int size) {
        return new IvParameterSpec(generateSecure(size));
    }

    /**
     * Generate a AES key from a password and a salt.
     * @param password The password to generate a key from.
     * @param salt The salt to use when generating the key.
     * @throws NoSuchAlgorithmException If the {@link #KEY_DERIVATION_ALGORITHM} is invalid.
     * @throws InvalidKeySpecException If the generated {@link KeySpec} is invalid.
     * @return The generated AES {@link SecretKey}.
     */
    private SecretKey generatePasswordKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password, salt, KEY_ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
        return factory.generateSecret(spec);
    }

    /**
     * Create and initialize the {@link Cipher} {@link #cipher} to be used by the CleaverCryptOutputStream.
     * @param password The password to generate a key from.
     * @throws ProgrammingError If something goes wrong while preparing the {@link Cipher}. (It should never happen.)
     */
    private void initCipher(char[] password) {
        //Setup the cipher object
        try {
            cipher = Cipher.getInstance(getTransformationString());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            //Should never happen
            throw new ProgrammingError(e.toString());
        }

        //Generate the salt
        salt = generateSecure(SALT_SIZE);

        //"Convert" the secret key to a AES secret key
        SecretKey aes;
        try {
            aes = new SecretKeySpec(generatePasswordKey(password, salt).getEncoded(), ENCRYPTION_ALGORITHM);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            //Should never happen
            throw new ProgrammingError(e.toString());
        }

        //Generate the initialization vector
        IvParameterSpec iv = generateIV(IV_SIZE);

        //Init the cipher instance
        try {
            cipher.init(Cipher.ENCRYPT_MODE, aes, iv);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            //Should never happen
            throw new ProgrammingError(e.toString());
        }
    }

    /**
     * Create a new CleaverCryptOutputStream with default {@link Cipher} parameters (AES algorithm in operation mode CFB8 with PKCS5 padding).
     *
     * @param out The {@link OutputStream} to connect this {@link FilterOutputStream} to.
     * @param key The desired encryption key.
     */
    public CleaverCryptOutputStream(OutputStream out, char[] key) {
        super(out);
        initCipher(key);
    }

    @Override
    public void write(int decryptedInt) throws IOException {
        byte[] decryptedByte = new byte[1];
        decryptedByte[0] = (byte)decryptedInt;
        byte[] encryptedByte = cipher.update(decryptedByte);
        int encryptedInt = encryptedByte[0];
        super.write(encryptedInt);
    }

    /**
     * Force writes of 1 byte at a time by overriding {@link FilterOutputStream#write(byte[])} with the code from
     * {@link OutputStream#write(byte[])}.
     * @param b The buffer from where the data should be written.
     * @throws IOException If an error occours during the write.
     */
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    /**
     * Force writes of 1 byte at a time by overriding {@link FilterOutputStream#write(byte[], int, int)} with the code from
     * {@link OutputStream#write(byte[], int, int)}.
     * @param b The buffer from where the data should be written.
     * @param off The first position of the {@literal b} buffer from where the data should be written.
     * @param len The maximum number of bytes to write.
     * @throws IOException If an error occours during the write.
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        for (int i = 0 ; i < len ; i++) {
            write(b[off + i]);
        }
    }


    @Override
    public Element toElement(Document doc) {
        Element element = doc.createElement("Crypt");

        Element child = ((ICleaverOutputStream)out).toElement(doc);
        element.appendChild(child);

        Attr algorithmAttr = doc.createAttribute("algorithm");
        algorithmAttr.setValue(ENCRYPTION_ALGORITHM);
        element.setAttributeNode(algorithmAttr);

        Attr modeOfOperationAttr = doc.createAttribute("mode-of-operation");
        modeOfOperationAttr.setValue(MODE_OF_OPERATION);
        element.setAttributeNode(modeOfOperationAttr);

        Attr paddingAttr = doc.createAttribute("padding");
        paddingAttr.setValue(PADDING);
        element.setAttributeNode(paddingAttr);

        Attr keyAlgorithmAttr = doc.createAttribute("key-algorithm");
        keyAlgorithmAttr.setValue(KEY_DERIVATION_ALGORITHM);
        element.setAttributeNode(keyAlgorithmAttr);

        Attr iterationCountAttr = doc.createAttribute("iteration-count");
        iterationCountAttr.setValue(Integer.toString(KEY_ITERATION_COUNT));
        element.setAttributeNode(iterationCountAttr);

        Attr keyLengthAttr = doc.createAttribute("key-length");
        keyLengthAttr.setValue(Integer.toString(KEY_LENGTH));
        element.setAttributeNode(keyLengthAttr);

        Attr ivAttr = doc.createAttribute("iv");
        ivAttr.setValue(SaltSerializer.serialize(cipher.getIV()));
        element.setAttributeNode(ivAttr);

        Attr saltAttr = doc.createAttribute("salt");
        saltAttr.setValue(SaltSerializer.serialize(salt));
        element.setAttributeNode(saltAttr);

        return element;
    }
}

package eu.steffo.cleaver.logic.stream.output;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class CleaverCryptOutputStream extends FilterOutputStream implements ICleaverOutputStream {
    private Cipher cipher;

    /**
     * The algorithm used for the encryption (<a href="https://en.wikipedia.org/wiki/Advanced_Encryption_Standard">Advanced Encryption Standard</a>).
     */
    public static final String ENCRYPTION_ALGORITHM = "AES";

    /**
     * The mode of operation used for the encryption (<a href="https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Cipher_Feedback_(CFB)">Cipher FeedBack</a> with 8-bit blocks).
     */
    public static final String MODE_OF_OPERATION = "CFB8";

    /**
     * The padding standard used for the encryption (<a href="https://en.wikipedia.org/wiki/PKCS">PKCS#5</a>).
     */
    public static final String PADDING = "PKCS5Padding";

    /**
     * The size in bytes of the <a href="https://en.wikipedia.org/wiki/Salt_(cryptography)">salt</a>.
     */
    public static final int SALT_SIZE = 8;

    /**
     * The name of the key derivation algorithm to be used (<a href="https://en.wikipedia.org/wiki/PBKDF2">Password-Based Key Derivation Function 2</a> with <a href="https://en.wikipedia.org/wiki/HMAC">HMAC</a>-<a href="https://it.wikipedia.org/wiki/Secure_Hash_Algorithm>SHA512</a>).
     */
    public static final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA512";

    /**
     * The iteration count for the {@link #KEY_DERIVATION_ALGORITHM}.
     */
    public static final int KEY_ITERATION_COUNT = 65535;

    /**
     * The length in bits of the key to be generated with the {@link #KEY_DERIVATION_ALGORITHM}.
     */
    public static final int KEY_LENGTH = 512;

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
    protected byte[] generateSecure(int size) {
        byte[] salt = new byte[8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        return salt;
    }

    /**
     * Generate a new <a href="https://en.wikipedia.org/wiki/Initialization_vector">Initialization Vector</a> with the specified size.
     * @param size The size in bytes of the initialization vector.
     * @return The generated IV.
     */
    protected IvParameterSpec generateIV(int size) {
        return new IvParameterSpec(generateSecure(size));
    }

    /**
     * Generate a key starting from a character array.
     * @throws NoSuchAlgorithmException If the {@link #KEY_DERIVATION_ALGORITHM} is invalid.
     * @throws InvalidKeySpecException If the generated {@link KeySpec} is invalid.
     */
    private SecretKey generatePasswordKey(char[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(key, generateSecure(SALT_SIZE), KEY_ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
        return factory.generateSecret(spec);
    }

    /**
     * Create and initialize the {@link Cipher} {@link #cipher} to be used by the CleaverCryptOutputStream.
     * @param key The string to be used in the {@link Cipher} as encryption key.
     * @throws NoSuchPaddingException If the {@link #PADDING} is invalid.
     * @throws NoSuchAlgorithmException If the {@link #ENCRYPTION_ALGORITHM} is invalid.
     * @throws InvalidKeySpecException If the generated {@link KeySpec} is invalid.
     */
    private void initCipher(char[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException {
        //Setup the cipher object
        cipher = Cipher.getInstance(getTransformationString());

        //"Convert" the secret key to a AES secret key
        SecretKey aes = new SecretKeySpec(generatePasswordKey(key).getEncoded(), ENCRYPTION_ALGORITHM);

        //Generate the initialization vector
        IvParameterSpec iv = generateIV(1);

        //Init the cipher instance
        cipher.init(Cipher.ENCRYPT_MODE, aes, iv);
    }

    /**
     * Create a new CleaverCryptOutputStream with default {@link Cipher} parameters (AES algorithm in operation mode CFB8 with PKCS5 padding).
     *
     * @param out The {@link OutputStream} to connect this {@link FilterOutputStream} to.
     * @param key The desired encryption key.
     */
    public CleaverCryptOutputStream(OutputStream out, String key) {
        super(out);
        try {
            initCipher(key.toCharArray());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            //This should never happen...
            e.printStackTrace();
        }
    }

    @Override
    public void write(int decryptedInt) throws IOException {
        byte[] decryptedByte = new byte[1];
        decryptedByte[0] = (byte)decryptedInt;
        byte[] encryptedByte = cipher.update(decryptedByte);
        int encryptedInt = encryptedByte[0];
        super.write(encryptedInt);
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
        ivAttr.setValue(Byte.toString(cipher.getIV()[0]));
        element.setAttributeNode(ivAttr);

        return element;
    }
}

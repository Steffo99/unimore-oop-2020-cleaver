package eu.steffo.cleaver.logic.crypt;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A class containing the configuration for the encryption/decryption step of a {@link eu.steffo.cleaver.logic.Job Job}.
 */
public class CryptConfig {
    protected final String key;

    /**
     * Construct a new CryptConfig with a specific encryption key.
     * @param key The encryption key.
     */
    public CryptConfig(String key) {
        this.key = key;
    }

    /**
     * @return The encryption key.
     */
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "••••••••";
    }

    /**
     * Create a {@link Element} representing this CryptConfig (to be used in *.chp metadata files).
     * @param doc The {@link Document} the {@link Element} should be created in.
     * @return The created {@link Element}.
     * @see eu.steffo.cleaver.logic.ChopJob
     * @see eu.steffo.cleaver.logic.StitchJob
     */
    public Element toElement(Document doc) {
        return doc.createElement("Crypt");
    }
}

package eu.steffo.cleaver.logic.config;

import eu.steffo.cleaver.logic.job.Job;

/**
 * A class containing the configuration for the encryption/decryption step of a {@link Job Job}.
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
}

package eu.steffo.cleaver.logic.config;

/**
 * A config for encrypting a file with an arbitrary length password.
 */
public class PasswordConfig implements ICryptConfig {
    protected final String key;

    /**
     * Construct a new CryptConfig with a specific encryption key.
     * @param key The encryption key.
     */
    public PasswordConfig(String key) {
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

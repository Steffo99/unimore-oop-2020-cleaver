package eu.steffo.cleaver.logic.config;

/**
 * A {@link ICryptConfig} requesting the encryption of a file using a specific {@link #password}.
 */
public class PasswordConfig implements ICryptConfig {
    /**
     * The password to be used in the encryption.
     */
    private final String password;

    /**
     * Construct a new PasswordConfig with a specific password.
     * @param key The password to be used in the encryption.
     */
    public PasswordConfig(String key) {
        this.password = key;
    }

    /**
     * @return The password to be used in the encryption.
     */
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Encrypt";
    }
}

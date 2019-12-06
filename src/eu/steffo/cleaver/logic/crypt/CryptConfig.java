package eu.steffo.cleaver.logic.crypt;

public class CryptConfig {
    protected String key;

    public CryptConfig(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

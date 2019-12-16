package eu.steffo.cleaver.logic.crypt;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CryptConfig {
    protected String key;

    public CryptConfig(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "••••••••";
    }

    public Element toElement(Document doc) {
        return doc.createElement("Crypt");
    }
}

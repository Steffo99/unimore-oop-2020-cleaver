package eu.steffo.cleaver.logic.compress;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CompressConfig {
    @Override
    public String toString() {
        return "Yes";
    }

    public Element toElement(Document doc) {
        return doc.createElement("Compress");
    }
}

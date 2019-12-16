package eu.steffo.cleaver.logic.split;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SplitByPartsConfig extends SplitConfig {
    protected int parts;

    public SplitByPartsConfig(int parts) {
        this.parts = parts;
    }

    public int getParts() {
        return parts;
    }

    @Override
    public String toString() {
        return String.format("%d parts", this.parts);
    }

    @Override
    public Element toElement(Document doc) {
        Element element = doc.createElement("Split");

        Attr attr = doc.createAttribute("mode");
        attr.setValue("by-parts");
        element.setAttributeNode(attr);

        element.setTextContent(Integer.toString(parts));

        return element;
    }
}

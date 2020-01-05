package eu.steffo.cleaver.logic.split;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class SplitBySizeConfig extends SplitConfig {
    private long size;

    public SplitBySizeConfig(long size) {
        this.size = size;
    }

    /**
     * @return The size in bytes the file parts should be.
     */
    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return String.format("%d bytes", this.size);
    }

    @Override
    public Element toElement(Document doc) {
        Element element = doc.createElement("Split");

        Attr attr = doc.createAttribute("mode");
        attr.setValue("by-size");
        element.setAttributeNode(attr);

        element.setTextContent(Long.toString(size));

        return element;
    }
}

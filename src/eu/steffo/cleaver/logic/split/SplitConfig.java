package eu.steffo.cleaver.logic.split;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class SplitConfig {
    public abstract Element toElement(Document doc);
}

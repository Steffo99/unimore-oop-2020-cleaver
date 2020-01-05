package eu.steffo.cleaver.logic.compress;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A class containing the configuration for the compression step of a {@link eu.steffo.cleaver.logic.Job Job}.
 */
public class CompressConfig {
    @Override
    public String toString() {
        return "Yes (Deflate)";
    }

    /**
     * Create a {@link Element} representing this CompressConfig (to be used in *.chp metadata files).
     * @param doc The {@link Document} the {@link Element} should be created in.
     * @return The created {@link Element}.
     * @see eu.steffo.cleaver.logic.ChopJob
     * @see eu.steffo.cleaver.logic.StitchJob
     */
    public Element toElement(Document doc) {
        return doc.createElement("Compress");
    }
}

package eu.steffo.cleaver.logic.split;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A class containing the configuration for the split/merge step of a {@link eu.steffo.cleaver.logic.Job Job}.
 */
public abstract class SplitConfig {
    /**
     * Create a {@link Element} representing this SplitConfig (to be used in *.chp metadata files).
     * @param doc The {@link Document} the {@link Element} should be created in.
     * @return The created {@link Element}.
     * @see eu.steffo.cleaver.logic.ChopJob
     * @see eu.steffo.cleaver.logic.StitchJob
     */
    public abstract Element toElement(Document doc);
}

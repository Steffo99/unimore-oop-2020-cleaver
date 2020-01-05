package eu.steffo.cleaver.logic.split;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A class containing the configuration for the split/merge step of a {@link eu.steffo.cleaver.logic.Job Job}.
 */
public abstract class SplitConfig {
    /**
     * Create a {@link Element} representing this SplitConfig (to be used in *.chp metadata files).
     *
     * @param doc The {@link Document} the {@link Element} should be created in.
     * @return The created {@link Element}.
     *
     * @see eu.steffo.cleaver.logic.ChopJob
     * @see eu.steffo.cleaver.logic.StitchJob
     */
    public Element toElement(Document doc) {
        Element element = doc.createElement("Split");

        Attr partSizeAttr = doc.createAttribute("part-size");
        partSizeAttr.setValue(Long.toString(getPartSize()));
        element.setAttributeNode(partSizeAttr);

        Attr partCountAttr = doc.createAttribute("parts");
        partCountAttr.setValue(Long.toString(getPartCount()));
        element.setAttributeNode(partCountAttr);

        Attr totalSizeAttr = doc.createAttribute("total-size");
        totalSizeAttr.setValue(Long.toString(getPartCount()));
        element.setAttributeNode(totalSizeAttr);

        return element;
    }

    /**
     * @return The size in bytes of a single part of the file.
     */
    public abstract long getPartSize();

    /**
     * @return The number of parts the file should be split in.
     */
    public abstract int getPartCount();

    /**
     * @return The total size of the original file.
     */
    public abstract long getTotalSize();

    /**
     * @return The string representation of the {@link SplitConfig}, to be used in the jobs table.
     * @see eu.steffo.cleaver.gui.panels.JobsTablePanel
     */
    public abstract String toString();
}

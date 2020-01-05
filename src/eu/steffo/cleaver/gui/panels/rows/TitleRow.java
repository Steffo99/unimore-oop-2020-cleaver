package eu.steffo.cleaver.gui.panels.rows;

import javax.swing.*;

/**
 * A {@link Row} containing a label, to be used as a title.
 * @see eu.steffo.cleaver.gui.panels.CreateJobPanel
 */
public class TitleRow extends Row {
    /**
     * The title label.
     */
    protected final JLabel titleLabel;

    /**
     * Construct a TitleRow with a specific title.
     * @param str The title to display in the TitleRow.
     */
    public TitleRow(String str) {
        super();

        this.add(Box.createHorizontalStrut(8));

        titleLabel = new JLabel(str);
        this.add(titleLabel);

        this.add(Box.createHorizontalStrut(8));
    }
}

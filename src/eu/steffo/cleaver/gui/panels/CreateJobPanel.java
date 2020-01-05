package eu.steffo.cleaver.gui.panels;

import eu.steffo.cleaver.gui.panels.rows.FileSelectRow;
import eu.steffo.cleaver.gui.panels.rows.TitleRow;

import javax.swing.*;

/**
 * The base class for the two job creation panels ({@link ChopPanel} and {@link StitchPanel}).
 * It uses a <i>vertical</i> layout; therefore, {@link eu.steffo.cleaver.gui.panels.rows.Row Rows} are added to it.
 */
public abstract class CreateJobPanel extends JPanel {
    /**
     * The first row, containing the name of the panel ("Chop" or "Stitch")
     */
    protected final TitleRow titlePanel;

    /**
     * The second row, containing the file selector.
     */
    protected final FileSelectRow fileSelectPanel;

    /**
     * @return The {@link String} that should be displayed as title of the panel.
     */
    protected abstract String getPanelText();

    /**
     * Construct the job panel by setting its layout, adding a border, the {@link TitleRow} and the {@link FileSelectRow}.
     */
    public CreateJobPanel() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBorder(BorderFactory.createEtchedBorder());

        this.add(Box.createVerticalStrut(8));

        titlePanel = new TitleRow(this.getPanelText());
        this.add(titlePanel);

        this.add(Box.createVerticalStrut(8));

        fileSelectPanel = new FileSelectRow();
        this.add(fileSelectPanel);
    }
}

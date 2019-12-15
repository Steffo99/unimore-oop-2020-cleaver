package eu.steffo.cleaver.gui.panels;

import eu.steffo.cleaver.gui.rows.FileSelectRow;
import eu.steffo.cleaver.gui.rows.TitleRow;
import eu.steffo.cleaver.logic.Job;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class CreateJobPanel extends JPanel {
    protected final TitleRow titlePanel;
    protected final FileSelectRow fileSelectPanel;

    protected abstract String getPanelText();

    protected abstract Class<? extends Job> getJobClass();

    public CreateJobPanel(ActionListener onCreateJobClick) {
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

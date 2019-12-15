package eu.steffo.cleaver.gui.panels;

import eu.steffo.cleaver.gui.rows.CreateJobButtonRow;
import eu.steffo.cleaver.gui.rows.option.CryptRow;
import eu.steffo.cleaver.logic.Job;
import eu.steffo.cleaver.logic.StitchJob;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class StitchPanel extends CreateJobPanel {
    protected final CreateJobButtonRow createJobButtonPanel;
    protected final CryptRow cryptOptionPanel;

    @Override
    protected String getPanelText() {
        return "Stitch";
    }

    @Override
    protected Class<? extends Job> getJobClass() {
        return StitchJob.class;
    }

    public StitchPanel(ActionListener onCreateJobClick) {
        super(onCreateJobClick);

        this.add(Box.createVerticalStrut(8));
        this.add(Box.createVerticalStrut(24));
        this.add(Box.createVerticalStrut(8));

        cryptOptionPanel = new CryptRow();
        this.add(cryptOptionPanel);

        this.add(Box.createVerticalStrut(8));
        this.add(Box.createVerticalStrut(24));
        this.add(Box.createVerticalStrut(8));

        createJobButtonPanel = new CreateJobButtonRow(onCreateJobClick);
        this.add(createJobButtonPanel);

        this.add(Box.createVerticalStrut(8));

        fileSelectPanel.setFileFilter(new FileNameExtensionFilter("Cleaver Metadata (*.chp)", "chp"));
    }

    public void createAndAddJobs(ArrayList<Job> jobs) {
        File[] files = fileSelectPanel.getSelectedFiles();
        for(File file : files) {
            job
        }
    }
}

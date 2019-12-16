package eu.steffo.cleaver.gui.panels;

import eu.steffo.cleaver.errors.ChpFileError;
import eu.steffo.cleaver.errors.ProgrammingError;
import eu.steffo.cleaver.gui.rows.CreateJobButtonRow;
import eu.steffo.cleaver.gui.rows.option.KeyRow;
import eu.steffo.cleaver.logic.Job;
import eu.steffo.cleaver.logic.StitchJob;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class StitchPanel extends CreateJobPanel {
    protected final CreateJobButtonRow createJobButtonPanel;
    protected final KeyRow keyOptionRow;

    @Override
    protected String getPanelText() {
        return "Stitch";
    }

    public StitchPanel(ActionListener onCreateJobClick) {
        super(onCreateJobClick);

        this.add(Box.createVerticalStrut(8));
        this.add(Box.createVerticalStrut(24));
        this.add(Box.createVerticalStrut(8));

        keyOptionRow = new KeyRow();
        this.add(keyOptionRow);

        this.add(Box.createVerticalStrut(8));
        this.add(Box.createVerticalStrut(24));
        this.add(Box.createVerticalStrut(8));

        createJobButtonPanel = new CreateJobButtonRow(onCreateJobClick);
        this.add(createJobButtonPanel);

        this.add(Box.createVerticalStrut(8));

        fileSelectPanel.setFileFilter(new FileNameExtensionFilter("Cleaver Metadata (*.chp)", "chp"));
    }

    public void createAndAddJobs(ArrayList<Job> jobs, Runnable updateTable) {
        File[] files = fileSelectPanel.getSelectedFiles();
        for(File file : files) {
            try {
                Job job = new StitchJob(file, updateTable, keyOptionRow.getKey());
                jobs.add(job);
            } catch (ChpFileError ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ProgrammingError ex) {
                JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        fileSelectPanel.clearSelectedFiles();
    }
}

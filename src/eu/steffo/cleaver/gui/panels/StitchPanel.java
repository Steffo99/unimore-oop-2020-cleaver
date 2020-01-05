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

/**
 * The {@link CreateJobPanel} allowing the creation of {@link StitchJob StitchJobs}.
 */
public class StitchPanel extends CreateJobPanel {
    /**
     * The {@link eu.steffo.cleaver.gui.rows.Row Row} to select the encryption key.
     */
    protected final KeyRow keyOptionRow;

    /**
     * The {@link eu.steffo.cleaver.gui.rows.Row Row} containing the button to create the {@link StitchJob StitchJobs}.
     */
    protected final CreateJobButtonRow createJobButtonRow;

    @Override
    protected String getPanelText() {
        return "Stitch";
    }

    /**
     * Construct a StitchPanel.
     * @param onCreateJobClick The {@link ActionListener} that will be added to the button in the {@link #createJobButtonRow}.
     */
    public StitchPanel(ActionListener onCreateJobClick) {
        super();

        //Leave the empty space for the split row
        this.add(Box.createVerticalStrut(40));

        keyOptionRow = new KeyRow();
        this.add(keyOptionRow);

        //Leave the empty space for the compress row
        this.add(Box.createVerticalStrut(40));

        createJobButtonRow = new CreateJobButtonRow(onCreateJobClick);
        this.add(createJobButtonRow);

        this.add(Box.createVerticalStrut(8));

        fileSelectPanel.setFileFilter(new FileNameExtensionFilter("Cleaver Metadata (*.chp)", "chp"));
    }

    /**
     * Add to the {@link ArrayList jobs ArrayList} the {@link StitchJob StitchJobs} for the current settings.
     * @param jobs The {@link ArrayList} the {@link StitchJob StitchJobs} should be added to.
     * @param onProgressChange The function that should be invoked when the {@link Job} {@link eu.steffo.cleaver.logic.progress.Progress Progress} changes.
     */
    public void createAndAddJobs(ArrayList<Job> jobs, Runnable onProgressChange) {
        File[] files = fileSelectPanel.getSelectedFiles();
        for(File file : files) {
            try {
                Job job = new StitchJob(file, keyOptionRow.getKey(), onProgressChange);
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

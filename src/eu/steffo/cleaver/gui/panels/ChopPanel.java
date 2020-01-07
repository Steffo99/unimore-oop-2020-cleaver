package eu.steffo.cleaver.gui.panels;

import eu.steffo.cleaver.gui.panels.rows.CreateJobButtonRow;
import eu.steffo.cleaver.gui.panels.rows.option.CompressRow;
import eu.steffo.cleaver.gui.panels.rows.option.CryptRow;
import eu.steffo.cleaver.gui.panels.rows.option.SplitRow;
import eu.steffo.cleaver.logic.job.ChopJob;
import eu.steffo.cleaver.logic.job.Job;
import eu.steffo.cleaver.logic.config.CompressConfig;
import eu.steffo.cleaver.logic.config.CryptConfig;
import eu.steffo.cleaver.logic.config.SplitConfig;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * The {@link CreateJobPanel} allowing the creation of {@link ChopJob ChopJobs}.
 */
public class ChopPanel extends CreateJobPanel {
    /**
     * The {@link eu.steffo.cleaver.gui.panels.rows.Row Row} to enable/disable the file split functionality.
     */
    protected final SplitRow splitRow;

    /**
     * The {@link eu.steffo.cleaver.gui.panels.rows.Row Row} to enable/disable the encryption functionality.
     */
    protected final CryptRow cryptRow;

    /**
     * The {@link eu.steffo.cleaver.gui.panels.rows.Row Row} to enable/disable the compression functionality.
     */
    protected final CompressRow compressRow;

    /**
     * The {@link eu.steffo.cleaver.gui.panels.rows.Row Row} containing the button to create the {@link ChopJob ChopJobs}.
     */
    protected final CreateJobButtonRow createJobButtonRow;

    @Override
    protected String getPanelText() {
        return "Chop";
    }

    /**
     * Construct a ChopPanel.
     * @param onCreateJobClick The {@link ActionListener} that will be added to the button in the {@link #createJobButtonRow}.
     */
    public ChopPanel(ActionListener onCreateJobClick) {
        super();

        this.add(Box.createVerticalStrut(8));

        splitRow = new SplitRow();
        this.add(splitRow);

        this.add(Box.createVerticalStrut(8));

        cryptRow = new CryptRow();
        this.add(cryptRow);

        this.add(Box.createVerticalStrut(8));

        compressRow = new CompressRow();
        this.add(compressRow);

        this.add(Box.createVerticalStrut(8));

        createJobButtonRow = new CreateJobButtonRow(onCreateJobClick);
        this.add(createJobButtonRow);

        this.add(Box.createVerticalStrut(8));
    }

    /**
     * Add to the {@link ArrayList jobs ArrayList} the {@link ChopJob ChopJobs} for the current settings.
     * @param jobs The {@link ArrayList} the {@link ChopJob ChopJobs} should be added to.
     * @param onProgressChange The function that should be invoked when the {@link Job} {@link eu.steffo.cleaver.logic.progress.Progress Progress} changes.
     */
    public void createAndAddJobs(ArrayList<Job> jobs, Runnable onProgressChange) {
        if(fileSelectPanel.getSelectedFiles().length == 0) {
            JOptionPane.showMessageDialog(null, "No files selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        for(File file : fileSelectPanel.getSelectedFiles()) {

            SplitConfig sc;
            try {
                sc = splitRow.getSplitConfig(file.length());
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(null, "Invalid value in the Split fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            CryptConfig cc = cryptRow.getCryptConfig();

            CompressConfig zc = compressRow.getCompressConfig();

            Job job = new ChopJob(file, sc, cc, zc, onProgressChange);
            jobs.add(job);

        }
        fileSelectPanel.clearSelectedFiles();
    }
}

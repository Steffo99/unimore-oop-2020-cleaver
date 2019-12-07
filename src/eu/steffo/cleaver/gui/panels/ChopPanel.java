package eu.steffo.cleaver.gui.panels;

import eu.steffo.cleaver.logic.ChopJob;
import eu.steffo.cleaver.logic.Job;

import java.awt.event.ActionListener;

public class ChopPanel extends CreateJobPanel {

    @Override
    protected String getPanelText() {
        return "Chop";
    }

    @Override
    protected Class<? extends Job> getJobClass() {
        return ChopJob.class;
    }

    public ChopPanel(ActionListener onCreateJobClick) {
        super(onCreateJobClick);
    }
}

package eu.steffo.cleaver.gui;

import javax.swing.*;

public class JobsButtons extends JPanel {

    protected final JButton deleteButton;
    protected final JButton startButton;

    public JobsButtons() {
        super();

        this.add(Box.createHorizontalStrut(8));

        deleteButton = new JButton("Delete selected job");
        this.add(deleteButton);

        this.add(Box.createHorizontalStrut(8));

        startButton = new JButton("Start jobs");
        this.add(startButton);

        this.add(Box.createHorizontalStrut(8));
    }
}

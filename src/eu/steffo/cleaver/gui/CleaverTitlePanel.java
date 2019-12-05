package eu.steffo.cleaver.gui;

import javax.swing.*;

public class CleaverTitlePanel extends CleaverRowPanel {
    protected JLabel titleLabel;

    public CleaverTitlePanel(String str) {
        this.add(Box.createHorizontalStrut(8));

        titleLabel = new JLabel(str);
        this.add(titleLabel);

        this.add(Box.createHorizontalStrut(8));
    }
}

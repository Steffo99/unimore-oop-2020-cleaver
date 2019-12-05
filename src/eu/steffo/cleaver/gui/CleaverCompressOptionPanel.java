package eu.steffo.cleaver.gui;

import javax.swing.*;

public class CleaverCompressOptionPanel extends CleaverRowPanel {
    protected JCheckBox compressionCheckBox;

    public CleaverCompressOptionPanel() {
        this.add(Box.createHorizontalStrut(8));

        compressionCheckBox = new JCheckBox("Compress");
        this.add(compressionCheckBox);

        this.add(Box.createHorizontalStrut(8));
    }

    public boolean getCompressionEnabled() {
        return compressionCheckBox.isSelected();
    }
}

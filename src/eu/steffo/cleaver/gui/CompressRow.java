package eu.steffo.cleaver.gui;

import javax.swing.*;

public class CompressRow extends Row {
    protected final JCheckBox compressionCheckBox;

    public CompressRow() {
        super();

        this.add(Box.createHorizontalStrut(8));

        compressionCheckBox = new JCheckBox("Compress");
        this.add(compressionCheckBox);

        this.add(Box.createHorizontalStrut(8));
    }

    public boolean getCompressionEnabled() {
        return compressionCheckBox.isSelected();
    }
}

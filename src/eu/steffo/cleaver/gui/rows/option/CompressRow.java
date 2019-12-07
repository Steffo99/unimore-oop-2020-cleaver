package eu.steffo.cleaver.gui.rows.option;

import eu.steffo.cleaver.gui.rows.option.OptionRow;
import eu.steffo.cleaver.logic.compress.CompressConfig;

import javax.swing.*;

public class CompressRow extends OptionRow {
    protected final JCheckBox compressionCheckBox;

    public CompressRow() {
        super();

        this.add(Box.createHorizontalStrut(8));

        compressionCheckBox = new JCheckBox("Compress");
        this.add(compressionCheckBox);

        this.add(Box.createHorizontalStrut(8));
    }

    public CompressConfig getCompressConfig() {
        if(!compressionCheckBox.isSelected()) {
            return null;
        }
        return new CompressConfig();
    }

    public void setCompressConfig(CompressConfig cfg) {
        if(cfg == null) {
            compressionCheckBox.setSelected(false);
        }
        else {
            compressionCheckBox.setSelected(true);
        }
        this.updateEnabledState();
    }

    @Override
    public void setEditable(boolean value) {
        compressionCheckBox.setEnabled(value);
        this.updateEnabledState();
    }

    @Override
    protected void updateEnabledState() {}
}

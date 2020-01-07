package eu.steffo.cleaver.gui.panels.rows.option;

import eu.steffo.cleaver.logic.config.CompressConfig;

import javax.swing.*;

/**
 * A {@link OptionRow} allowing the {@link CompressConfig configuration of the compress step} of the file chop process.
 *
 * @see eu.steffo.cleaver.gui.panels.ChopPanel
 */
public class CompressRow extends OptionRow {
    /**
     * The checkbox enabling or disabling the crypt step.
     *
     * If unticked, {@link #getCompressConfig()} will return {@literal null}.
     */
    protected final JCheckBox compressionCheckBox;

    /**
     * Construct a CompressRow.
     */
    public CompressRow() {
        super();

        this.add(Box.createHorizontalStrut(8));

        compressionCheckBox = new JCheckBox("Compress");
        this.add(compressionCheckBox);

        this.add(Box.createHorizontalStrut(8));
    }

    /**
     * Create a {@link CompressConfig} from the settings in this {@link OptionRow}.
     * @return The resulting {@link CompressConfig}, or {@literal null} if the {@link #compressionCheckBox} is unticked.
     */
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
    }

    @Override
    public void setEditable(boolean value) {
        compressionCheckBox.setEnabled(value);
    }
}

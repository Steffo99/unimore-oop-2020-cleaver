package eu.steffo.cleaver.gui.rows.option;

import eu.steffo.cleaver.logic.crypt.CryptConfig;

import javax.swing.*;
import java.awt.*;

public class KeyRow extends OptionRow {
    protected final JLabel keyLabel;
    protected final JTextField keyTextField;
    protected final JLabel optionalLabel;

    public KeyRow() {
        super();

        this.add(Box.createHorizontalStrut(8));

        keyLabel = new JLabel("Encryption key");
        this.add(keyLabel);

        this.add(Box.createHorizontalStrut(8));

        keyTextField = new JTextField();
        this.add(keyTextField);

        this.add(Box.createHorizontalStrut(8));

        optionalLabel = new JLabel("(if required)");
        this.add(optionalLabel);

        this.add(Box.createHorizontalStrut(8));

        this.updateEnabledState();
    }

    public String getKey() {
        return keyTextField.getText();
    }

    public void setKey(String key) {
        keyTextField.setText(key);
    }

    @Override
    public void setEditable(boolean value) {
        keyTextField.setEditable(value);
        this.updateEnabledState();
    }

    @Override
    protected void updateEnabledState() {
        keyTextField.setEnabled(keyTextField.isEditable());
    }
}

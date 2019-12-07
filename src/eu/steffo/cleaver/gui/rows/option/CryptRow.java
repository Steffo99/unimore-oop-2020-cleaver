package eu.steffo.cleaver.gui.rows.option;

import eu.steffo.cleaver.gui.rows.option.OptionRow;
import eu.steffo.cleaver.logic.crypt.CryptConfig;

import javax.swing.*;

public class CryptRow extends OptionRow {
    protected final JCheckBox cryptCheckBox;
    protected final JSeparator separator;
    protected final JLabel keyLabel;
    protected final JTextField keyTextField;

    public CryptRow() {
        super();

        this.add(Box.createHorizontalStrut(8));

        cryptCheckBox = new JCheckBox("Encrypt");
        cryptCheckBox.addActionListener(e -> updateEnabledState());
        this.add(cryptCheckBox);

        this.add(Box.createHorizontalStrut(8));

        separator = new JSeparator(JSeparator.VERTICAL);
        this.add(separator);

        this.add(Box.createHorizontalStrut(8));

        keyLabel = new JLabel("Key");
        this.add(keyLabel);

        this.add(Box.createHorizontalStrut(8));

        keyTextField = new JTextField();
        this.add(keyTextField);

        this.add(Box.createHorizontalStrut(8));

        this.updateEnabledState();
    }

    public CryptConfig getCryptConfig() {
        if(!cryptCheckBox.isSelected()) {
            return null;
        }
        return new CryptConfig(keyTextField.getText());
    }

    public void setCryptConfig(CryptConfig cfg) {
        if(cfg == null) {
            cryptCheckBox.setSelected(false);
            keyTextField.setText("");
        }
        else {
            cryptCheckBox.setSelected(true);
            keyTextField.setText(cfg.getKey());
        }
        this.updateEnabledState();
    }

    @Override
    public void setEditable(boolean value) {
        cryptCheckBox.setEnabled(value);
        this.updateEnabledState();
    }

    @Override
    protected void updateEnabledState() {
        keyTextField.setEnabled(cryptCheckBox.isEnabled());
        keyTextField.setEditable(cryptCheckBox.isSelected());
    }
}

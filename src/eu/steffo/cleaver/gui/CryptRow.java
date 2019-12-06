package eu.steffo.cleaver.gui;

import javax.swing.*;

public class CryptRow extends Row {
    protected final JCheckBox cryptCheckBox;
    protected final JSeparator separator;
    protected final JLabel keyLabel;
    protected final JTextField keyTextField;

    public CryptRow() {
        super();

        this.add(Box.createHorizontalStrut(8));

        cryptCheckBox = new JCheckBox("Encrypt");
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
    }

    public boolean getEncryptionEnabled() {
        return cryptCheckBox.isSelected();
    }
}

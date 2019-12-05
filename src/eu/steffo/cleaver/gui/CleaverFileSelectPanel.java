package eu.steffo.cleaver.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

public class CleaverFileSelectPanel extends JPanel {
    protected JFileChooser fileChooser;
    protected JButton selectFilesButton;
    protected JTextField selectedFilesText;
    protected File[] selectedFiles;

    public CleaverFileSelectPanel() {
        super();

        this.setLayout(new FlowLayout());

        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        selectFilesButton = new JButton("Select files...");
        selectFilesButton.addActionListener(e -> {
            fileChooser.showOpenDialog(this);

            selectedFiles = fileChooser.getSelectedFiles();

            StringBuilder displayedText = new StringBuilder();
            for (File file : selectedFiles) {
                displayedText.append(file.getName());
                displayedText.append("; ");
            }

            selectedFilesText.setText(displayedText.toString());
        });
        this.add(selectFilesButton);

        selectedFiles = new File[0];

        selectedFilesText = new JTextField();
        selectedFilesText.setPreferredSize(new Dimension(200, 24));
        selectedFilesText.setEditable(false);
        this.add(selectedFilesText);
    }

    public File[] getSelectedFiles() {
        return selectedFiles;
    }
}

package eu.steffo.cleaver.gui;

import java.awt.*;
import javax.swing.*;
import java.io.File;

public class CleaverFileSelectPanel extends CleaverRowPanel {
    protected JFileChooser fileChooser;
    protected JButton selectFilesButton;
    protected JTextField selectedFilesText;
    protected File[] selectedFiles;

    public CleaverFileSelectPanel() {
        super();

        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        this.add(Box.createHorizontalStrut(8));

        selectFilesButton = new JButton("Select files...");
        selectFilesButton.addActionListener(e -> {
            fileChooser.showOpenDialog(this);

            selectedFiles = fileChooser.getSelectedFiles();

            StringBuilder displayedText = new StringBuilder();
            for (File file : selectedFiles) {
                displayedText.append("\"");
                displayedText.append(file.getName());
                displayedText.append("\" ");
            }

            selectedFilesText.setText(displayedText.toString());
        });
        this.add(selectFilesButton);
        selectedFiles = new File[0];

        this.add(Box.createHorizontalStrut(8));

        selectedFilesText = new JTextField();
        selectedFilesText.setMinimumSize(new Dimension(200, 24));
        selectedFilesText.setPreferredSize(new Dimension(200, 24));
        selectedFilesText.setEditable(false);
        this.add(selectedFilesText);

        this.add(Box.createHorizontalStrut(8));
    }

    public File[] getSelectedFiles() {
        return selectedFiles;
    }
}

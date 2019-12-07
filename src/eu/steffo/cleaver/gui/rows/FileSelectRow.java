package eu.steffo.cleaver.gui.rows;

import eu.steffo.cleaver.gui.rows.Row;

import java.awt.*;
import javax.swing.*;
import java.io.File;

public class FileSelectRow extends Row {
    protected final JFileChooser fileChooser;
    protected final JButton selectFilesButton;
    protected final JTextField selectedFilesText;

    public FileSelectRow() {
        super();

        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        this.add(Box.createHorizontalStrut(8));

        selectedFilesText = new JTextField();
        selectedFilesText.setMinimumSize(new Dimension(200, 24));
        selectedFilesText.setPreferredSize(new Dimension(200, 24));
        selectedFilesText.setEditable(false);

        selectFilesButton = new JButton("Select files...");
        selectFilesButton.addActionListener(e -> {
            fileChooser.showOpenDialog(this);
            this.update();
        });
        this.add(selectFilesButton);

        this.add(Box.createHorizontalStrut(8));

        this.add(selectedFilesText);

        this.add(Box.createHorizontalStrut(8));
    }

    public File[] getSelectedFiles() {
        return fileChooser.getSelectedFiles();
    }

    protected void update() {
        StringBuilder displayedText = new StringBuilder();
        for (File file : fileChooser.getSelectedFiles()) {
            displayedText.append("\"");
            displayedText.append(file.getName());
            displayedText.append("\" ");
        }
        selectedFilesText.setText(displayedText.toString());
    }

    public void clearSelectedFiles() {
        fileChooser.setSelectedFiles(new File[0]);
        update();
    }
}

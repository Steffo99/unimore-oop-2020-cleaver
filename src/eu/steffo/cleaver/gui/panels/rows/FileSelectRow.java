package eu.steffo.cleaver.gui.panels.rows;

import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * A {@link Row} allowing for the selection of one or multiple files through a {@link JFileChooser}.
 *
 * <p><img src="doc-files/fileselectrow.png" alt=""></p>
 *
 * @see eu.steffo.cleaver.gui.panels.CreateJobPanel
 */
public class FileSelectRow extends Row {
    /**
     * A non-editable text field that displays the files selected in the file choice dialog.
     */
    protected final JTextField selectionTextField;

    /**
     * The button to open the file choice dialog.
     */
    protected final JButton openFileChooserButton;

    /**
     * The {@link JFileChooser} that will be opened when the {@link #openFileChooserButton} is pressed.
     */
    protected final JFileChooser fileChooser;

    /**
     * Construct a FileSelectRow and set all properties of its elements.
     */
    public FileSelectRow() {
        super();

        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setCurrentDirectory(new File("."));

        this.add(Box.createHorizontalStrut(8));

        selectionTextField = new JTextField();
        selectionTextField.setMinimumSize(new Dimension(200, 24));
        selectionTextField.setPreferredSize(new Dimension(200, 24));
        selectionTextField.setEditable(false);

        openFileChooserButton = new JButton("Select files...");
        openFileChooserButton.addActionListener(e -> {
            fileChooser.showOpenDialog(this);
            this.update();
        });
        this.add(openFileChooserButton);

        this.add(Box.createHorizontalStrut(8));

        this.add(selectionTextField);

        this.add(Box.createHorizontalStrut(8));
    }

    /**
     * Update the text displayed in the {@link #selectionTextField} with the current selected files.
     *
     * This method is called by the {@link java.awt.event.ActionListener} of the {@link #openFileChooserButton} after the file choice dialog is closed.
     */
    protected void update() {
        StringBuilder displayedText = new StringBuilder();
        for (File file : fileChooser.getSelectedFiles()) {
            displayedText.append("\"");
            displayedText.append(file.getName());
            displayedText.append("\" ");
        }
        selectionTextField.setText(displayedText.toString());
    }

    /**
     * @return The array of {@link File Files} that were selected in the {@link #fileChooser}.
     */
    public File[] getSelectedFiles() {
        return fileChooser.getSelectedFiles();
    }

    /**
     * Clear the files selected in the {@link #fileChooser}, and {@link #update() update} the text displayed.
     */
    public void clearSelectedFiles() {
        fileChooser.setSelectedFiles(new File[0]);
        update();
    }

    /**
     * Set a specific {@link FileFilter} in the {@link #fileChooser}.
     *
     * Used by the {@link eu.steffo.cleaver.gui.panels.StitchPanel StitchPanel} to restrict the selection to only {@literal *.chp} files.
     *
     * @param filter The filter to set.
     */
    public void setFileFilter(FileFilter filter) {
        fileChooser.setFileFilter(filter);
    }
}

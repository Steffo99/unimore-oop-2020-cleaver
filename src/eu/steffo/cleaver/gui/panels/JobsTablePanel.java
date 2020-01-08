package eu.steffo.cleaver.gui.panels;

import eu.steffo.cleaver.logic.job.Job;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * The {@link JPanel Panel} containing the jobs {@link JTable Table}.
 */
public class JobsTablePanel extends JPanel {
    /**
     * The {@link JScrollPane} wrapping the jobs {@link JTable Table}.
     */
    protected final JScrollPane tableScrollPane;

    /**
     * The {@link JobsTableModel TableModel} instance for the {@link JTable Table}.
     */
    protected final JobsTableModel tableModel;

    /**
     * The jobs table.
     */
    protected final JTable table;

    /**
     * A reference to the {@link ArrayList} of {@link Job Jobs} that should be displayed.
     */
    protected final ArrayList<Job> jobs;

    /**
     * The {@link javax.swing.table.TableModel} of the jobs table.
     *
     * It is an inner class.
     */
    public class JobsTableModel extends AbstractTableModel {
        @Override
        public int getRowCount() {
            return jobs.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch(columnIndex) {
                case 0: return "Type";
                case 1: return "File";
                case 2: return "Process";
                case 3: return "Progress";
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Job job = jobs.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return job.getTypeString();
                case 1:
                    return job.getFileString();
                case 2:
                    return job.getProcessString();
                case 3:
                    return job.getProgress().toString();
            }
            return "Unknown";
        }
    }

    /**
     * Construct a JobsTablePanel.
     * @param jobs A reference to the {@link ArrayList} of {@link Job Jobs} that should be displayed in the table.
     */
    public JobsTablePanel(ArrayList<Job> jobs) {
        super();

        this.jobs = jobs;

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        this.add(Box.createHorizontalStrut(4));

        tableModel = new JobsTableModel();
        table = new JTable(tableModel);
        tableScrollPane = new JScrollPane(table);
        this.add(tableScrollPane, BorderLayout.CENTER);

        this.add(Box.createHorizontalStrut(4));
    }

    /**
     * @return The array of the indexes of the jobs that are currently selected in the table.
     */
    public int[] getSelectedJobsIndexes() {
        return table.getSelectedRows();
    }

    /**
     * Refresh the table, updating all data inside it.
     */
    public void updateTable() {
        tableModel.fireTableDataChanged();
    }
}

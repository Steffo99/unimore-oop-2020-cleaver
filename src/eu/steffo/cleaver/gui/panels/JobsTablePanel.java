package eu.steffo.cleaver.gui.panels;

import eu.steffo.cleaver.logic.Job;
import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.split.SplitConfig;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

public class JobsTablePanel extends JPanel {
    protected final JScrollPane tableScrollPane;
    protected final JobsTableModel tableModel;
    protected final JTable table;
    protected final ArrayList<Job> jobs;

    public class JobsTableModel extends AbstractTableModel {
        @Override
        public int getRowCount() {
            return jobs.size();
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch(columnIndex) {
                case 0: return "Type";
                case 1: return "File";
                case 2: return "Split";
                case 3: return "Crypt";
                case 4: return "Compress";
                case 5: return "Progress";
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
                    return job.getType();
                case 1:
                    return job.getFile().getAbsolutePath();
                case 2:
                    SplitConfig s = job.getSplitConfig();
                    if(s == null) return "";
                    return s.toString();
                case 3:
                    CryptConfig k = job.getCryptConfig();
                    if(k == null) return "";
                    return k.toString();
                case 4:
                    CompressConfig c = job.getCompressConfig();
                    if(c == null) return "";
                    return c.toString();
                case 5:
                    return job.getProgress().toString();

            }
            return "Unknown";
        }
    }


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

    public int[] getSelectedJobsIndexes() {
        return table.getSelectedRows();
    }

    public void updateTableChanged() {
        tableModel.fireTableDataChanged();
    }
}

package eu.steffo.cleaver.gui;

import eu.steffo.cleaver.logic.Job;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;

public class JobsTablePanel extends JPanel {
    protected final JScrollPane tableScrollPane;
    protected final JTable table;
    protected final ArrayList<Job> jobs;

    public class JobsTableModel implements TableModel {
        protected final ArrayList<Job> jobs;

        public JobsTableModel(ArrayList<Job> jobs) {
            this.jobs = jobs;
        }

        @Override
        public int getRowCount() {
            return jobs.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch(columnIndex) {
                case 0: return "File";
                case 1: return "Split";
                case 2: return "Crypt";
                case 3: return "Compress";
                case 4: return "Progress";
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
            try {
                switch (columnIndex) {
                    case 0:
                        return job.getFile().getName();
                    case 1:
                        return job.getSplitConfig().toString();
                    case 2:
                        return job.getCryptConfig().toString();
                    case 3:
                        return job.getCompressConfig().toString();
                    case 4:
                        return "TODO";  // TODO
                }
            } catch (NullPointerException exc) {
                return "nullptr";
            }
            return "null";
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}

        @Override
        public void addTableModelListener(TableModelListener l) {}

        @Override
        public void removeTableModelListener(TableModelListener l) {}
    }


    public JobsTablePanel(ArrayList<Job> jobs) {
        super();

        this.jobs = jobs;

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        this.add(Box.createHorizontalStrut(4));

        table = new JTable(new JobsTableModel(jobs));
        tableScrollPane = new JScrollPane(table);
        this.add(tableScrollPane, BorderLayout.CENTER);

        this.add(Box.createHorizontalStrut(4));
    }

}

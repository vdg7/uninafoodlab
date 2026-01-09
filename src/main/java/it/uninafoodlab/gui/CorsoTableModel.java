package it.uninafoodlab.gui;

import it.uninafoodlab.domain.Corso;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CorsoTableModel extends AbstractTableModel {

    private final String[] columns = {"Titolo", "Tema", "Data Inizio", "Frequenza"};
    private List<Corso> corsi;

    public CorsoTableModel(List<Corso> corsi) {
        this.corsi = corsi;
    }

    public void setCorsi(List<Corso> corsi) {
        this.corsi = corsi;
        fireTableDataChanged();
    }

    public Corso getCorsoAt(int row) {
        return corsi.get(row);
    }

    @Override
    public int getRowCount() {
        return corsi == null ? 0 : corsi.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Corso c = corsi.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> c.getTitolo();
            case 1 -> c.getTema();
            case 2 -> c.getDataInizio();
            case 3 -> c.getFrequenzaSettimanale();
            default -> "";
        };
    }
}

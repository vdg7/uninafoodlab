package it.uninafoodlab.gui;

import it.uninafoodlab.controller.CorsoController;
import it.uninafoodlab.dao.CorsoDao;
import it.uninafoodlab.domain.Chef;
import it.uninafoodlab.domain.Corso;
import it.uninafoodlab.gui.CorsoTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {

    private final Chef chef;
    private final CorsoController corsoController;

    private CorsoTableModel tableModel;
    private JTextField temaField;

    public DashboardFrame(Chef chef) {
        this.chef = chef;
        this.corsoController = new CorsoController(new CorsoDao());
        initUI();
        loadCorsi();
    }

    private void initUI() {
        setTitle("Dashboard - " + chef.getNome());
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // TOP: filtro
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Tema:"));
        temaField = new JTextField(15);
        topPanel.add(temaField);

        JButton filterButton = new JButton("Filtra");
        topPanel.add(filterButton);

        add(topPanel, BorderLayout.NORTH);

        // CENTER: tabella
        tableModel = new CorsoTableModel(List.of());
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // BOTTOM: azione
        JButton openButton = new JButton("Apri corso");
        add(openButton, BorderLayout.SOUTH);

        // EVENTI
        filterButton.addActionListener(e -> filtraCorsi());
        openButton.addActionListener(e -> apriCorso(table));
    }

    private void loadCorsi() {
        List<Corso> corsi = corsoController.getCorsiChef(chef.getIdChef());
        tableModel.setCorsi(corsi);
    }

    private void filtraCorsi() {
        String tema = temaField.getText();
        List<Corso> corsi =
                corsoController.getCorsiChefPerCategoria(chef.getIdChef(), tema);
        tableModel.setCorsi(corsi);
    }

    private void apriCorso(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona un corso",
                    "Attenzione",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Corso corso = tableModel.getCorsoAt(selectedRow);
        new CorsoFrame(corso).setVisible(true);
    }
}

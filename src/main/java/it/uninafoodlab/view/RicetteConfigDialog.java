package it.uninafoodlab.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import it.uninafoodlab.view.SessionConfigPanel.RicettaInput;

/**
 * Dialog modale per configurare le ricette di una sessione pratica.
 */
public class RicetteConfigDialog extends JDialog {
    
    private List<RicettaInput> ricette;
    private List<RicettaRow> ricetteRows;
    private boolean confirmed;
    
    public RicetteConfigDialog(JFrame parent, int numRicette, List<RicettaInput> ricetteEsistenti) {
        super(parent, "Configura Ricette", true);
        this.ricette = new ArrayList<>(ricetteEsistenti);
        this.ricetteRows = new ArrayList<>();
        this.confirmed = false;
        
        initDialog(numRicette);
    }
    
    private void initDialog(int numRicette) {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UiUtil.UNINA_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Configura " + numRicette + " Ricette");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
        
        // Panel ricette
        JPanel ricettePanel = new JPanel();
        ricettePanel.setLayout(new BoxLayout(ricettePanel, BoxLayout.Y_AXIS));
        ricettePanel.setBackground(Color.WHITE);
        ricettePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        for (int i = 0; i < numRicette; i++) {
            // Pre-popola se esistono già
            RicettaInput existing = i < ricette.size() ? ricette.get(i) : null;
            
            RicettaRow row = new RicettaRow(i + 1, existing);
            ricetteRows.add(row);
            ricettePanel.add(row.getPanel());
            ricettePanel.add(Box.createVerticalStrut(15));
        }
        
        JScrollPane scrollPane = new JScrollPane(ricettePanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottoni
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton annullaBtn = new JButton("Annulla");
        annullaBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        annullaBtn.setPreferredSize(new Dimension(120, 35));
        annullaBtn.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        JButton confermaBtn = new JButton("Conferma");
        confermaBtn.setFont(new Font("Arial", Font.BOLD, 14));
        confermaBtn.setBackground(UiUtil.UNINA_BLUE);
        confermaBtn.setForeground(Color.WHITE);
        confermaBtn.setFocusPainted(false);
        confermaBtn.setPreferredSize(new Dimension(120, 35));
        confermaBtn.addActionListener(e -> handleConferma());
        
        buttonPanel.add(annullaBtn);
        buttonPanel.add(confermaBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Impostazioni finestra
        setSize(700, 600);
        setLocationRelativeTo(getParent());
    }
    
    /**
     * Gestisce la conferma delle ricette.
     */
    private void handleConferma() {
        ricette.clear();
        
        for (RicettaRow row : ricetteRows) {
            String nome = row.getNomeField().getText().trim();
            String ingredienti = row.getIngredientiArea().getText().trim();
            
            if (nome.isEmpty() || ingredienti.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Compila tutti i campi per ogni ricetta",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            ricette.add(new RicettaInput(nome, ingredienti));
        }
        
        confirmed = true;
        dispose();
    }
    
    /**
     * Mostra il dialog e ritorna le ricette configurate.
     */
    public List<RicettaInput> showDialog() {
        setVisible(true);
        return confirmed ? ricette : null;
    }
    
    // ==================== CLASSE INTERNA: RicettaRow ====================
    
    private class RicettaRow {
        private JPanel panel;
        private JTextField nomeField;
        private JTextArea ingredientiArea;
        
        public RicettaRow(int numero, RicettaInput existing) {
            createPanel(numero, existing);
        }
        
        private void createPanel(int numero, RicettaInput existing) {
            panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(UiUtil.CARD_LIGHT);
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiUtil.UNINA_BLUE, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
            
            // Header
            JLabel numeroLabel = new JLabel("Ricetta " + numero);
            numeroLabel.setFont(new Font("Arial", Font.BOLD, 16));
            numeroLabel.setForeground(UiUtil.UNINA_BLUE);
            
            // Form
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(UiUtil.CARD_LIGHT);
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 0, 5, 10);
            
            // Nome Ricetta
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0.0;
            JLabel nomeLabel = new JLabel("Nome:");
            nomeLabel.setFont(new Font("Arial", Font.BOLD, 13));
            formPanel.add(nomeLabel, gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            nomeField = new JTextField(30);
            nomeField.setFont(new Font("Arial", Font.PLAIN, 13));
            if (existing != null) {
                nomeField.setText(existing.getNome());
            }
            formPanel.add(nomeField, gbc);
            
            // Ingredienti
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0.0;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            JLabel ingredientiLabel = new JLabel("Ingredienti:");
            ingredientiLabel.setFont(new Font("Arial", Font.BOLD, 13));
            formPanel.add(ingredientiLabel, gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.BOTH;
            ingredientiArea = new JTextArea(3, 30);
            ingredientiArea.setLineWrap(true);
            ingredientiArea.setWrapStyleWord(true);
            ingredientiArea.setFont(new Font("Arial", Font.PLAIN, 13));
            ingredientiArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            if (existing != null) {
                ingredientiArea.setText(existing.getIngredienti());
            }
            JScrollPane scrollPane = new JScrollPane(ingredientiArea);
            scrollPane.setPreferredSize(new Dimension(300, 60));
            formPanel.add(scrollPane, gbc);
            
            panel.add(numeroLabel, BorderLayout.NORTH);
            panel.add(formPanel, BorderLayout.CENTER);
        }
        
        public JPanel getPanel() {
            return panel;
        }
        
        public JTextField getNomeField() {
            return nomeField;
        }
        
        public JTextArea getIngredientiArea() {
            return ingredientiArea;
        }
    }
}
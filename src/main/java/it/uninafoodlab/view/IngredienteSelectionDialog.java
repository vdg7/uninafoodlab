package it.uninafoodlab.view;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.*;

import it.uninafoodlab.dao.IngredienteDAO;
import it.uninafoodlab.model.domain.Ingrediente;

/**
 * Dialog per selezionare un ingrediente e specificare la quantità.
 */
public class IngredienteSelectionDialog extends JDialog {
    
    private JComboBox<IngredienteItem> ingredienteCombo;
    private JTextField quantitaField;
    private JLabel unitaLabel;
    
    private Ingrediente selectedIngrediente;
    private BigDecimal selectedQuantita;
    private boolean confirmed;
    
    /**
     * Classe wrapper per mostrare ingrediente nella ComboBox.
     */
    private static class IngredienteItem {
        private Ingrediente ingrediente;
        
        public IngredienteItem(Ingrediente ingrediente) {
            this.ingrediente = ingrediente;
        }
        
        public Ingrediente getIngrediente() {
            return ingrediente;
        }
        
        @Override
        public String toString() {
            return ingrediente.getNome() + " (" + ingrediente.getCategoria() + ")";
        }
    }
    
    public IngredienteSelectionDialog(Window parent) {
    	super(parent, "Seleziona Ingrediente", Dialog.ModalityType.APPLICATION_MODAL);
        this.confirmed = false;
        initDialog();
    }
    
    private void initDialog() {
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UiUtil.UNINA_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Seleziona Ingrediente");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Ingrediente
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        JLabel ingredienteLabel = new JLabel("Ingrediente:");
        ingredienteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(ingredienteLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        ingredienteCombo = new JComboBox<>();
        ingredienteCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        ingredienteCombo.setPreferredSize(new Dimension(300, 30));
        
        // Carica ingredienti dal database
        loadIngredienti();
        
        // Listener per aggiornare unità di misura
        ingredienteCombo.addActionListener(e -> updateUnitaMisura());
        
        formPanel.add(ingredienteCombo, gbc);
        
        // Quantità
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel quantitaLabel = new JLabel("Quantità:");
        quantitaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(quantitaLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel quantitaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        quantitaPanel.setBackground(Color.WHITE);
        
        quantitaField = new JTextField(10);
        quantitaField.setFont(new Font("Arial", Font.PLAIN, 13));
        
        unitaLabel = new JLabel("");
        unitaLabel.setFont(new Font("Arial", Font.BOLD, 13));
        unitaLabel.setForeground(UiUtil.UNINA_BLUE);
        
        quantitaPanel.add(quantitaField);
        quantitaPanel.add(unitaLabel);
        
        formPanel.add(quantitaPanel, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton annullaBtn = new JButton("Annulla");
        annullaBtn.setPreferredSize(new Dimension(100, 35));
        annullaBtn.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        JButton confermaBtn = new JButton("Conferma");
        confermaBtn.setBackground(UiUtil.UNINA_BLUE);
        confermaBtn.setForeground(Color.WHITE);
        confermaBtn.setFocusPainted(false);
        confermaBtn.setPreferredSize(new Dimension(100, 35));
        confermaBtn.addActionListener(e -> handleConferma());
        
        buttonPanel.add(annullaBtn);
        buttonPanel.add(confermaBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Impostazioni finestra
        pack();
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        // Aggiorna unità iniziale
        updateUnitaMisura();
    }
    
    /**
     * Carica ingredienti dal database.
     */
    private void loadIngredienti() {
        List<Ingrediente> ingredienti = IngredienteDAO.getAll();
        
        for (Ingrediente ingrediente : ingredienti) {
            ingredienteCombo.addItem(new IngredienteItem(ingrediente));
        }
    }
    
    /**
     * Aggiorna l'unità di misura mostrata.
     */
    private void updateUnitaMisura() {
        IngredienteItem selected = (IngredienteItem) ingredienteCombo.getSelectedItem();
        if (selected != null) {
            unitaLabel.setText(selected.getIngrediente().getUnitaMisura());
        }
    }
    
    /**
     * Gestisce la conferma.
     */
    private void handleConferma() {
        // Valida ingrediente
        IngredienteItem selected = (IngredienteItem) ingredienteCombo.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un ingrediente", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Valida quantità
        String quantitaStr = quantitaField.getText().trim();
        if (quantitaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci la quantità", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            BigDecimal quantita = new BigDecimal(quantitaStr);
            if (quantita.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "La quantità deve essere maggiore di zero", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            selectedIngrediente = selected.getIngrediente();
            selectedQuantita = quantita;
            confirmed = true;
            dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantità non valida", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Mostra il dialog e ritorna il risultato.
     */
    public IngredienteQuantita showDialog() {
        setVisible(true);
        return confirmed ? new IngredienteQuantita(selectedIngrediente, selectedQuantita) : null;
    }
    
    /**
     * Classe per restituire ingrediente + quantità.
     */
    public static class IngredienteQuantita {
        private Ingrediente ingrediente;
        private BigDecimal quantita;
        
        public IngredienteQuantita(Ingrediente ingrediente, BigDecimal quantita) {
            this.ingrediente = ingrediente;
            this.quantita = quantita;
        }
        
        public Ingrediente getIngrediente() {
            return ingrediente;
        }
        
        public BigDecimal getQuantita() {
            return quantita;
        }
    }
}
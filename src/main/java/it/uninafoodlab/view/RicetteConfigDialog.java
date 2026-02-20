package it.uninafoodlab.view;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import it.uninafoodlab.model.domain.Ingrediente;
import it.uninafoodlab.view.IngredienteSelectionDialog.IngredienteQuantita;

/**
 * Dialog modale per configurare le ricette di una sessione pratica.
 * AGGIORNATO per usare ingredienti separati dal database.
 */
public class RicetteConfigDialog extends JDialog {
    
    private List<RicettaData> ricette;
    private List<RicettaRow> ricetteRows;
    private boolean confirmed;
    
    public RicetteConfigDialog(JFrame parent, int numRicette, List<RicettaData> ricetteEsistenti) {
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
            RicettaData existing = i < ricette.size() ? ricette.get(i) : null;
            
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
        setSize(800, 600);
        setLocationRelativeTo(getParent());
    }
    
    private void handleConferma() {
        ricette.clear();
        
        for (RicettaRow row : ricetteRows) {
            String nome = row.getNomeField().getText().trim();
            
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserisci il nome per ogni ricetta", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (row.getIngredienti().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aggiungi almeno un ingrediente per ogni ricetta", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            ricette.add(new RicettaData(nome, row.getIngredienti()));
        }
        
        confirmed = true;
        dispose();
    }
    
    public List<RicettaData> showDialog() {
        setVisible(true);
        return confirmed ? ricette : null;
    }
    
    // ==================== CLASSE INTERNA: RicettaRow ====================
    
    private class RicettaRow {
        private JPanel panel;
        private JTextField nomeField;
        private List<IngredienteQuantita> ingredienti;
        private JPanel ingredientiListPanel;
        
        public RicettaRow(int numero, RicettaData existing) {
            this.ingredienti = new ArrayList<>();
            createPanel(numero, existing);
        }
        
        private void createPanel(int numero, RicettaData existing) {
            panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(UiUtil.CARD_LIGHT);
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiUtil.UNINA_BLUE, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
            
            // Header
            JLabel numeroLabel = new JLabel("Ricetta " + numero);
            numeroLabel.setFont(new Font("Arial", Font.BOLD, 16));
            numeroLabel.setForeground(UiUtil.UNINA_BLUE);
            
            // Form nome
            JPanel nomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            nomePanel.setBackground(UiUtil.CARD_LIGHT);
            
            JLabel nomeLabel = new JLabel("Nome:");
            nomeLabel.setFont(new Font("Arial", Font.BOLD, 13));
            
            nomeField = new JTextField(30);
            nomeField.setFont(new Font("Arial", Font.PLAIN, 13));
            if (existing != null) {
                nomeField.setText(existing.getNome());
            }
            
            nomePanel.add(nomeLabel);
            nomePanel.add(nomeField);
            
            // Ingredienti section
            JPanel ingredientiPanel = new JPanel(new BorderLayout(5, 5));
            ingredientiPanel.setBackground(UiUtil.CARD_LIGHT);
            
            JLabel ingredientiLabel = new JLabel("Ingredienti:");
            ingredientiLabel.setFont(new Font("Arial", Font.BOLD, 13));
            
            ingredientiListPanel = new JPanel();
            ingredientiListPanel.setLayout(new BoxLayout(ingredientiListPanel, BoxLayout.Y_AXIS));
            ingredientiListPanel.setBackground(Color.WHITE);
            ingredientiListPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            
            JScrollPane ingredientiScroll = new JScrollPane(ingredientiListPanel);
            ingredientiScroll.setPreferredSize(new Dimension(400, 80));
            
            JButton aggiungiBtn = new JButton("+ Aggiungi Ingrediente");
            aggiungiBtn.setFont(new Font("Arial", Font.BOLD, 11));
            aggiungiBtn.setBackground(UiUtil.UNINA_BLUE);
            aggiungiBtn.setForeground(Color.WHITE);
            aggiungiBtn.setFocusPainted(false);
            aggiungiBtn.addActionListener(e -> aggiungiIngrediente());
            
            ingredientiPanel.add(ingredientiLabel, BorderLayout.NORTH);
            ingredientiPanel.add(ingredientiScroll, BorderLayout.CENTER);
            ingredientiPanel.add(aggiungiBtn, BorderLayout.SOUTH);
            
            // Pre-popola ingredienti se esistono
            if (existing != null && existing.getIngredienti() != null) {
                for (IngredienteQuantita iq : existing.getIngredienti()) {
                    ingredienti.add(iq);
                    addIngredienteToList(iq);
                }
            }
            
            // Assembly
            JPanel topPanel = new JPanel(new BorderLayout(5, 5));
            topPanel.setBackground(UiUtil.CARD_LIGHT);
            topPanel.add(numeroLabel, BorderLayout.NORTH);
            topPanel.add(nomePanel, BorderLayout.CENTER);
            
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(ingredientiPanel, BorderLayout.CENTER);
        }
        
        private void aggiungiIngrediente() {
            IngredienteSelectionDialog dialog = new IngredienteSelectionDialog(
                SwingUtilities.getWindowAncestor(panel)
            );
            
            IngredienteQuantita result = dialog.showDialog();
            if (result != null) {
                ingredienti.add(result);
                addIngredienteToList(result);
            }
        }
        
        private void addIngredienteToList(IngredienteQuantita iq) {
            JPanel itemPanel = new JPanel(new BorderLayout(5, 0));
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
            itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            
            String text = iq.getIngrediente().getNome() + ": " + 
                         iq.getQuantita() + " " + 
                         iq.getIngrediente().getUnitaMisura();
            
            JLabel label = new JLabel(text);
            label.setFont(new Font("Arial", Font.PLAIN, 12));
            
            JButton removeBtn = new JButton("✕");
            removeBtn.setFont(new Font("Arial", Font.BOLD, 10));
            removeBtn.setPreferredSize(new Dimension(20, 20));
            removeBtn.setBackground(Color.RED);
            removeBtn.setForeground(Color.WHITE);
            removeBtn.setFocusPainted(false);
            removeBtn.addActionListener(e -> {
                ingredienti.remove(iq);
                ingredientiListPanel.remove(itemPanel);
                ingredientiListPanel.revalidate();
                ingredientiListPanel.repaint();
            });
            
            itemPanel.add(label, BorderLayout.CENTER);
            itemPanel.add(removeBtn, BorderLayout.EAST);
            
            ingredientiListPanel.add(itemPanel);
            ingredientiListPanel.revalidate();
            ingredientiListPanel.repaint();
        }
        
        public JPanel getPanel() { return panel; }
        public JTextField getNomeField() { return nomeField; }
        public List<IngredienteQuantita> getIngredienti() { return ingredienti; }
    }
    
    // ==================== CLASSE DATI ====================
    
    public static class RicettaData {
        private String nome;
        private List<IngredienteQuantita> ingredienti;
        
        public RicettaData(String nome, List<IngredienteQuantita> ingredienti) {
            this.nome = nome;
            this.ingredienti = ingredienti;
        }
        
        public String getNome() { return nome; }
        public List<IngredienteQuantita> getIngredienti() { return ingredienti; }
    }
}
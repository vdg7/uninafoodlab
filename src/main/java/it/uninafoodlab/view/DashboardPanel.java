package it.uninafoodlab.view;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

import it.uninafoodlab.model.domain.Corso;
import it.uninafoodlab.model.enums.Categoria;

/**
 * Panel che mostra la lista dei corsi dello chef con filtro per categoria.
 */
public class DashboardPanel extends BasePanel {
    
    private JComboBox<String> categoriaFilter;
    private JPanel corsiContainer;
    private List<Corso> allCorsi;
    
    public DashboardPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UiUtil.UNINA_GREY);
        setPadding(20);
        
        initHeader();
        initFilterPanel();
        initCorsiPanel();
    }
    
    private void initHeader() {
        JLabel titleLabel = new JLabel("I Miei Corsi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(UiUtil.UNINA_BLUE);
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(UiUtil.UNINA_GREY);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void initFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(UiUtil.UNINA_GREY);
        
        JLabel filterLabel = new JLabel("Filtra per categoria:");
        filterLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // ComboBox con tutte le categorie + "Tutte"
        String[] categorie = new String[Categoria.values().length + 1];
        categorie[0] = "Tutte";
        for (int i = 0; i < Categoria.values().length; i++) {
            categorie[i + 1] = Categoria.values()[i].getDisplayName();
        }
        
        categoriaFilter = new JComboBox<>(categorie);
        categoriaFilter.setPreferredSize(new Dimension(200, 30));
        categoriaFilter.addActionListener(e -> applyFilter());
        
        filterPanel.add(filterLabel);
        filterPanel.add(categoriaFilter);
        
        add(filterPanel, BorderLayout.NORTH);
        // Fix: aggiungi header e filter in un unico panel nord
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UiUtil.UNINA_GREY);
        
        JLabel titleLabel = new JLabel("I Miei Corsi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(UiUtil.UNINA_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
    }
    
    private void initCorsiPanel() {
        corsiContainer = new JPanel();
        corsiContainer.setLayout(new BoxLayout(corsiContainer, BoxLayout.Y_AXIS));
        corsiContainer.setBackground(UiUtil.UNINA_GREY);
        
        JScrollPane scrollPane = new JScrollPane(corsiContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Imposta i corsi da visualizzare.
     */
    public void setCorsi(List<Corso> corsi) {
        this.allCorsi = corsi;
        applyFilter();
    }
    
    /**
     * Applica il filtro per categoria.
     */
    private void applyFilter() {
        if (allCorsi == null) return;
        
        String selectedCategoria = (String) categoriaFilter.getSelectedItem();
        
        List<Corso> filteredCorsi = allCorsi;
        
        if (!"Tutte".equals(selectedCategoria)) {
            filteredCorsi = allCorsi.stream()
                .filter(c -> c.getCategoria().getDisplayName().equals(selectedCategoria))
                .collect(Collectors.toList());
        }
        
        displayCorsi(filteredCorsi);
    }
    
    /**
     * Mostra i corsi nel container.
     */
    private void displayCorsi(List<Corso> corsi) {
        corsiContainer.removeAll();
        
        if (corsi.isEmpty()) {
            JLabel emptyLabel = new JLabel("Nessun corso trovato");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            corsiContainer.add(Box.createVerticalStrut(50));
            corsiContainer.add(emptyLabel);
        } else {
            for (Corso corso : corsi) {
                corsiContainer.add(createCorsoCard(corso));
                corsiContainer.add(Box.createVerticalStrut(10));
            }
        }
        
        corsiContainer.revalidate();
        corsiContainer.repaint();
    }
    
    /**
     * Crea una card per un corso.
     */
    private JPanel createCorsoCard(Corso corso) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(UiUtil.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiUtil.CARD_BORDER, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Pannello info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(UiUtil.CARD_BG);
        
        JLabel titoloLabel = new JLabel(corso.getTitolo());
        titoloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titoloLabel.setForeground(UiUtil.UNINA_BLUE);
        
        JLabel categoriaLabel = new JLabel("Categoria: " + corso.getCategoria().getDisplayName());
        categoriaLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel dataLabel = new JLabel("Inizio: " + corso.getDataInizio() + " | Frequenza: ogni " + 
                                      corso.getFrequenza() + " giorni | Sessioni: " + corso.getNumeroSessioni());
        dataLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dataLabel.setForeground(Color.GRAY);
        
        infoPanel.add(titoloLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(categoriaLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(dataLabel);
        
        // Pulsante dettagli
        JButton dettagliBtn = new JButton("Vedi Dettagli");
        dettagliBtn.setBackground(UiUtil.UNINA_BLUE);
        dettagliBtn.setForeground(Color.WHITE);
        dettagliBtn.setFocusPainted(false);
        dettagliBtn.addActionListener(e -> showCorsoDetails(corso));
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(dettagliBtn, BorderLayout.EAST);
        
        // Effetto hover
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(UiUtil.CARD_HOVER);
                infoPanel.setBackground(UiUtil.CARD_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(UiUtil.CARD_BG);
                infoPanel.setBackground(UiUtil.CARD_BG);
            }
        });
        
        return card;
    }
    
    /**
     * Mostra i dettagli del corso (TODO).
     */
    private void showCorsoDetails(Corso corso) {
        showWarning("Funzionalità dettaglio corso in arrivo!", "Work in Progress");
        // TODO: Aprire DettaglioCorsoPanel
    }
}
package it.uninafoodlab.view;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import it.uninafoodlab.model.domain.*;

/**
 * Panel per visualizzare i dettagli completi di un corso.
 */
public class DettagliCorsoPanel extends BasePanel {
    
    private Corso corsoCorrente;
    
    private JLabel titoloLabel;
    private JLabel categoriaLabel;
    private JLabel dataInizioLabel;
    private JLabel frequenzaLabel;
    private JLabel numSessioniLabel;
    private JPanel headerPanel;
    private JPanel sessioniContainer;
    
    public DettagliCorsoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UiUtil.UNINA_GREY);
        setPadding(20);
        
        initHeader();
        initInfoPanel();
        initSessioniPanel();
    }
    
    private void initHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UiUtil.UNINA_GREY);
        
        JLabel titleLabel = new JLabel("Dettaglio Corso");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(UiUtil.UNINA_BLUE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void initInfoPanel() {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiUtil.CARD_BORDER, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        // Titolo
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(createBoldLabel("Titolo:"), gbc);
        
        gbc.gridx = 1;
        titoloLabel = new JLabel();
        titoloLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoPanel.add(titoloLabel, gbc);
        
        // Categoria
        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(createBoldLabel("Categoria:"), gbc);
        
        gbc.gridx = 1;
        categoriaLabel = new JLabel();
        categoriaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoPanel.add(categoriaLabel, gbc);
        
        // Data Inizio
        gbc.gridx = 0;
        gbc.gridy = 2;
        infoPanel.add(createBoldLabel("Data Inizio:"), gbc);
        
        gbc.gridx = 1;
        dataInizioLabel = new JLabel();
        dataInizioLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoPanel.add(dataInizioLabel, gbc);
        
        // Frequenza
        gbc.gridx = 0;
        gbc.gridy = 3;
        infoPanel.add(createBoldLabel("Frequenza:"), gbc);
        
        gbc.gridx = 1;
        frequenzaLabel = new JLabel();
        frequenzaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoPanel.add(frequenzaLabel, gbc);
        
        // Numero Sessioni
        gbc.gridx = 0;
        gbc.gridy = 4;
        infoPanel.add(createBoldLabel("Numero Sessioni:"), gbc);
        
        gbc.gridx = 1;
        numSessioniLabel = new JLabel();
        numSessioniLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoPanel.add(numSessioniLabel, gbc);
        
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapperPanel.setBackground(UiUtil.UNINA_GREY);
        wrapperPanel.add(infoPanel);
        
        add(wrapperPanel, BorderLayout.NORTH);
        
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(UiUtil.UNINA_GREY);
        
        topContainer.add(headerPanel);
        topContainer.add(infoPanel);
        
        add(topContainer, BorderLayout.NORTH);
    }
    
    private void initSessioniPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UiUtil.UNINA_GREY);
        
        JLabel sessioniTitle = new JLabel("Sessioni del Corso");
        sessioniTitle.setFont(new Font("Arial", Font.BOLD, 20));
        sessioniTitle.setForeground(UiUtil.UNINA_BLUE);
        sessioniTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        sessioniContainer = new JPanel();
        sessioniContainer.setLayout(new BoxLayout(sessioniContainer, BoxLayout.Y_AXIS));
        sessioniContainer.setBackground(UiUtil.UNINA_GREY);
        
        JScrollPane scrollPane = new JScrollPane(sessioniContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(sessioniTitle, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(UiUtil.UNINA_BLUE);
        return label;
    }
    
    /**
     * Carica e visualizza un corso con tutte le sue sessioni.
     */
    public void loadCorso(Corso corso, List<SessioneOnline> sessioniOnline, 
                         List<SessionePratica> sessioniPratiche,
                         List<Ricetta> ricetteConIngredienti) {
        this.corsoCorrente = corso;
        
        // Aggiorna info corso
        titoloLabel.setText(corso.getTitolo());
        categoriaLabel.setText(corso.getCategoria().getDisplayName());
        dataInizioLabel.setText(corso.getDataInizio().toString());
        frequenzaLabel.setText("Ogni " + corso.getFrequenza() + " giorni");
        numSessioniLabel.setText(String.valueOf(corso.getNumeroSessioni()));
        
        // Pulisci container sessioni
        sessioniContainer.removeAll();
        
        // Ordina sessioni per data (mix online e pratiche)
        // Per semplicità mostriamo prima online, poi pratiche
        
        // Sessioni Online
        if (!sessioniOnline.isEmpty()) {
            sessioniContainer.add(createSectionTitle("Sessioni Online"));
            for (SessioneOnline sessione : sessioniOnline) {
                sessioniContainer.add(createSessioneOnlineCard(sessione));
                sessioniContainer.add(Box.createVerticalStrut(10));
            }
        }
        
        // Sessioni Pratiche
        if (!sessioniPratiche.isEmpty()) {
            sessioniContainer.add(createSectionTitle("Sessioni Pratiche"));
            for (SessionePratica sessione : sessioniPratiche) {
                // Trova ricette per questa sessione
                List<Ricetta> ricetteSessione = ricetteConIngredienti.stream()
                    .filter(r -> r.getIdSessionePratica() == sessione.getIdSessionePratica())
                    .toList();
                
                sessioniContainer.add(createSessionePraticaCard(sessione, ricetteSessione));
                sessioniContainer.add(Box.createVerticalStrut(10));
            }
        }
        
        if (sessioniOnline.isEmpty() && sessioniPratiche.isEmpty()) {
            JLabel emptyLabel = new JLabel("Nessuna sessione trovata");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            emptyLabel.setForeground(Color.GRAY);
            sessioniContainer.add(emptyLabel);
        }
        
        sessioniContainer.revalidate();
        sessioniContainer.repaint();
    }
    
    private JPanel createSectionTitle(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(UiUtil.UNINA_GREY);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(UiUtil.UNINA_BLUE);
        
        panel.add(label);
        return panel;
    }
    
    private JPanel createSessioneOnlineCard(SessioneOnline sessione) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiUtil.UNINA_BLUE, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel tipoLabel = new JLabel("SESSIONE ONLINE");
        tipoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tipoLabel.setForeground(UiUtil.UNINA_BLUE);
        
        JLabel dataLabel = new JLabel("Data: " + sessione.getData() + " | Durata: " + sessione.getDurata() + " min");
        dataLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        
        JLabel linkLabel = new JLabel("Link: " + sessione.getLink());
        linkLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        linkLabel.setForeground(Color.GRAY);
        
        infoPanel.add(tipoLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(dataLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(linkLabel);
        
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createSessionePraticaCard(SessionePratica sessione, List<Ricetta> ricette) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 100, 50), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        
        // Info sessione
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel tipoLabel = new JLabel("SESSIONE PRATICA");
        tipoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tipoLabel.setForeground(new Color(220, 100, 50));
        tipoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel dataLabel = new JLabel("Data: " + sessione.getData() + " | Durata: " + sessione.getDurata() + " min");
        dataLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        dataLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel luogoLabel = new JLabel("Luogo: " + sessione.getLuogo());
        luogoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        luogoLabel.setForeground(Color.GRAY);
        luogoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(tipoLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(dataLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(luogoLabel);
        
        // Ricette
        if (!ricette.isEmpty()) {
            infoPanel.add(Box.createVerticalStrut(10));
            
            JLabel ricetteLabel = new JLabel("Ricette (" + ricette.size() + "):");
            ricetteLabel.setFont(new Font("Arial", Font.BOLD, 13));
            ricetteLabel.setForeground(UiUtil.UNINA_BLUE);
            ricetteLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(ricetteLabel);
            
            for (Ricetta ricetta : ricette) {
                infoPanel.add(Box.createVerticalStrut(5));
                JPanel ricettaCard = createRicettaPanel(ricetta);
                ricettaCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                infoPanel.add(ricettaCard);
            }
        } else {
            infoPanel.add(Box.createVerticalStrut(5));
            JLabel noRicette = new JLabel("Nessuna ricetta associata");
            noRicette.setFont(new Font("Arial", Font.ITALIC, 12));
            noRicette.setForeground(Color.GRAY);
            infoPanel.add(noRicette);
        }
        
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createRicettaPanel(Ricetta ricetta) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(UiUtil.CARD_LIGHT);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        JLabel nomeLabel = new JLabel("• " + ricetta.getNome());
        nomeLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Ingredienti
        if (!ricetta.getIngredienti().isEmpty()) {
            JPanel ingredientiPanel = new JPanel();
            ingredientiPanel.setLayout(new BoxLayout(ingredientiPanel, BoxLayout.Y_AXIS));
            ingredientiPanel.setBackground(UiUtil.CARD_LIGHT);
            ingredientiPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel ingredientiLabel = new JLabel("Ingredienti:");
            ingredientiLabel.setFont(new Font("Arial", Font.ITALIC, 11));
            ingredientiLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            ingredientiLabel.setForeground(Color.DARK_GRAY);
            ingredientiPanel.add(ingredientiLabel);
            
            for (RicettaIngrediente ri : ricetta.getIngredienti()) {
                String text = "  - " + ri.getIngrediente().getNome() + ": " + 
                             ri.getQuantita() + " " + 
                             ri.getIngrediente().getUnitaMisura();
                
                JLabel ingLabel = new JLabel(text);
                ingLabel.setFont(new Font("Arial", Font.PLAIN, 11));
                ingredientiPanel.add(ingLabel);
            }
            
            panel.add(nomeLabel, BorderLayout.NORTH);
            panel.add(ingredientiPanel, BorderLayout.CENTER);
        } else {
            panel.add(nomeLabel, BorderLayout.CENTER);
        }
        
        return panel;
    }
    
    public Corso getCorsoCorrente() {
        return corsoCorrente;
    }
}
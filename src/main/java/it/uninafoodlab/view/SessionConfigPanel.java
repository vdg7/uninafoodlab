package it.uninafoodlab.view;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.*;

/**
 * Panel per configurare ogni sessione nel dettaglio.
 */
public class SessionConfigPanel extends BasePanel {
    
    private JPanel sessionesPanel;
    private List<SessionRow> sessionRows;
    private JButton confermaButton;
    private JButton annullaButton;
    
    private LocalDate dataInizio;
    private int frequenza;
    private int numSessioni;
    
    private Consumer<List<SessionData>> onConfermaAction;
    private Runnable onAnnullaAction;
    
    public SessionConfigPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UiUtil.UNINA_GREY);
        setPadding(20);
        
        sessionRows = new ArrayList<>();
        
        initHeader();
        initSessionesPanel();
        initButtonPanel();
    }
    
    private void initHeader() {
        JLabel titleLabel = new JLabel("Configura Sessioni");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(UiUtil.UNINA_BLUE);
        
        JLabel subtitleLabel = new JLabel("Specifica i dettagli di ogni sessione");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(UiUtil.UNINA_GREY);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void initSessionesPanel() {
        sessionesPanel = new JPanel();
        sessionesPanel.setLayout(new BoxLayout(sessionesPanel, BoxLayout.Y_AXIS));
        sessionesPanel.setBackground(UiUtil.UNINA_GREY);
        
        JScrollPane scrollPane = new JScrollPane(sessionesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(UiUtil.UNINA_GREY);
        
        annullaButton = new JButton("Annulla");
        annullaButton.setFont(new Font("Arial", Font.PLAIN, 14));
        annullaButton.setPreferredSize(new Dimension(150, 40));
        annullaButton.addActionListener(e -> {
            if (onAnnullaAction != null) onAnnullaAction.run();
        });
        
        confermaButton = new JButton("Conferma e Salva Corso");
        confermaButton.setFont(new Font("Arial", Font.BOLD, 14));
        confermaButton.setBackground(UiUtil.UNINA_BLUE);
        confermaButton.setForeground(Color.WHITE);
        confermaButton.setFocusPainted(false);
        confermaButton.setPreferredSize(new Dimension(200, 40));
        confermaButton.addActionListener(e -> handleConferma());
        
        buttonPanel.add(annullaButton);
        buttonPanel.add(confermaButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Configura le sessioni.
     */
    public void setupSessions(LocalDate dataInizio, int frequenza, int numSessioni) {
        this.dataInizio = dataInizio;
        this.frequenza = frequenza;
        this.numSessioni = numSessioni;
        
        sessionesPanel.removeAll();
        sessionRows.clear();
        
        LocalDate currentDate = dataInizio;
        
        for (int i = 1; i <= numSessioni; i++) {
            SessionRow row = new SessionRow(i, currentDate);
            sessionRows.add(row);
            sessionesPanel.add(row.getPanel());
            sessionesPanel.add(Box.createVerticalStrut(15));
            
            currentDate = currentDate.plusDays(frequenza);
        }
        
        sessionesPanel.revalidate();
        sessionesPanel.repaint();
    }
    
    /**
     * Gestisce la conferma.
     */
    private void handleConferma() {
        List<SessionData> sessionsData = new ArrayList<>();
        
        // Valida e raccoglie i dati
        for (SessionRow row : sessionRows) {
            SessionData data = row.getData();
            if (data == null) {
                return; // Errore già mostrato dalla row
            }
            sessionsData.add(data);
        }
        
        if (onConfermaAction != null) {
            onConfermaAction.accept(sessionsData);
        }
    }
    
    public void setConfermaAction(Consumer<List<SessionData>> action) {
        this.onConfermaAction = action;
    }
    
    public void setAnnullaAction(Runnable action) {
        this.onAnnullaAction = action;
    }
    
    // ==================== CLASSE INTERNA: SessionRow ====================
    
    /**
     * Rappresenta una singola riga di configurazione sessione.
     */
    private class SessionRow {
        private JPanel panel;
        private int sessionNumber;
        private LocalDate sessionDate;
        
        private JRadioButton onlineRadio;
        private JRadioButton praticaRadio;
        
        // Campi Online
        private JTextField linkField;
        
        // Campi Pratica
        private JTextField luogoField;
        private List<RicetteConfigDialog.RicettaData> ricetteConfigurate;
        
        // Panel dinamici
        private JPanel onlinePanel;
        private JPanel praticaPanel;
        private JPanel detailsContainer;
        
        public SessionRow(int sessionNumber, LocalDate sessionDate) {
            this.sessionNumber = sessionNumber;
            this.sessionDate = sessionDate;
            createPanel();
        }
        
        private void createPanel() {
            panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiUtil.UNINA_BLUE, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
            
            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(Color.WHITE);
            
            JLabel titleLabel = new JLabel("Sessione " + sessionNumber);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setForeground(UiUtil.UNINA_BLUE);
            
            JLabel dateLabel = new JLabel("Data: " + sessionDate);
            dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            dateLabel.setForeground(Color.GRAY);
            
            headerPanel.add(titleLabel, BorderLayout.WEST);
            headerPanel.add(dateLabel, BorderLayout.EAST);
            
            // Radio buttons
            JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
            radioPanel.setBackground(Color.WHITE);
            
            onlineRadio = new JRadioButton("Online");
            onlineRadio.setFont(new Font("Arial", Font.BOLD, 14));
            onlineRadio.setBackground(Color.WHITE);
            onlineRadio.setSelected(true);
            onlineRadio.addActionListener(e -> showOnlineFields());
            
            praticaRadio = new JRadioButton("Pratica");
            praticaRadio.setFont(new Font("Arial", Font.BOLD, 14));
            praticaRadio.setBackground(Color.WHITE);
            praticaRadio.addActionListener(e -> showPraticaFields());
            
            ButtonGroup group = new ButtonGroup();
            group.add(onlineRadio);
            group.add(praticaRadio);
            
            radioPanel.add(onlineRadio);
            radioPanel.add(praticaRadio);
            
            // Container per i dettagli (dinamico)
            detailsContainer = new JPanel(new BorderLayout());
            detailsContainer.setBackground(Color.WHITE);
            
            createOnlinePanel();
            createPraticaPanel();
            
            // Mostra online di default
            showOnlineFields();
            
            // Assembla
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(Color.WHITE);
            topPanel.add(headerPanel, BorderLayout.NORTH);
            topPanel.add(radioPanel, BorderLayout.CENTER);
            
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(detailsContainer, BorderLayout.CENTER);
        }
        
        private void createOnlinePanel() {
            onlinePanel = new JPanel(new GridBagLayout());
            onlinePanel.setBackground(UiUtil.CARD_LIGHT);
            onlinePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.weightx = 1.0;
            
            // Link
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0.0;
            JLabel linkLabel = new JLabel("Link Meeting:");
            linkLabel.setFont(new Font("Arial", Font.BOLD, 13));
            onlinePanel.add(linkLabel, gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            linkField = new JTextField(40);
            linkField.setText("https://meet.uninafoodlab.it/sessione-" + sessionNumber);
            onlinePanel.add(linkField, gbc);
        }
        
        private void createPraticaPanel() {
            praticaPanel = new JPanel(new GridBagLayout());
            praticaPanel.setBackground(UiUtil.CARD_LIGHT);
            praticaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            
            // Luogo
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0.0;
            JLabel luogoLabel = new JLabel("Luogo:");
            luogoLabel.setFont(new Font("Arial", Font.BOLD, 13));
            praticaPanel.add(luogoLabel, gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            luogoField = new JTextField(40);
            luogoField.setText("Laboratorio Cucina - Sede UniNA");
            praticaPanel.add(luogoField, gbc);
            
            // Numero Ricette + Bottone
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0.0;
            JLabel numRicetteLabel = new JLabel("Numero Ricette:");
            numRicetteLabel.setFont(new Font("Arial", Font.BOLD, 13));
            praticaPanel.add(numRicetteLabel, gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 0.0;
            JPanel ricetteControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            ricetteControlPanel.setBackground(UiUtil.CARD_LIGHT);
            
            SpinnerNumberModel numRicetteModel = new SpinnerNumberModel(1, 0, 10, 1);
            JSpinner numRicetteSpinner = new JSpinner(numRicetteModel);
            numRicetteSpinner.setPreferredSize(new Dimension(60, 25));
            
            JButton configRicetteBtn = new JButton("Configura Ricette");
            configRicetteBtn.setFont(new Font("Arial", Font.BOLD, 12));
            configRicetteBtn.setBackground(UiUtil.UNINA_BLUE);
            configRicetteBtn.setForeground(Color.WHITE);
            configRicetteBtn.setFocusPainted(false);
            
            // Lista ricette configurate (temporanea)
            List<RicetteConfigDialog.RicettaData> ricetteConfigurate = new ArrayList<>();
            
            configRicetteBtn.addActionListener(e -> {
                int numRicette = (Integer) numRicetteSpinner.getValue();
                if (numRicette == 0) {
                    showWarning("Imposta il numero di ricette prima di configurare");
                    return;
                }
                
                // Apri dialog per configurare ricette
                RicetteConfigDialog dialog = new RicetteConfigDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(panel),
                    numRicette,
                    ricetteConfigurate
                );
                
                List<RicetteConfigDialog.RicettaData> risultato = dialog.showDialog();
                if (risultato != null) {
                    ricetteConfigurate.clear();
                    ricetteConfigurate.addAll(risultato);
                    showSuccess(risultato.size() + " ricette configurate!");
                }
            });
            
            ricetteControlPanel.add(numRicetteSpinner);
            ricetteControlPanel.add(configRicetteBtn);
            
            praticaPanel.add(ricetteControlPanel, gbc);
            
            // Salva riferimento alla lista ricette per getData()
            this.ricetteConfigurate = ricetteConfigurate;
        }
        
        private void showOnlineFields() {
            detailsContainer.removeAll();
            detailsContainer.add(onlinePanel, BorderLayout.CENTER);
            detailsContainer.revalidate();
            detailsContainer.repaint();
        }
        
        private void showPraticaFields() {
            detailsContainer.removeAll();
            detailsContainer.add(praticaPanel, BorderLayout.CENTER);
            detailsContainer.revalidate();
            detailsContainer.repaint();
        }
        
        public JPanel getPanel() {
            return panel;
        }
        
        /**
         * Raccoglie e valida i dati della sessione.
         */
        public SessionData getData() {
            if (onlineRadio.isSelected()) {
                String link = linkField.getText().trim();
                if (link.isEmpty()) {
                    showError("Inserisci il link per la sessione " + sessionNumber);
                    return null;
                }
                return new SessionData(SessionType.ONLINE, sessionDate, link, null, null);
                
            } else {
                String luogo = luogoField.getText().trim();
                if (luogo.isEmpty()) {
                    showError("Inserisci il luogo per la sessione " + sessionNumber);
                    return null;
                }
                
                // ✅ USA la lista configurata di RicettaData
                List<RicetteConfigDialog.RicettaData> ricette = new ArrayList<>(ricetteConfigurate);
                
                return new SessionData(SessionType.PRATICA, sessionDate, null, luogo, ricette);
            }
        }
    }
    
    // ==================== CLASSI DATI ====================
    
    public enum SessionType {
        ONLINE, PRATICA
    }
    
    public static class SessionData {
        private final SessionType type;
        private final LocalDate data;
        private final String link;          // Per Online
        private final String luogo;         // Per Pratica
        private final List<RicetteConfigDialog.RicettaData> ricette;  // Per Pratica
        
        public SessionData(SessionType type, LocalDate data, String link, 
                          String luogo, List<RicetteConfigDialog.RicettaData> ricette) {
            this.type = type;
            this.data = data;
            this.link = link;
            this.luogo = luogo;
            this.ricette = ricette;
        }
        
        public SessionType getType() { return type; }
        public LocalDate getData() { return data; }
        public String getLink() { return link; }
        public String getLuogo() { return luogo; }
        public List<RicetteConfigDialog.RicettaData> getRicette() { return ricette; }
    }
}
package it.uninafoodlab.view;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.*;

/**
 * Panel per configurare ogni sessione nel dettaglio.
 * Ogni sessione ha: tipo (Online/Pratica), data, ora di inizio, durata,
 * e campi specifici per tipo (link oppure luogo + ricette).
 */
public class SessionConfigPanel extends BasePanel {

    private JPanel sessionesPanel;
    private List<SessionRow> sessionRows;
    private JButton confermaButton;
    private JButton annullaButton;

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

        JLabel subtitleLabel = new JLabel("Specifica data, orario, durata e dettagli di ogni sessione");
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
        annullaButton.addActionListener(e -> { if (onAnnullaAction != null) onAnnullaAction.run(); });

        confermaButton = new JButton("Conferma e Salva Corso");
        confermaButton.setFont(new Font("Arial", Font.BOLD, 14));
        confermaButton.setBackground(UiUtil.UNINA_BLUE);
        confermaButton.setForeground(Color.WHITE);
        confermaButton.setFocusPainted(false);
        confermaButton.setPreferredSize(new Dimension(220, 40));
        confermaButton.addActionListener(e -> handleConferma());

        buttonPanel.add(annullaButton);
        buttonPanel.add(confermaButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setupSessions(LocalDate dataInizio, int frequenza, int numSessioni) {
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

    private void handleConferma() {
        List<SessionData> sessionsData = new ArrayList<>();
        for (SessionRow row : sessionRows) {
            SessionData data = row.getData();
            if (data == null) return;
            sessionsData.add(data);
        }
        if (onConfermaAction != null) onConfermaAction.accept(sessionsData);
    }

    public void setConfermaAction(Consumer<List<SessionData>> action) { this.onConfermaAction = action; }
    public void setAnnullaAction(Runnable action)                     { this.onAnnullaAction = action; }

    // ═══════════════════════════════════════════════════════════════
    //  INNER CLASS – SessionRow
    // ═══════════════════════════════════════════════════════════════

    private class SessionRow {
        private JPanel panel;
        private final int sessionNumber;
        private final LocalDate sessionDate;

        private JRadioButton onlineRadio;
        private JRadioButton praticaRadio;

        // Ora e durata (comuni a entrambi i tipi)
        private JSpinner oraSpinner;
        private JSpinner minutiSpinner;
        private JSpinner durataSpinner;

        // Online
        private JTextField linkField;

        // Pratica
        private JTextField luogoField;
        private List<RicetteConfigDialog.RicettaData> ricetteConfigurate;

        private JPanel detailsContainer;

        public SessionRow(int sessionNumber, LocalDate sessionDate) {
            this.sessionNumber = sessionNumber;
            this.sessionDate   = sessionDate;
            createPanel();
        }

        private void createPanel() {
            panel = new JPanel(new BorderLayout(10, 8));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiUtil.UNINA_BLUE, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

            // ── Header: titolo + data ──────────────────────────────
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(Color.WHITE);

            JLabel titleLabel = new JLabel("Sessione " + sessionNumber);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setForeground(UiUtil.UNINA_BLUE);

            JLabel dateLabel = new JLabel("Data: " + sessionDate);
            dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            dateLabel.setForeground(Color.GRAY);

            headerPanel.add(titleLabel, BorderLayout.WEST);
            headerPanel.add(dateLabel,  BorderLayout.EAST);

            // ── Riga radio + orario/durata ─────────────────────────
            JPanel controlsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 4));
            controlsRow.setBackground(Color.WHITE);

            // Radio tipo
            onlineRadio = new JRadioButton("Online");
            onlineRadio.setFont(new Font("Arial", Font.BOLD, 13));
            onlineRadio.setBackground(Color.WHITE);
            onlineRadio.setSelected(true);
            onlineRadio.addActionListener(e -> showOnlineFields());

            praticaRadio = new JRadioButton("Pratica");
            praticaRadio.setFont(new Font("Arial", Font.BOLD, 13));
            praticaRadio.setBackground(Color.WHITE);
            praticaRadio.addActionListener(e -> showPraticaFields());

            ButtonGroup group = new ButtonGroup();
            group.add(onlineRadio);
            group.add(praticaRadio);

            // Ora di inizio
            JLabel oraLabel = new JLabel("Ora inizio:");
            oraLabel.setFont(new Font("Arial", Font.PLAIN, 13));

            oraSpinner = new JSpinner(new SpinnerNumberModel(9, 0, 23, 1));
            oraSpinner.setPreferredSize(new Dimension(52, 28));
            ((JSpinner.DefaultEditor) oraSpinner.getEditor()).getTextField().setHorizontalAlignment(SwingConstants.CENTER);

            JLabel sepLabel = new JLabel(":");
            sepLabel.setFont(new Font("Arial", Font.BOLD, 14));

            minutiSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
            minutiSpinner.setPreferredSize(new Dimension(52, 28));
            // Formato con zero iniziale (es. "05")
            minutiSpinner.setEditor(new JSpinner.NumberEditor(minutiSpinner, "00"));
            ((JSpinner.DefaultEditor) minutiSpinner.getEditor()).getTextField().setHorizontalAlignment(SwingConstants.CENTER);

            // Durata
            JLabel durataLabel = new JLabel("Durata (min):");
            durataLabel.setFont(new Font("Arial", Font.PLAIN, 13));

            durataSpinner = new JSpinner(new SpinnerNumberModel(90, 15, 480, 15));
            durataSpinner.setPreferredSize(new Dimension(70, 28));
            ((JSpinner.DefaultEditor) durataSpinner.getEditor()).getTextField().setHorizontalAlignment(SwingConstants.CENTER);

            controlsRow.add(onlineRadio);
            controlsRow.add(praticaRadio);
            controlsRow.add(Box.createHorizontalStrut(10));
            controlsRow.add(oraLabel);
            controlsRow.add(oraSpinner);
            controlsRow.add(sepLabel);
            controlsRow.add(minutiSpinner);
            controlsRow.add(Box.createHorizontalStrut(10));
            controlsRow.add(durataLabel);
            controlsRow.add(durataSpinner);

            // ── Top: header + controls ─────────────────────────────
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(Color.WHITE);
            topPanel.add(headerPanel, BorderLayout.NORTH);
            topPanel.add(controlsRow, BorderLayout.CENTER);

            // ── Details container (cambia in base al tipo) ─────────
            detailsContainer = new JPanel(new BorderLayout());
            detailsContainer.setBackground(Color.WHITE);

            createOnlinePanel();
            createPraticaPanel();
            showOnlineFields();

            panel.add(topPanel,          BorderLayout.NORTH);
            panel.add(detailsContainer,  BorderLayout.CENTER);
        }

        private void createOnlinePanel() {
            JPanel p = new JPanel(new GridBagLayout());
            p.setBackground(UiUtil.CARD_LIGHT);
            p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            p.setName("ONLINE");

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill    = GridBagConstraints.HORIZONTAL;
            gbc.insets  = new Insets(5, 5, 5, 5);

            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
            JLabel lbl = new JLabel("Link Meeting:");
            lbl.setFont(new Font("Arial", Font.BOLD, 13));
            p.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 1.0;
            linkField = new JTextField("https://meet.uninafoodlab.it/sessione-" + sessionNumber);
            p.add(linkField, gbc);

            // Sostituisce il panel precedente se esiste
            swapDetails("ONLINE", p);
        }

        private void createPraticaPanel() {
            JPanel p = new JPanel(new GridBagLayout());
            p.setBackground(UiUtil.CARD_LIGHT);
            p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            p.setName("PRATICA");

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill   = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);

            // Luogo
            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
            JLabel luogoLbl = new JLabel("Luogo:");
            luogoLbl.setFont(new Font("Arial", Font.BOLD, 13));
            p.add(luogoLbl, gbc);

            gbc.gridx = 1; gbc.weightx = 1.0;
            luogoField = new JTextField("Laboratorio Cucina - Sede UniNA");
            p.add(luogoField, gbc);

            // Ricette
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
            JLabel ricetteLbl = new JLabel("Numero Ricette:");
            ricetteLbl.setFont(new Font("Arial", Font.BOLD, 13));
            p.add(ricetteLbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0;
            JPanel ricetteCtrl = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            ricetteCtrl.setBackground(UiUtil.CARD_LIGHT);

            JSpinner numRicetteSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10, 1));
            numRicetteSpinner.setPreferredSize(new Dimension(60, 25));

            JButton configBtn = new JButton("Configura Ricette");
            configBtn.setFont(new Font("Arial", Font.BOLD, 12));
            configBtn.setBackground(UiUtil.UNINA_BLUE);
            configBtn.setForeground(Color.WHITE);
            configBtn.setFocusPainted(false);

            ricetteConfigurate = new ArrayList<>();

            configBtn.addActionListener(e -> {
                int num = (Integer) numRicetteSpinner.getValue();
                if (num == 0) { showWarning("Imposta il numero di ricette prima di configurare"); return; }
                RicetteConfigDialog dialog = new RicetteConfigDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(panel), num, ricetteConfigurate);
                List<RicetteConfigDialog.RicettaData> res = dialog.showDialog();
                if (res != null) {
                    ricetteConfigurate.clear();
                    ricetteConfigurate.addAll(res);
                    showSuccess(res.size() + " ricette configurate!");
                }
            });

            ricetteCtrl.add(numRicetteSpinner);
            ricetteCtrl.add(configBtn);
            p.add(ricetteCtrl, gbc);

            swapDetails("PRATICA", p);
        }

        /** Registra il panel nel detailsContainer con il nome dato */
        private void swapDetails(String name, JPanel p) {
            // I panel vengono creati prima di essere mostrati;
            // basta tenerli come campi e mostrarli on demand.
            // Usiamo putClientProperty per referenziarli.
            detailsContainer.putClientProperty(name, p);
        }

        private void showOnlineFields() {
            JPanel p = (JPanel) detailsContainer.getClientProperty("ONLINE");
            if (p == null) return;
            detailsContainer.removeAll();
            detailsContainer.add(p, BorderLayout.CENTER);
            detailsContainer.revalidate();
            detailsContainer.repaint();
        }

        private void showPraticaFields() {
            JPanel p = (JPanel) detailsContainer.getClientProperty("PRATICA");
            if (p == null) return;
            detailsContainer.removeAll();
            detailsContainer.add(p, BorderLayout.CENTER);
            detailsContainer.revalidate();
            detailsContainer.repaint();
        }

        public JPanel getPanel() { return panel; }

        /** Ora di inizio come LocalTime dai due spinner */
        private LocalTime getOraSelezionata() {
            int h = (Integer) oraSpinner.getValue();
            int m = (Integer) minutiSpinner.getValue();
            return LocalTime.of(h, m);
        }

        /** Durata in minuti */
        private int getDurataMinuti() {
            return (Integer) durataSpinner.getValue();
        }

        public SessionData getData() {
            LocalTime ora    = getOraSelezionata();
            int       durata = getDurataMinuti();

            if (onlineRadio.isSelected()) {
                String link = linkField.getText().trim();
                if (link.isEmpty()) { showError("Inserisci il link per la sessione " + sessionNumber); return null; }
                return new SessionData(SessionType.ONLINE, sessionDate, ora, durata, link, null, null);

            } else {
                String luogo = luogoField.getText().trim();
                if (luogo.isEmpty()) { showError("Inserisci il luogo per la sessione " + sessionNumber); return null; }
                return new SessionData(SessionType.PRATICA, sessionDate, ora, durata, null, luogo,
                                       new ArrayList<>(ricetteConfigurate));
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  DATA CLASSES
    // ═══════════════════════════════════════════════════════════════

    public enum SessionType { ONLINE, PRATICA }

    public static class SessionData {
        private final SessionType type;
        private final LocalDate   data;
        private final LocalTime   ora;
        private final int         durata;
        private final String      link;
        private final String      luogo;
        private final List<RicetteConfigDialog.RicettaData> ricette;

        public SessionData(SessionType type, LocalDate data, LocalTime ora, int durata,
                           String link, String luogo, List<RicetteConfigDialog.RicettaData> ricette) {
            this.type   = type;
            this.data   = data;
            this.ora    = ora;
            this.durata = durata;
            this.link   = link;
            this.luogo  = luogo;
            this.ricette = ricette;
        }

        public SessionType getType()   { return type; }
        public LocalDate   getData()   { return data; }
        public LocalTime   getOra()    { return ora; }
        public int         getDurata() { return durata; }
        public String      getLink()   { return link; }
        public String      getLuogo()  { return luogo; }
        public List<RicetteConfigDialog.RicettaData> getRicette() { return ricette; }
    }
}
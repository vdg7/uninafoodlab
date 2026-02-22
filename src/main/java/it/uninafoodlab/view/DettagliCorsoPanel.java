package it.uninafoodlab.view;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;

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

    // Callback iniettati da CorsoController
    private Consumer<ModificaSessioneRequest> onModifica;
    private Consumer<EliminaSessioneRequest>  onElimina;
    private Consumer<AggiungiRicetteRequest>  onAggiungiRicette;

    // ────────────────────────────────────────────────────────────────────────

    public DettagliCorsoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UiUtil.UNINA_GREY);
        setPadding(20);

        initHeader();
        initInfoPanel();
        initSessioniPanel();
    }

    // ── Costruzione UI (identica all'originale) ──────────────────────────────

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

        titoloLabel      = new JLabel(); titoloLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        categoriaLabel   = new JLabel(); categoriaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        dataInizioLabel  = new JLabel(); dataInizioLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        frequenzaLabel   = new JLabel(); frequenzaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        numSessioniLabel = new JLabel(); numSessioniLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        addInfoRow(infoPanel, gbc, 0, "Titolo:",           titoloLabel);
        addInfoRow(infoPanel, gbc, 1, "Categoria:",        categoriaLabel);
        addInfoRow(infoPanel, gbc, 2, "Data Inizio:",      dataInizioLabel);
        addInfoRow(infoPanel, gbc, 3, "Frequenza:",        frequenzaLabel);
        addInfoRow(infoPanel, gbc, 4, "Numero Sessioni:",  numSessioniLabel);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(UiUtil.UNINA_GREY);
        topContainer.add(headerPanel);
        topContainer.add(infoPanel);

        add(topContainer, BorderLayout.NORTH);
    }

    private void addInfoRow(JPanel p, GridBagConstraints gbc, int row, String label, JLabel value) {
        gbc.gridx = 0; gbc.gridy = row;
        p.add(createBoldLabel(label), gbc);
        gbc.gridx = 1;
        p.add(value, gbc);
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
        mainPanel.add(scrollPane,    BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(UiUtil.UNINA_BLUE);
        return label;
    }

    // ── API pubblica: callback ────────────────────────────────────────────────

    public void setOnModifica(Consumer<ModificaSessioneRequest> cb)      { this.onModifica         = cb; }
    public void setOnElimina(Consumer<EliminaSessioneRequest>  cb)       { this.onElimina          = cb; }
    public void setOnAggiungiRicette(Consumer<AggiungiRicetteRequest> cb){ this.onAggiungiRicette  = cb; }

    // ── loadCorso ────────────────────────────────────────────────────────────

    public void loadCorso(Corso corso,
                          List<SessioneOnline>  sessioniOnline,
                          List<SessionePratica> sessioniPratiche,
                          List<Ricetta>         ricetteConIngredienti) {
        this.corsoCorrente = corso;

        titoloLabel.setText(corso.getTitolo());
        categoriaLabel.setText(corso.getCategoria().getDisplayName());
        dataInizioLabel.setText(corso.getDataInizio().toString());
        frequenzaLabel.setText("Ogni " + corso.getFrequenza() + " giorni");
        numSessioniLabel.setText(String.valueOf(corso.getNumeroSessioni()));

        sessioniContainer.removeAll();

        if (!sessioniOnline.isEmpty()) {
            sessioniContainer.add(createSectionTitle("Sessioni Online"));
            for (SessioneOnline s : sessioniOnline) {
                sessioniContainer.add(createSessioneOnlineCard(s));
                sessioniContainer.add(Box.createVerticalStrut(10));
            }
        }

        if (!sessioniPratiche.isEmpty()) {
            sessioniContainer.add(createSectionTitle("Sessioni Pratiche"));
            for (SessionePratica s : sessioniPratiche) {
                List<Ricetta> ricetteSessione = ricetteConIngredienti.stream()
                    .filter(r -> r.getIdSessionePratica() == s.getIdSessionePratica())
                    .toList();
                sessioniContainer.add(createSessionePraticaCard(s, ricetteSessione));
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

    public Corso getCorsoCorrente() { return corsoCorrente; }

    // ── Card sessione online ──────────────────────────────────────────────────

    private JPanel createSessioneOnlineCard(SessioneOnline sessione) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiUtil.UNINA_BLUE, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        // Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel tipoLabel = new JLabel("SESSIONE ONLINE");
        tipoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tipoLabel.setForeground(UiUtil.UNINA_BLUE);

        String oraStr = sessione.getOra() != null
            ? " | Ora: " + String.format("%02d:%02d", sessione.getOra().getHour(), sessione.getOra().getMinute())
            : "";
        JLabel dataLabel = new JLabel("Data: " + sessione.getData() + oraStr + " | Durata: " + sessione.getDurata() + " min");
        dataLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel linkLabel = new JLabel("Link: " + sessione.getLink());
        linkLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        linkLabel.setForeground(Color.GRAY);

        infoPanel.add(tipoLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(dataLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(linkLabel);

        // Bottoni azione
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBackground(Color.WHITE);

        JButton btnModifica = makeActionBtn("✏ Modifica", UiUtil.UNINA_BLUE);
        btnModifica.addActionListener(e ->
            showModificaDialog(sessione.getIdSessioneOnline(), "ONLINE",
                               sessione.getData(), sessione.getOra()));

        JButton btnElimina = makeActionBtn("✕ Elimina", new Color(180, 40, 40));
        btnElimina.addActionListener(e ->
            showEliminaDialog(sessione.getIdSessioneOnline(), "ONLINE", sessione.getData()));

        btnPanel.add(btnModifica);
        btnPanel.add(Box.createVerticalStrut(6));
        btnPanel.add(btnElimina);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(btnPanel,  BorderLayout.EAST);
        return card;
    }

    // ── Card sessione pratica ─────────────────────────────────────────────────

    private JPanel createSessionePraticaCard(SessionePratica sessione, List<Ricetta> ricette) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 100, 50), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));

        // Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel tipoLabel = new JLabel("SESSIONE PRATICA");
        tipoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tipoLabel.setForeground(new Color(220, 100, 50));
        tipoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String oraStr = sessione.getOra() != null
            ? " | Ora: " + String.format("%02d:%02d", sessione.getOra().getHour(), sessione.getOra().getMinute())
            : "";
        JLabel dataLabel = new JLabel("Data: " + sessione.getData() + oraStr + " | Durata: " + sessione.getDurata() + " min");
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
            for (Ricetta r : ricette) {
                infoPanel.add(Box.createVerticalStrut(5));
                JPanel rp = createRicettaPanel(r);
                rp.setAlignmentX(Component.LEFT_ALIGNMENT);
                infoPanel.add(rp);
            }
        } else {
            infoPanel.add(Box.createVerticalStrut(5));
            JLabel noRicette = new JLabel("Nessuna ricetta associata");
            noRicette.setFont(new Font("Arial", Font.ITALIC, 12));
            noRicette.setForeground(Color.GRAY);
            noRicette.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(noRicette);
        }

        // Bottoni azione
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JButton btnModifica = makeActionBtn("✏ Modifica", UiUtil.UNINA_BLUE);
        btnModifica.addActionListener(e ->
            showModificaDialog(sessione.getIdSessionePratica(), "PRATICA",
                               sessione.getData(), sessione.getOra()));

        JButton btnRicette = makeActionBtn("＋ Ricette", new Color(39, 174, 96));
        btnRicette.addActionListener(e ->
            showAggiungiRicetteDialog(sessione.getIdSessionePratica()));

        JButton btnElimina = makeActionBtn("✕ Elimina", new Color(180, 40, 40));
        btnElimina.addActionListener(e ->
            showEliminaDialog(sessione.getIdSessionePratica(), "PRATICA", sessione.getData()));

        btnPanel.add(btnModifica);
        btnPanel.add(Box.createVerticalStrut(6));
        btnPanel.add(btnRicette);
        btnPanel.add(Box.createVerticalStrut(6));
        btnPanel.add(btnElimina);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(btnPanel,  BorderLayout.EAST);
        return card;
    }

    // ── Dialog: Modifica data/ora ─────────────────────────────────────────────

    private void showModificaDialog(int idSessione, String tipo,
                                    LocalDate dataCorrente, LocalTime oraCorrente) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dlg = new JDialog(owner, "Modifica Sessione", Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.setSize(440, 310);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(7, 6, 7, 6);

        // Data
        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        form.add(boldLbl("Data (AAAA-MM-GG):"), g);
        g.gridx = 1; g.weightx = 1;
        JTextField dataField = new JTextField(dataCorrente != null ? dataCorrente.toString() : "");
        form.add(dataField, g);

        // Ora
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        form.add(boldLbl("Ora (HH:MM):"), g);
        g.gridx = 1; g.weightx = 1;
        String oraInit = oraCorrente != null
            ? String.format("%02d:%02d", oraCorrente.getHour(), oraCorrente.getMinute()) : "09:00";
        JTextField oraField = new JTextField(oraInit);
        form.add(oraField, g);

        // Notifica
        g.gridx = 0; g.gridy = 2; g.gridwidth = 2;
        JCheckBox chkNotifica = new JCheckBox("Invia notifica agli iscritti", true);
        chkNotifica.setBackground(Color.WHITE);
        form.add(chkNotifica, g);

        // Portata notifica
        g.gridy = 3;
        JPanel scopePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        scopePanel.setBackground(Color.WHITE);
        JRadioButton rbCorso   = new JRadioButton("Solo questo corso", true);
        JRadioButton rbGlobale = new JRadioButton("Tutti i corsi");
        rbCorso.setBackground(Color.WHITE);
        rbGlobale.setBackground(Color.WHITE);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbCorso); bg.add(rbGlobale);
        scopePanel.add(new JLabel("Portata:"));
        scopePanel.add(rbCorso);
        scopePanel.add(rbGlobale);
        form.add(scopePanel, g);

        // Bottoni
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnRow.setBackground(Color.WHITE);
        JButton btnAnnulla  = new JButton("Annulla");
        JButton btnConferma = new JButton("Conferma");
        btnConferma.setBackground(UiUtil.UNINA_BLUE);
        btnConferma.setForeground(Color.WHITE);
        btnConferma.setFocusPainted(false);

        btnAnnulla.addActionListener(e -> dlg.dispose());
        btnConferma.addActionListener(e -> {
            try {
                LocalDate nuovaData = LocalDate.parse(dataField.getText().trim());
                String[] parti = oraField.getText().trim().split(":");
                LocalTime nuovaOra = LocalTime.of(Integer.parseInt(parti[0]), Integer.parseInt(parti[1]));
                if (onModifica != null) {
                    onModifica.accept(new ModificaSessioneRequest(
                        idSessione, tipo, nuovaData, nuovaOra,
                        chkNotifica.isSelected(), rbGlobale.isSelected(), corsoCorrente));
                }
                dlg.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg,
                    "Formato non valido.\nData: AAAA-MM-GG   Ora: HH:MM",
                    "Errore input", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRow.add(btnAnnulla);
        btnRow.add(btnConferma);
        dlg.add(form,   BorderLayout.CENTER);
        dlg.add(btnRow, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── Dialog: Elimina sessione ──────────────────────────────────────────────

    private void showEliminaDialog(int idSessione, String tipo, LocalDate data) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dlg = new JDialog(owner, "Elimina Sessione", Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.setSize(420, 220);
        dlg.setLocationRelativeTo(this);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));

        JLabel msg = new JLabel("<html>Eliminare la sessione del <b>" + data + "</b>?<br>L'azione è irreversibile.</html>");
        msg.setFont(new Font("Arial", Font.PLAIN, 14));
        msg.setAlignmentX(LEFT_ALIGNMENT);

        JCheckBox chkNotifica = new JCheckBox("Invia notifica di cancellazione", true);
        chkNotifica.setBackground(Color.WHITE);
        chkNotifica.setAlignmentX(LEFT_ALIGNMENT);

        JPanel scopePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        scopePanel.setBackground(Color.WHITE);
        scopePanel.setAlignmentX(LEFT_ALIGNMENT);
        JRadioButton rbCorso   = new JRadioButton("Solo questo corso", true);
        JRadioButton rbGlobale = new JRadioButton("Tutti i corsi");
        rbCorso.setBackground(Color.WHITE);
        rbGlobale.setBackground(Color.WHITE);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbCorso); bg.add(rbGlobale);
        scopePanel.add(new JLabel("Portata:"));
        scopePanel.add(rbCorso);
        scopePanel.add(rbGlobale);

        content.add(msg);
        content.add(Box.createVerticalStrut(10));
        content.add(chkNotifica);
        content.add(Box.createVerticalStrut(4));
        content.add(scopePanel);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnRow.setBackground(Color.WHITE);
        JButton btnAnnulla = new JButton("Annulla");
        JButton btnElimina = new JButton("Elimina");
        btnElimina.setBackground(new Color(180, 40, 40));
        btnElimina.setForeground(Color.WHITE);
        btnElimina.setFocusPainted(false);

        btnAnnulla.addActionListener(e -> dlg.dispose());
        btnElimina.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(dlg,
                    "Sei sicuro? L'operazione è irreversibile.",
                    "Conferma eliminazione", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                if (r != JOptionPane.YES_OPTION) return;
            if (onElimina != null) {
                onElimina.accept(new EliminaSessioneRequest(
                    idSessione, tipo,
                    chkNotifica.isSelected(), rbGlobale.isSelected(), corsoCorrente));
            }
            dlg.dispose();
        });

        btnRow.add(btnAnnulla);
        btnRow.add(btnElimina);
        dlg.add(content, BorderLayout.CENTER);
        dlg.add(btnRow,  BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── Dialog: Aggiungi ricette ──────────────────────────────────────────────

    private void showAggiungiRicetteDialog(int idSessionePratica) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dlg = new JDialog(owner, "Aggiungi Ricette", Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.setSize(380, 160);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(7, 6, 7, 6);

        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        form.add(boldLbl("Numero di nuove ricette:"), g);
        g.gridx = 1; g.weightx = 1;
        JSpinner numSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        form.add(numSpinner, g);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnRow.setBackground(Color.WHITE);
        JButton btnAnnulla = new JButton("Annulla");
        JButton btnProcedi = new JButton("Configura →");
        btnProcedi.setBackground(new Color(39, 174, 96));
        btnProcedi.setForeground(Color.WHITE);
        btnProcedi.setFocusPainted(false);

        btnAnnulla.addActionListener(e -> dlg.dispose());
        btnProcedi.addActionListener(e -> {
            int num = (Integer) numSpinner.getValue();
            dlg.dispose();
            RicetteConfigDialog rcd = new RicetteConfigDialog(owner, num, new java.util.ArrayList<>());
            List<RicetteConfigDialog.RicettaData> risultato = rcd.showDialog();
            if (risultato != null && !risultato.isEmpty() && onAggiungiRicette != null) {
                onAggiungiRicette.accept(new AggiungiRicetteRequest(
                    idSessionePratica, risultato, corsoCorrente));
            }
        });

        btnRow.add(btnAnnulla);
        btnRow.add(btnProcedi);
        dlg.add(form,   BorderLayout.CENTER);
        dlg.add(btnRow, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── Utility UI ────────────────────────────────────────────────────────────

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

        if (!ricetta.getIngredienti().isEmpty()) {
            JPanel ingredientiPanel = new JPanel();
            ingredientiPanel.setLayout(new BoxLayout(ingredientiPanel, BoxLayout.Y_AXIS));
            ingredientiPanel.setBackground(UiUtil.CARD_LIGHT);
            ingredientiPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel ingredientiLabel = new JLabel("Ingredienti:");
            ingredientiLabel.setFont(new Font("Arial", Font.ITALIC, 11));
            ingredientiLabel.setForeground(Color.DARK_GRAY);
            ingredientiPanel.add(ingredientiLabel);

            for (RicettaIngrediente ri : ricetta.getIngredienti()) {
                JLabel ingLabel = new JLabel("  - " + ri.getIngrediente().getNome()
                    + ": " + ri.getQuantita() + " " + ri.getIngrediente().getUnitaMisura());
                ingLabel.setFont(new Font("Arial", Font.PLAIN, 11));
                ingredientiPanel.add(ingLabel);
            }
            panel.add(nomeLabel,        BorderLayout.NORTH);
            panel.add(ingredientiPanel, BorderLayout.CENTER);
        } else {
            panel.add(nomeLabel, BorderLayout.CENTER);
        }
        return panel;
    }

    private JButton makeActionBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(115, 30));
        return b;
    }

    private JLabel boldLbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 13));
        return l;
    }

    // ── Record DTO ────────────────────────────────────────────────────────────

    public record ModificaSessioneRequest(
        int        idSessione,
        String     tipo,          // "ONLINE" | "PRATICA"
        LocalDate  nuovaData,
        LocalTime  nuovaOra,
        boolean    inviaNotifica,
        boolean    notificaGlobale,
        Corso      corso
    ) {}

    public record EliminaSessioneRequest(
        int     idSessione,
        String  tipo,
        boolean inviaNotifica,
        boolean notificaGlobale,
        Corso   corso
    ) {}

    public record AggiungiRicetteRequest(
        int                                   idSessionePratica,
        List<RicetteConfigDialog.RicettaData> ricette,
        Corso                                 corso
    ) {}
}
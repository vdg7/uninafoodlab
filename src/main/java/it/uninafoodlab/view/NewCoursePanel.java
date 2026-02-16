package it.uninafoodlab.view;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Consumer;

import javax.swing.*;

import it.uninafoodlab.model.enums.Categoria;

/**
 * Panel per creare un nuovo corso.
 * Step 1: Dati base del corso
 */
public class NewCoursePanel extends BasePanel {
    
    private JTextField titoloField;
    private JComboBox<String> categoriaCombo;
    private JTextField dataInizioField;
    private JSpinner frequenzaSpinner;
    private JSpinner numSessioniSpinner;
    private JButton creaButton;
    
    private Consumer<CourseData> onCreateAction;
    
    public NewCoursePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UiUtil.UNINA_GREY);
        setPadding(20);
        
        initHeader();
        initForm();
    }
    
    private void initHeader() {
        JLabel titleLabel = new JLabel("Crea Nuovo Corso");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(UiUtil.UNINA_BLUE);
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(UiUtil.UNINA_GREY);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void initForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiUtil.CARD_BORDER, 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        
        // Titolo
        gbc.gridy = 0;
        formPanel.add(createLabel("Titolo Corso:"), gbc);
        
        gbc.gridy = 1;
        titoloField = new JTextField(30);
        titoloField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(titoloField, gbc);
        
        // Categoria
        gbc.gridy = 2;
        formPanel.add(createLabel("Categoria:"), gbc);
        
        gbc.gridy = 3;
        String[] categorie = new String[Categoria.values().length];
        for (int i = 0; i < Categoria.values().length; i++) {
            categorie[i] = Categoria.values()[i].getDisplayName();
        }
        categoriaCombo = new JComboBox<>(categorie);
        categoriaCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(categoriaCombo, gbc);
        
        // Data Inizio
        gbc.gridy = 4;
        formPanel.add(createLabel("Data Inizio (YYYY-MM-DD):"), gbc);
        
        gbc.gridy = 5;
        dataInizioField = new JTextField(30);
        dataInizioField.setFont(new Font("Arial", Font.PLAIN, 14));
        dataInizioField.setText(LocalDate.now().plusDays(7).toString()); // Suggerimento
        formPanel.add(dataInizioField, gbc);
        
        // Frequenza
        gbc.gridy = 6;
        formPanel.add(createLabel("Frequenza Sessioni (giorni tra sessioni):"), gbc);
        
        gbc.gridy = 7;
        SpinnerNumberModel frequenzaModel = new SpinnerNumberModel(7, 1, 30, 1);
        frequenzaSpinner = new JSpinner(frequenzaModel);
        frequenzaSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(frequenzaSpinner, gbc);
        
        // Numero Sessioni
        gbc.gridy = 8;
        formPanel.add(createLabel("Numero Totale Sessioni:"), gbc);
        
        gbc.gridy = 9;
        SpinnerNumberModel numSessioniModel = new SpinnerNumberModel(4, 1, 20, 1);
        numSessioniSpinner = new JSpinner(numSessioniModel);
        numSessioniSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(numSessioniSpinner, gbc);
        
        // Pulsante Crea
        gbc.gridy = 10;
        gbc.insets = new Insets(30, 0, 0, 0);
        creaButton = new JButton("Avanti - Configura Sessioni");
        creaButton.setFont(new Font("Arial", Font.BOLD, 16));
        creaButton.setBackground(UiUtil.UNINA_BLUE);
        creaButton.setForeground(Color.WHITE);
        creaButton.setFocusPainted(false);
        creaButton.setPreferredSize(new Dimension(300, 45));
        creaButton.addActionListener(e -> handleCreate());
        formPanel.add(creaButton, gbc);
        
        // Centra il form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UiUtil.UNINA_GREY);
        centerPanel.add(formPanel);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(UiUtil.UNINA_BLUE);
        return label;
    }
    
    /**
     * Gestisce la creazione del corso.
     */
    private void handleCreate() {
        // Validazione
        String titolo = titoloField.getText().trim();
        if (titolo.isEmpty()) {
            showError("Il titolo del corso è obbligatorio");
            return;
        }
        
        String categoriaStr = (String) categoriaCombo.getSelectedItem();
        Categoria categoria = Categoria.fromString(categoriaStr);
        
        LocalDate dataInizio;
        try {
            dataInizio = LocalDate.parse(dataInizioField.getText().trim());
        } catch (DateTimeParseException e) {
            showError("Data non valida. Usa il formato YYYY-MM-DD (es: 2025-06-15)");
            return;
        }
        
        // Verifica che la data sia futura
        if (dataInizio.isBefore(LocalDate.now())) {
            showError("La data di inizio deve essere futura");
            return;
        }
        
        int frequenza = (Integer) frequenzaSpinner.getValue();
        int numSessioni = (Integer) numSessioniSpinner.getValue();
        
        // Crea oggetto dati corso
        CourseData courseData = new CourseData(
            titolo, categoria, dataInizio, frequenza, numSessioni
        );
        
        // Chiama callback
        if (onCreateAction != null) {
            onCreateAction.accept(courseData);
        }
    }
    
    /**
     * Imposta l'azione da eseguire quando si clicca "Avanti".
     */
    public void setCreateAction(Consumer<CourseData> action) {
        this.onCreateAction = action;
    }
    
    /**
     * Pulisce i campi del form.
     */
    public void clearFields() {
        titoloField.setText("");
        categoriaCombo.setSelectedIndex(0);
        dataInizioField.setText(LocalDate.now().plusDays(7).toString());
        frequenzaSpinner.setValue(7);
        numSessioniSpinner.setValue(4);
    }
    
    /**
     * Classe interna per trasferire i dati del corso.
     */
    public static class CourseData {
        private final String titolo;
        private final Categoria categoria;
        private final LocalDate dataInizio;
        private final int frequenza;
        private final int numSessioni;
        
        public CourseData(String titolo, Categoria categoria, LocalDate dataInizio, 
                          int frequenza, int numSessioni) {
            this.titolo = titolo;
            this.categoria = categoria;
            this.dataInizio = dataInizio;
            this.frequenza = frequenza;
            this.numSessioni = numSessioni;
        }
        
        public String getTitolo() { return titolo; }
        public Categoria getCategoria() { return categoria; }
        public LocalDate getDataInizio() { return dataInizio; }
        public int getFrequenza() { return frequenza; }
        public int getNumSessioni() { return numSessioni; }
    }
}
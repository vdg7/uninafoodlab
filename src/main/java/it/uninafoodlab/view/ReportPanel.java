package it.uninafoodlab.view;

import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import it.uninafoodlab.dao.ReportDAO.ReportMensile;

/**
 * Panel per la visualizzazione del report mensile.
 *
 * Mostra:
 *  - 6 stat-card numeriche (corsi, sessioni online, pratiche, media/max/min ricette)
 *  - Grafico a barre: andamento sessioni online vs pratiche per mese nell'anno selezionato
 *  - Grafico a torta: distribuzione sessioni online vs pratiche nel mese selezionato
 *
 * Il panel non accede direttamente ai DAO: espone {@link #setRefreshAction} affinché
 * il controller inietti il callback di aggiornamento, e {@link #updateData} per ricevere i dati.
 */
public class ReportPanel extends BasePanel {

    // ── filtri ──────────────────────────────────────────────────────────────
    private JComboBox<Integer> yearCombo;
    private JComboBox<String>  monthCombo;
    private JButton            refreshBtn;

    // ── stat cards ──────────────────────────────────────────────────────────
    private JLabel lblCorsi;
    private JLabel lblOnline;
    private JLabel lblPratiche;
    private JLabel lblMedia;
    private JLabel lblMax;
    private JLabel lblMin;

    // ── grafici ─────────────────────────────────────────────────────────────
    private JPanel chartsContainer;

    // ── callback iniettato dal controller ───────────────────────────────────
    private BiConsumer<YearMonth, Integer> onRefresh; // (yearMonth, idChef)

    private static final String[] MESI = {
        "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
        "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
    };
    private static final String[] MESI_BREVI = {
        "Gen", "Feb", "Mar", "Apr", "Mag", "Giu",
        "Lug", "Ago", "Set", "Ott", "Nov", "Dic"
    };

    // ────────────────────────────────────────────────────────────────────────
    //  Costruttore
    // ────────────────────────────────────────────────────────────────────────

    public ReportPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(UiUtil.UNINA_GREY);
        setPadding(20);

        add(buildHeader(),      BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
    }

    // ────────────────────────────────────────────────────────────────────────
    //  Costruzione UI
    // ────────────────────────────────────────────────────────────────────────

    /** Header: titolo + filtri mese/anno + bottone aggiorna */
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setBackground(UiUtil.UNINA_GREY);

        // Titolo
        JLabel title = new JLabel("Report e Statistiche");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(UiUtil.UNINA_BLUE);
        header.add(title, BorderLayout.WEST);

        // Filtri
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filters.setBackground(UiUtil.UNINA_GREY);

        monthCombo = new JComboBox<>(MESI);
        monthCombo.setSelectedIndex(YearMonth.now().getMonthValue() - 1);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 13));

        int currentYear = LocalDate.now().getYear();
        Integer[] anni  = new Integer[currentYear - 2022];
        for (int i = 0; i < anni.length; i++) anni[i] = 2023 + i;
        yearCombo = new JComboBox<>(anni);
        yearCombo.setSelectedItem(currentYear);
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 13));

        refreshBtn = new JButton("Aggiorna Report");
        refreshBtn.setBackground(UiUtil.UNINA_BLUE);
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 13));
        refreshBtn.addActionListener(e -> fireRefresh());

        filters.add(new JLabel("Mese:"));
        filters.add(monthCombo);
        filters.add(new JLabel("Anno:"));
        filters.add(yearCombo);
        filters.add(Box.createHorizontalStrut(6));
        filters.add(refreshBtn);

        header.add(filters, BorderLayout.EAST);
        return header;
    }

    /** Pannello centrale scorrevole: stat-cards sopra, grafici sotto */
    private JScrollPane buildCenterPanel() {
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(UiUtil.UNINA_GREY);

        center.add(buildStatCardsPanel());
        center.add(Box.createVerticalStrut(16));
        center.add(buildChartsPanel());

        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(UiUtil.UNINA_GREY);
        return scroll;
    }

    /** Griglia 2x3 di stat-card */
    private JPanel buildStatCardsPanel() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 12, 12));
        grid.setBackground(UiUtil.UNINA_GREY);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        lblCorsi    = new JLabel("–", SwingConstants.CENTER);
        lblOnline   = new JLabel("–", SwingConstants.CENTER);
        lblPratiche = new JLabel("–", SwingConstants.CENTER);
        lblMedia    = new JLabel("–", SwingConstants.CENTER);
        lblMax      = new JLabel("–", SwingConstants.CENTER);
        lblMin      = new JLabel("–", SwingConstants.CENTER);

        grid.add(buildCard("Corsi del mese",        lblCorsi,    UiUtil.UNINA_BLUE));
        grid.add(buildCard("Sessioni Online",        lblOnline,   new Color(41, 128, 185)));
        grid.add(buildCard("Sessioni Pratiche",      lblPratiche, new Color(190, 75, 20)));
        grid.add(buildCard("Media Ricette/Sessione", lblMedia,    new Color(39, 174, 96)));
        grid.add(buildCard("Max Ricette/Sessione",   lblMax,      new Color(142, 68, 173)));
        grid.add(buildCard("Min Ricette/Sessione",   lblMin,      new Color(100, 100, 100)));

        return grid;
    }

    /** Singola stat-card bianca con titolo e valore colorato */
    private JPanel buildCard(String title, JLabel valueLabel, Color valueColor) {
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiUtil.CARD_BORDER, 1),
            new EmptyBorder(10, 8, 10, 8)
        ));

        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setForeground(Color.DARK_GRAY);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(valueColor);

        card.add(lbl);
        card.add(valueLabel);
        return card;
    }

    /** Contenitore grafico (verrà popolato da updateData) */
    private JPanel buildChartsPanel() {
        chartsContainer = new JPanel(new GridLayout(1, 2, 16, 0));
        chartsContainer.setBackground(UiUtil.UNINA_GREY);
        chartsContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 380));

        // placeholder iniziale
        chartsContainer.add(buildPlaceholder("Seleziona un mese e premi\n\"Aggiorna Report\""));
        chartsContainer.add(buildPlaceholder(""));
        return chartsContainer;
    }

    /** Pannello placeholder grigio chiaro */
    private JPanel buildPlaceholder(String text) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(UiUtil.CARD_BORDER, 1));
        if (!text.isEmpty()) {
            JLabel lbl = new JLabel("<html><center>" + text.replace("\n", "<br>") + "</center></html>",
                                    SwingConstants.CENTER);
            lbl.setForeground(Color.LIGHT_GRAY);
            lbl.setFont(new Font("Arial", Font.ITALIC, 14));
            p.add(lbl);
        }
        return p;
    }

    // ────────────────────────────────────────────────────────────────────────
    //  API pubblica
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Inietta il callback che viene invocato quando l'utente preme "Aggiorna Report".
     * Il BiConsumer riceve (YearMonth selezionato, idChef corrente).
     * Il controller chiamerà poi {@link #updateData} per aggiornare la vista.
     *
     * @param action BiConsumer<YearMonth, Integer>
     */
    public void setRefreshAction(BiConsumer<YearMonth, Integer> action) {
        this.onRefresh = action;
    }

    /**
     * Aggiorna la vista con i dati forniti dal controller.
     *
     * @param report       statistiche mensili
     * @param datiAnnuali  Map mese->int[]{online,pratiche} per il grafico annuale
     * @param anno         anno selezionato (per il titolo del grafico a barre)
     */
    public void updateData(ReportMensile report, Map<Integer, int[]> datiAnnuali, int anno) {
        // -- stat cards
        lblCorsi.setText(String.valueOf(report.getNumeroCorsi()));
        lblOnline.setText(String.valueOf(report.getNumeroSessioniOnline()));
        lblPratiche.setText(String.valueOf(report.getNumeroSessioniPratiche()));
        lblMedia.setText(String.format("%.1f", report.getMediaRicettePerSessione()));
        lblMax.setText(String.valueOf(report.getMaxRicettePerSessione()));
        lblMin.setText(String.valueOf(report.getMinRicettePerSessione()));

        // -- grafici
        chartsContainer.removeAll();
        chartsContainer.add(buildBarChart(datiAnnuali, anno));
        chartsContainer.add(buildPieChart(report));
        chartsContainer.revalidate();
        chartsContainer.repaint();
    }

    /** Restituisce il YearMonth correntemente selezionato nei filtri */
    public YearMonth getSelectedYearMonth() {
        int month = monthCombo.getSelectedIndex() + 1;
        int year  = (Integer) yearCombo.getSelectedItem();
        return YearMonth.of(year, month);
    }

    /** Restituisce l'anno correntemente selezionato nel filtro */
    public int getSelectedYear() {
        return (Integer) yearCombo.getSelectedItem();
    }

    // ────────────────────────────────────────────────────────────────────────
    //  Grafici JFreeChart
    // ────────────────────────────────────────────────────────────────────────

    private ChartPanel buildBarChart(Map<Integer, int[]> dati, int anno) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 1; i <= 12; i++) {
            int[] vals = dati.get(i);
            dataset.addValue(vals[0], "Online",   MESI_BREVI[i - 1]);
            dataset.addValue(vals[1], "Pratiche", MESI_BREVI[i - 1]);
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "Sessioni per mese – " + anno,
            "Mese", "Sessioni",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        chart.setBackgroundPaint(Color.WHITE);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(41, 128, 185));  // Online  = blu
        renderer.setSeriesPaint(1, new Color(190, 75, 20));   // Pratiche = arancio
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.05);

        CategoryAxis axis = plot.getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(Color.WHITE);
        cp.setBorder(BorderFactory.createLineBorder(UiUtil.CARD_BORDER, 1));
        return cp;
    }

    @SuppressWarnings("unchecked")
    private ChartPanel buildPieChart(ReportMensile report) {
        int online   = report.getNumeroSessioniOnline();
        int pratiche = report.getNumeroSessioniPratiche();
        int totale   = online + pratiche;

        DefaultPieDataset dataset = new DefaultPieDataset();

        if (totale == 0) {
            dataset.setValue("Nessuna sessione", 1);
        } else {
            if (online   > 0) dataset.setValue("Online ("   + online   + ")", online);
            if (pratiche > 0) dataset.setValue("Pratiche (" + pratiche + ")", pratiche);
        }

        String meseScelto = MESI[monthCombo.getSelectedIndex()];
        JFreeChart chart = ChartFactory.createPieChart(
            "Distribuzione sessioni – " + meseScelto,
            dataset, true, true, false
        );
        chart.setBackgroundPaint(Color.WHITE);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);

        if (totale > 0) {
            plot.setSectionPaint("Online ("   + online   + ")", new Color(41, 128, 185));
            plot.setSectionPaint("Pratiche (" + pratiche + ")", new Color(190, 75, 20));
        } else {
            plot.setSectionPaint("Nessuna sessione", Color.LIGHT_GRAY);
        }

        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(Color.WHITE);
        cp.setBorder(BorderFactory.createLineBorder(UiUtil.CARD_BORDER, 1));
        return cp;
    }

    // ────────────────────────────────────────────────────────────────────────
    //  Callback interno
    // ────────────────────────────────────────────────────────────────────────

    private void fireRefresh() {
        if (onRefresh != null) {
            onRefresh.accept(getSelectedYearMonth(), getSelectedYear());
        }
    }
}

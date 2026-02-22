package it.uninafoodlab.view;

import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.axis.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import it.uninafoodlab.dao.ReportDAO.ReportMensile;

/**
 * Panel Report Mensile.
 */
public class ReportPanel extends BasePanel {

    // ── Filtri ──
    private JComboBox<Integer> yearCombo;
    private JComboBox<String>  monthCombo;

    // ── Aree grafici (rimpiazzate ad ogni aggiornamento) ──
    private JPanel pieHolder;
    private JPanel barHolder;

    // ── Stat-cards dinamiche ──
    private JLabel lblCorsi;
    private JLabel lblOnline;
    private JLabel lblPratiche;
    private JLabel lblMedia;
    private JLabel lblMax;
    private JLabel lblMin;

    private JPanel miniBarOnline;
    private JPanel miniBarPratiche;
    private JPanel miniBarMedia;
    private JPanel miniBarMax;
    private JPanel miniBarMin;

    private BiConsumer<YearMonth, Integer> onRefresh;

    private static final String[] MESI = {
        "Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno",
        "Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre"
    };

    private static final Color COL_ONLINE   = new Color(41, 128, 185);
    private static final Color COL_PRATICHE = new Color(190, 75, 20);
    private static final Color COL_MEDIA    = new Color(39, 174, 96);
    private static final Color COL_MAX      = new Color(142, 68, 173);
    private static final Color COL_MIN      = new Color(100, 100, 100);
    private static final Color COL_CORSI    = UiUtil.UNINA_BLUE;

    // ═══════════════════════════════════════════════════════════════

    public ReportPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(UiUtil.UNINA_GREY);
        setPadding(20);

        add(buildHeader(),      BorderLayout.NORTH);
        add(buildChartArea(),   BorderLayout.CENTER);
        add(buildStatCards(),   BorderLayout.SOUTH);
    }

    // ── Costruzione UI ───────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout(10, 0));
        h.setBackground(UiUtil.UNINA_GREY);

        JLabel title = new JLabel("Report e Statistiche");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(UiUtil.UNINA_BLUE);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filters.setBackground(UiUtil.UNINA_GREY);

        monthCombo = new JComboBox<>(MESI);
        monthCombo.setSelectedIndex(YearMonth.now().getMonthValue() - 1);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 13));

        int curYear = LocalDate.now().getYear();
        Integer[] anni = new Integer[curYear - 2022];
        for (int i = 0; i < anni.length; i++) anni[i] = 2023 + i;
        yearCombo = new JComboBox<>(anni);
        yearCombo.setSelectedItem(curYear);
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton btn = new JButton("Aggiorna Report");
        btn.setBackground(UiUtil.UNINA_BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.addActionListener(e -> fireRefresh());

        filters.add(new JLabel("Mese:"));
        filters.add(monthCombo);
        filters.add(new JLabel("Anno:"));
        filters.add(yearCombo);
        filters.add(Box.createHorizontalStrut(4));
        filters.add(btn);

        h.add(title,   BorderLayout.WEST);
        h.add(filters, BorderLayout.EAST);
        return h;
    }

    /**
     * Area centrale: torta a sinistra, barre ricette a destra.
     * Entrambi usano placeholders iniziali.
     */
    private JPanel buildChartArea() {
        JPanel area = new JPanel(new GridLayout(1, 2, 14, 0));
        area.setBackground(UiUtil.UNINA_GREY);

        pieHolder = placeholder("Sessioni Online vs Pratiche");
        barHolder = placeholder("Statistiche Ricette per Sessione");

        area.add(pieHolder);
        area.add(barHolder);
        return area;
    }

    /**
     * Riga di stat-cards in basso: ogni card mostra valore numerico +
     * mini barra-progresso proporzionale (visual context).
     */
    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 6, 10, 0));
        row.setBackground(UiUtil.UNINA_GREY);
        row.setPreferredSize(new Dimension(0, 110));

        lblCorsi    = valueLbl(); miniBarOnline   = miniBar(COL_CORSI);
        lblOnline   = valueLbl(); miniBarOnline   = miniBar(COL_ONLINE);
        lblPratiche = valueLbl(); miniBarPratiche = miniBar(COL_PRATICHE);
        lblMedia    = valueLbl(); miniBarMedia    = miniBar(COL_MEDIA);
        lblMax      = valueLbl(); miniBarMax      = miniBar(COL_MAX);
        lblMin      = valueLbl(); miniBarMin      = miniBar(COL_MIN);

        JPanel miniBarCorsi = miniBar(COL_CORSI);
        lblCorsi.setForeground(COL_CORSI);
        lblOnline.setForeground(COL_ONLINE);
        lblPratiche.setForeground(COL_PRATICHE);
        lblMedia.setForeground(COL_MEDIA);
        lblMax.setForeground(COL_MAX);
        lblMin.setForeground(COL_MIN);

        row.add(statCard("Corsi del Mese",        lblCorsi,    miniBarCorsi));
        row.add(statCard("Sessioni Online",        lblOnline,   miniBarOnline));
        row.add(statCard("Sessioni Pratiche",      lblPratiche, miniBarPratiche));
        row.add(statCard("Media Ricette/Sess.",    lblMedia,    miniBarMedia));
        row.add(statCard("Max Ricette/Sess.",      lblMax,      miniBarMax));
        row.add(statCard("Min Ricette/Sess.",      lblMin,      miniBarMin));

        return row;
    }

    private JLabel valueLbl() {
        JLabel l = new JLabel("–", SwingConstants.CENTER);
        l.setFont(new Font("Arial", Font.BOLD, 26));
        return l;
    }

    private JPanel miniBar(Color color) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // disegna barra larga quanto la percentuale salvata in clientProperty
                Object pct = getClientProperty("pct");
                if (pct instanceof Double d && d > 0) {
                    g.setColor(color);
                    int w = (int) (getWidth() * d);
                    g.fillRoundRect(0, 0, w, getHeight(), 4, 4);
                }
            }
        };
        p.setBackground(new Color(230, 230, 230));
        p.setPreferredSize(new Dimension(0, 8));
        p.putClientProperty("pct", 0.0);
        return p;
    }

    private JPanel statCard(String title, JLabel valueLbl, JPanel miniBarPanel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiUtil.CARD_BORDER, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLbl.setForeground(Color.DARK_GRAY);
        titleLbl.setAlignmentX(CENTER_ALIGNMENT);

        valueLbl.setAlignmentX(CENTER_ALIGNMENT);

        JPanel barWrap = new JPanel(new BorderLayout());
        barWrap.setBackground(Color.WHITE);
        barWrap.add(miniBarPanel, BorderLayout.CENTER);
        barWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));

        card.add(titleLbl);
        card.add(Box.createVerticalStrut(4));
        card.add(valueLbl);
        card.add(Box.createVerticalGlue());
        card.add(barWrap);
        return card;
    }

    private JPanel placeholder(String msg) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(UiUtil.CARD_BORDER, 1));
        JLabel l = new JLabel("<html><center>" + msg + "<br><br><i style='color:#bbb'>Premi «Aggiorna Report»</i></center></html>",
            SwingConstants.CENTER);
        l.setFont(new Font("Arial", Font.PLAIN, 14));
        l.setForeground(new Color(180, 180, 180));
        p.add(l);
        return p;
    }

    // ── API pubblica ─────────────────────────────────────────────────────────

    public void setRefreshAction(BiConsumer<YearMonth, Integer> action) {
        this.onRefresh = action;
    }

    /**
     * Aggiorna tutta la vista con i dati freschi dal controller.
     *
     * @param report       statistiche mensili
     * @param datiAnnuali  Map mese->int[]{online,pratiche} (non più usato qui, mantenuto per API)
     * @param anno         anno selezionato
     */
    public void updateData(ReportMensile report, Map<Integer, int[]> datiAnnuali, int anno) {
        int online   = report.getNumeroSessioniOnline();
        int pratiche = report.getNumeroSessioniPratiche();
        int corsi    = report.getNumeroCorsi();
        double media = report.getMediaRicettePerSessione();
        int max      = report.getMaxRicettePerSessione();
        int min      = report.getMinRicettePerSessione();

        // ── stat-card labels ──
        lblCorsi.setText(String.valueOf(corsi));
        lblOnline.setText(String.valueOf(online));
        lblPratiche.setText(String.valueOf(pratiche));
        lblMedia.setText(String.format("%.1f", media));
        lblMax.setText(String.valueOf(max));
        lblMin.setText(String.valueOf(min));

        // ── mini-bar percentuali ──
        int totSess = online + pratiche;
        setMiniBarPct(miniBarOnline,   totSess > 0 ? (double) online   / totSess : 0);
        setMiniBarPct(miniBarPratiche, totSess > 0 ? (double) pratiche / totSess : 0);
        double maxVal = Math.max(media, Math.max(max, 1));
        setMiniBarPct(miniBarMedia, media / maxVal);
        setMiniBarPct(miniBarMax,   max   / maxVal);
        setMiniBarPct(miniBarMin,   max > 0 ? (double) min / max : 0);

        // ── grafico torta sessioni ──
        swapChart(pieHolder.getParent(), pieHolder, buildPieChart(report));

        // ── grafico barre ricette ──
        swapChart(barHolder.getParent(), barHolder, buildBarChart(report));

        revalidate(); repaint();
    }

    private void setMiniBarPct(JPanel bar, double pct) {
        bar.putClientProperty("pct", Math.max(0, Math.min(1, pct)));
        bar.repaint();
    }

    private void swapChart(Container parent, JPanel old, JPanel newChart) {
        if (!(parent instanceof JPanel p)) return;
        int idx = -1;
        for (int i = 0; i < p.getComponentCount(); i++) {
            if (p.getComponent(i) == old) { idx = i; break; }
        }
        if (idx < 0) return;
        p.remove(old);
        p.add(newChart, idx);
        // aggiorna il riferimento al campo
        if (old == pieHolder) pieHolder = newChart;
        else                  barHolder = newChart;
        p.revalidate(); p.repaint();
    }

    public YearMonth getSelectedYearMonth() {
        return YearMonth.of((Integer) yearCombo.getSelectedItem(), monthCombo.getSelectedIndex() + 1);
    }
    public int getSelectedYear() { return (Integer) yearCombo.getSelectedItem(); }

    // ── Grafici ──────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private ChartPanel buildPieChart(ReportMensile report) {
        int online   = report.getNumeroSessioniOnline();
        int pratiche = report.getNumeroSessioniPratiche();
        int totale   = online + pratiche;

        DefaultPieDataset ds = new DefaultPieDataset();
        if (totale == 0) {
            ds.setValue("Nessuna sessione", 1);
        } else {
            if (online   > 0) ds.setValue("Online ("   + online   + ")", online);
            if (pratiche > 0) ds.setValue("Pratiche (" + pratiche + ")", pratiche);
        }

        String mese = MESI[monthCombo.getSelectedIndex()];
        JFreeChart chart = ChartFactory.createPieChart(
            "Distribuzione sessioni – " + mese + " " + yearCombo.getSelectedItem(),
            ds, true, true, false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 13));

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 12));
        if (totale > 0) {
            plot.setSectionPaint("Online ("   + online   + ")", COL_ONLINE);
            plot.setSectionPaint("Pratiche (" + pratiche + ")", COL_PRATICHE);
        } else {
            plot.setSectionPaint("Nessuna sessione", new Color(210, 210, 210));
        }

        return freeChartPanel(chart);
    }

    private ChartPanel buildBarChart(ReportMensile report) {
        double media = report.getMediaRicettePerSessione();
        int    max   = report.getMaxRicettePerSessione();
        int    min   = report.getMinRicettePerSessione();

        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        ds.addValue(media, "Media", "Media");
        ds.addValue(max,   "Max",   "Max");
        ds.addValue(min,   "Min",   "Min");

        JFreeChart chart = ChartFactory.createBarChart(
            "Ricette per sessione pratica", "", "N° Ricette",
            ds, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 13));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.setOutlineVisible(false);

        BarRenderer r = (BarRenderer) plot.getRenderer();
        r.setBarPainter(new StandardBarPainter());
        r.setShadowVisible(false);
        r.setSeriesPaint(0, COL_MEDIA);
        r.setSeriesPaint(1, COL_MAX);
        r.setSeriesPaint(2, COL_MIN);
        r.setMaximumBarWidth(0.25);

        // etichetta valore sopra ogni barra
        r.setDefaultItemLabelsVisible(true);
        r.setDefaultItemLabelFont(new Font("Arial", Font.BOLD, 12));
        r.setDefaultItemLabelGenerator(
            new org.jfree.chart.labels.StandardCategoryItemLabelGenerator("{2}", new java.text.DecimalFormat("0.#")));

        ValueAxis axis = plot.getRangeAxis();
        axis.setLowerBound(0);
        axis.setUpperBound(max + 1);

        return freeChartPanel(chart);
    }

    private ChartPanel freeChartPanel(JFreeChart chart) {
        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(Color.WHITE);
        cp.setBorder(BorderFactory.createLineBorder(UiUtil.CARD_BORDER, 1));
        cp.setPreferredSize(new Dimension(0, 0));
        cp.setMinimumDrawWidth(0);
        cp.setMinimumDrawHeight(0);
        cp.setMaximumDrawWidth(4000);
        cp.setMaximumDrawHeight(4000);
        cp.setDomainZoomable(false);
        cp.setRangeZoomable(false);
        return cp;
    }

    private void fireRefresh() {
        if (onRefresh != null) onRefresh.accept(getSelectedYearMonth(), getSelectedYear());
    }
}

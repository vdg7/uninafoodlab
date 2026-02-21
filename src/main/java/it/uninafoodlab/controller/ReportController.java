package it.uninafoodlab.controller;

import java.time.YearMonth;
import java.util.Map;

import it.uninafoodlab.app.AppSession;
import it.uninafoodlab.dao.ReportDAO;
import it.uninafoodlab.dao.ReportDAO.ReportMensile;
import it.uninafoodlab.view.ReportPanel;

/**
 * Controller per la gestione del Report Mensile.
 *
 * Collega il bottone "Aggiorna Report" del {@link ReportPanel}
 * ai metodi del {@link ReportDAO}, iniettando i dati nella vista.
 */
public class ReportController {

    private final ReportPanel reportPanel;

    public ReportController(ReportPanel reportPanel) {
        this.reportPanel = reportPanel;

        // Inietta il callback: quando l'utente preme "Aggiorna Report"
        // il panel chiama questo metodo passando YearMonth e anno selezionati.
        reportPanel.setRefreshAction((yearMonth, anno) -> caricaReport(yearMonth, anno));
    }

    /**
     * Carica e mostra il report per il mese/anno indicati.
     * Legge l'idChef dalla sessione corrente.
     *
     * @param yearMonth mese e anno selezionati dal filtro
     * @param anno      anno selezionato (per il grafico annuale a barre)
     */
    public void caricaReport(YearMonth yearMonth, int anno) {
        int idChef = AppSession.getInstance().getLoggedChef().getIdChef();

        ReportMensile         report       = ReportDAO.getReportMensile(idChef, yearMonth);
        Map<Integer, int[]>   datiAnnuali  = ReportDAO.getSessioniPerMese(idChef, anno);

        reportPanel.updateData(report, datiAnnuali, anno);
    }

    /**
     * Carica il report per il mese corrente appena lo chef accede alla schermata.
     * Può essere chiamato da HomePanel (o da Main) dopo il login.
     */
    public void caricaReportCorrente() {
        YearMonth now = YearMonth.now();
        caricaReport(now, now.getYear());
    }
}
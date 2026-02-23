package it.uninafoodlab.controller;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JOptionPane;

import it.uninafoodlab.app.AppSession;
import it.uninafoodlab.dao.*;
import it.uninafoodlab.model.domain.*;
import it.uninafoodlab.model.enums.TipoModifica;
import it.uninafoodlab.view.DettagliCorsoPanel;
import it.uninafoodlab.view.DettagliCorsoPanel.*;
import it.uninafoodlab.view.HomePanel;
import it.uninafoodlab.view.IngredienteSelectionDialog.IngredienteQuantita;
import it.uninafoodlab.view.RicetteConfigDialog.RicettaData;

/**
 * Controller per la gestione dei corsi.
 *
 * Gestisce:
 *   - Visualizzazione dettagli corso  (mostraDettaglioCorso)
 *   - Modifica data/ora di una sessione + notifica opzionale
 *   - Eliminazione di una sessione    + notifica opzionale
 *   - Aggiunta ricette a sessione pratica
 *
 * Il panel viene ricaricato dal DB dopo ogni operazione.
 */
public class CorsoController {

    private final HomePanel          homePanel;
    private final DettagliCorsoPanel dettaglioPanel;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CorsoController(HomePanel homePanel, DettagliCorsoPanel dettaglioPanel) {
        this.homePanel      = homePanel;
        this.dettaglioPanel = dettaglioPanel;

        // Wiring callback view → controller
        dettaglioPanel.setOnModifica(this::gestisciModifica);
        dettaglioPanel.setOnElimina(this::gestisciElimina);
        dettaglioPanel.setOnAggiungiRicette(this::gestisciAggiungiRicette);
    }

    // ── Caricamento ──────────────────────────────────────────────────────────

    public void mostraDettaglioCorso(Corso corso) {
        try {
            List<SessioneOnline>  online   = SessioneOnlineDAO.getByCorso(corso.getIdCorso());
            List<SessionePratica> pratiche = SessionePraticaDAO.getByCorso(corso.getIdCorso());
            List<Ricetta>         ricette  = RicettaDAO.getByCorso(corso.getIdCorso());

            for (Ricetta r : ricette) {
                r.setIngredienti(RicettaIngredienteDAO.getByRicetta(r.getIdRicetta()));
            }
            int numIscritti = IscrizioneDAO.countByCorso(corso.getIdCorso());

            dettaglioPanel.loadCorso(corso, online, pratiche, ricette, numIscritti);
            homePanel.showPanel("DETTAGLIO");

        } catch (Exception e) {
            System.err.println("Errore caricamento dettagli corso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Ricarica il panel con i dati aggiornati. */
    private void ricarica(Corso corso) {
        mostraDettaglioCorso(corso);
    }

    // ── Modifica sessione ────────────────────────────────────────────────────

    private void gestisciModifica(ModificaSessioneRequest req) {
        boolean ok;

        if ("ONLINE".equals(req.tipo())) {
            SessioneOnline s = SessioneOnlineDAO.getById(req.idSessione());
            if (s == null) return;
            boolean dataChanged = !s.getData().equals(req.nuovaData());
            boolean oraChanged  = !java.util.Objects.equals(s.getOra(), req.nuovaOra());
            s.setData(req.nuovaData());
            s.setOra(req.nuovaOra());
            ok = SessioneOnlineDAO.update(s);
            if (ok) JOptionPane.showMessageDialog(null,
            	    "Sessione modificata con successo.", "Operazione completata",
            	    JOptionPane.INFORMATION_MESSAGE);
            	else JOptionPane.showMessageDialog(null,
            	    "Errore durante la modifica della sessione.", "Errore",
            	    JOptionPane.ERROR_MESSAGE);
            if (ok && req.inviaNotifica()) {
                inserisciNotifica(req.corso(), req.notificaGlobale(),
                    determinaTipo(dataChanged, oraChanged),
                    buildMsgModifica(req.corso().getTitolo(), "online",
                        req.nuovaData(), req.nuovaOra(), dataChanged, oraChanged));
            }
        } else {
            SessionePratica s = SessionePraticaDAO.getById(req.idSessione());
            if (s == null) return;
            boolean dataChanged = !s.getData().equals(req.nuovaData());
            boolean oraChanged  = !java.util.Objects.equals(s.getOra(), req.nuovaOra());
            s.setData(req.nuovaData());
            s.setOra(req.nuovaOra());
            ok = SessionePraticaDAO.update(s);
            if (ok) JOptionPane.showMessageDialog(null,
            	    "Sessione modificata con successo.", "Operazione completata",
            	    JOptionPane.INFORMATION_MESSAGE);
            	else JOptionPane.showMessageDialog(null,
            	    "Errore durante la modifica della sessione.", "Errore",
            	    JOptionPane.ERROR_MESSAGE);
            if (ok && req.inviaNotifica()) {
                inserisciNotifica(req.corso(), req.notificaGlobale(),
                    determinaTipo(dataChanged, oraChanged),
                    buildMsgModifica(req.corso().getTitolo(), "pratica",
                        req.nuovaData(), req.nuovaOra(), dataChanged, oraChanged));
            }
        }

        ricarica(req.corso());
    }

    // ── Elimina sessione ─────────────────────────────────────────────────────

    private void gestisciElimina(EliminaSessioneRequest req) {
        String dataStr = "";

        if ("ONLINE".equals(req.tipo())) {
            SessioneOnline s = SessioneOnlineDAO.getById(req.idSessione());
            if (s != null) dataStr = s.getData().format(FMT);
            SessioneOnlineDAO.delete(req.idSessione());
        } else {
            SessionePratica s = SessionePraticaDAO.getById(req.idSessione());
            if (s != null) dataStr = s.getData().format(FMT);
            SessionePraticaDAO.delete(req.idSessione());
        }

        if (req.inviaNotifica()) {
            String tipo = "ONLINE".equals(req.tipo()) ? "online" : "pratica";
            inserisciNotifica(req.corso(), req.notificaGlobale(),
                TipoModifica.CANCELLAZIONE,
                "La sessione " + tipo + " del " + dataStr
                + " del corso «" + req.corso().getTitolo() + "» è stata cancellata.");
        }
        
        JOptionPane.showMessageDialog(null,
        	    "Sessione eliminata.", "Operazione completata",
        	    JOptionPane.INFORMATION_MESSAGE);
        ricarica(req.corso());
    }

    // ── Aggiungi ricette ─────────────────────────────────────────────────────

    private void gestisciAggiungiRicette(AggiungiRicetteRequest req) {
        for (RicettaData rd : req.ricette()) {
            Ricetta ricetta = new Ricetta(rd.getNome(), req.idSessionePratica());
            int idRicetta = RicettaDAO.insert(ricetta);
            if (idRicetta == -1) continue;

            for (IngredienteQuantita iq : rd.getIngredienti()) {
                RicettaIngrediente ri = new RicettaIngrediente(
                    idRicetta,
                    iq.getIngrediente().getIdIngrediente(),
                    iq.getQuantita()
                );
                RicettaIngredienteDAO.insert(ri);
            }
        }
        JOptionPane.showMessageDialog(null,
        	    req.ricette().size() + " ricetta/e aggiunta/e con successo.", "Operazione completata",
        	    JOptionPane.INFORMATION_MESSAGE);
        ricarica(req.corso());
    }

    // ── Utility notifiche ────────────────────────────────────────────────────

    private void inserisciNotifica(Corso corso, boolean globale,
                                   TipoModifica tipo, String messaggio) {
        int idChef = AppSession.getInstance().getLoggedChef().getIdChef();
        Integer idCorso = globale ? null : corso.getIdCorso();
        String titolo = "[" + tipo.getDisplayName() + "] " + corso.getTitolo()
            + (globale ? " — tutti i corsi" : "");
        NotificaDAO.insert(new Notifica(idChef, idCorso, titolo, messaggio, tipo, globale));
    }

    private TipoModifica determinaTipo(boolean dataChanged, boolean oraChanged) {
        if (dataChanged) return TipoModifica.CAMBIO_DATA;
        if (oraChanged)  return TipoModifica.CAMBIO_ORA;
        return TipoModifica.ALTRO;
    }

    private String buildMsgModifica(String titoloCorso, String tipoSess,
                                    java.time.LocalDate nuovaData, java.time.LocalTime nuovaOra,
                                    boolean dataChanged, boolean oraChanged) {
        StringBuilder sb = new StringBuilder("Sessione ").append(tipoSess)
            .append(" del corso «").append(titoloCorso).append("»: ");
        if (dataChanged)
            sb.append("nuova data ").append(nuovaData.format(FMT)).append(". ");
        if (oraChanged && nuovaOra != null)
            sb.append("nuovo orario ")
              .append(String.format("%02d:%02d", nuovaOra.getHour(), nuovaOra.getMinute()))
              .append(".");
        return sb.toString();
    }
}
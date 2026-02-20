package it.uninafoodlab.controller;

import java.util.List;

import it.uninafoodlab.dao.*;
import it.uninafoodlab.model.domain.*;
import it.uninafoodlab.view.DettagliCorsoPanel;
import it.uninafoodlab.view.HomePanel;

/**
 * Controller per la gestione dei corsi e visualizzazione dettagli.
 */
public class CorsoController {
    
    private final HomePanel homePanel;
    private final DettagliCorsoPanel dettaglioPanel;
    
    public CorsoController(HomePanel homePanel, DettagliCorsoPanel dettaglioPanel) {
        this.homePanel = homePanel;
        this.dettaglioPanel = dettaglioPanel;
    }
    
    /**
     * Carica e mostra i dettagli completi di un corso.
     * 
     * @param corso corso da visualizzare
     */
    public void mostraDettaglioCorso(Corso corso) {
        try {
            // 1. Carica sessioni online
            List<SessioneOnline> sessioniOnline = SessioneOnlineDAO.getByCorso(corso.getIdCorso());
            
            // 2. Carica sessioni pratiche
            List<SessionePratica> sessioniPratiche = SessionePraticaDAO.getByCorso(corso.getIdCorso());
            
            // 3. Carica ricette del corso
            List<Ricetta> ricette = RicettaDAO.getByCorso(corso.getIdCorso());
            
            // 4. Carica ingredienti per ogni ricetta
            for (Ricetta ricetta : ricette) {
                List<RicettaIngrediente> ingredienti = RicettaIngredienteDAO.getByRicetta(ricetta.getIdRicetta());
                ricetta.setIngredienti(ingredienti);
            }
            
            // 5. Passa i dati al panel dettaglio
            dettaglioPanel.loadCorso(corso, sessioniOnline, sessioniPratiche, ricette);
            
            // 6. Mostra il panel
            homePanel.showPanel("DETTAGLIO");
            
        } catch (Exception e) {
            System.err.println("Errore caricamento dettagli corso: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
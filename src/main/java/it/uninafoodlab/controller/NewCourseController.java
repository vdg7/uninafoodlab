package it.uninafoodlab.controller;

import java.util.List;

import it.uninafoodlab.app.AppSession;
import it.uninafoodlab.dao.CorsoDAO;
import it.uninafoodlab.dao.RicettaDAO;
import it.uninafoodlab.dao.SessioneOnlineDAO;
import it.uninafoodlab.dao.SessionePraticaDAO;
import it.uninafoodlab.model.domain.Corso;
import it.uninafoodlab.model.domain.Ricetta;
import it.uninafoodlab.model.domain.SessioneOnline;
import it.uninafoodlab.model.domain.SessionePratica;
import it.uninafoodlab.view.NewCoursePanel.CourseData;
import it.uninafoodlab.view.SessionConfigPanel.RicettaInput;
import it.uninafoodlab.view.SessionConfigPanel.SessionData;
import it.uninafoodlab.view.SessionConfigPanel.SessionType;

/**
 * Controller per la creazione di nuovi corsi.
 */
public class NewCourseController {
    
    private CourseData tempCourseData;
    
    /**
     * Step 1: Salva i dati del corso temporaneamente.
     */
    public void startCourseCreation(CourseData courseData) {
        this.tempCourseData = courseData;
    }
    
    /**
     * Step 2: Conferma e salva il corso con le sessioni dettagliate.
     */
    public boolean confirmCourseWithSessions(List<SessionData> sessionsData) {
        if (tempCourseData == null) {
            return false;
        }
        
        try {
            // 1. Crea il corso
            Corso nuovoCorso = new Corso(
                tempCourseData.getTitolo(),
                tempCourseData.getCategoria(),
                tempCourseData.getDataInizio(),
                tempCourseData.getFrequenza(),
                tempCourseData.getNumSessioni(),
                AppSession.getInstance().getLoggedChef().getIdChef()
            );
            
            int idCorso = CorsoDAO.insert(nuovoCorso);
            
            if (idCorso == -1) {
                return false;
            }
            
            // 2. Crea le sessioni
            for (SessionData sessionData : sessionsData) {
                
                if (sessionData.getType() == SessionType.ONLINE) {
                    // Sessione Online
                    SessioneOnline sessione = new SessioneOnline(
                        sessionData.getData(),
                        120, // Durata default 2 ore
                        sessionData.getLink(),
                        idCorso
                    );
                    SessioneOnlineDAO.insert(sessione);
                    
                } else {
                    // Sessione Pratica
                    SessionePratica sessione = new SessionePratica(
                        sessionData.getData(),
                        180, // Durata default 3 ore
                        sessionData.getLuogo(),
                        idCorso
                    );
                    int idSessionePratica = SessionePraticaDAO.insert(sessione);
                    
                    // Aggiungi ricette se presenti
                    if (sessionData.getRicette() != null && !sessionData.getRicette().isEmpty()) {
                        for (RicettaInput ricettaInput : sessionData.getRicette()) {
                            Ricetta ricetta = new Ricetta(
                                ricettaInput.getNome(),
                                ricettaInput.getIngredienti(),
                                idSessionePratica
                            );
                            RicettaDAO.insert(ricetta);
                        }
                    }
                }
            }
            
            // Reset temp data
            tempCourseData = null;
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Annulla la creazione del corso.
     */
    public void cancelCourseCreation() {
        tempCourseData = null;
    }
}
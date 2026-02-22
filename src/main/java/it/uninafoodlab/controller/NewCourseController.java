package it.uninafoodlab.controller;

import java.math.BigDecimal;
import java.util.List;

import it.uninafoodlab.app.AppSession;
import it.uninafoodlab.dao.*;
import it.uninafoodlab.model.domain.*;
import it.uninafoodlab.view.IngredienteSelectionDialog.IngredienteQuantita;
import it.uninafoodlab.view.NewCoursePanel.CourseData;
import it.uninafoodlab.view.RicetteConfigDialog.RicettaData;
import it.uninafoodlab.view.SessionConfigPanel.SessionData;
import it.uninafoodlab.view.SessionConfigPanel.SessionType;

/**
 * Controller per la creazione di nuovi corsi.
 * AGGIORNATO per gestire ingredienti separati.
 */
public class NewCourseController {
    
    private CourseData tempCourseData;
    
    /**
     * Step 1: Salva i dati del corso temporaneamente.
     * 
     * @param courseData dati del corso
     */
    public void startCourseCreation(CourseData courseData) {
        this.tempCourseData = courseData;
    }
    
    /**
     * Step 2: Conferma e salva il corso con le sessioni dettagliate.
     * 
     * @param sessionsData dati delle sessioni con ricette e ingredienti
     * @return true se salvataggio riuscito, false altrimenti
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
                System.err.println("Errore creazione corso");
                return false;
            }
            
            // 2. Crea le sessioni
            for (SessionData sessionData : sessionsData) {
                
                if (sessionData.getType() == SessionType.ONLINE) {
                    // Sessione Online
                    SessioneOnline sessione = new SessioneOnline(
                        sessionData.getData(),
                        sessionData.getOra(),
                        120, // Durata default 2 ore
                        sessionData.getLink(),
                        idCorso
                    );
                    SessioneOnlineDAO.insert(sessione);
                    
                } else {
                    // Sessione Pratica
                    SessionePratica sessione = new SessionePratica(
                        sessionData.getData(),
                        sessionData.getOra(),
                        180, // Durata default 3 ore
                        sessionData.getLuogo(),
                        idCorso
                    );
                    int idSessionePratica = SessionePraticaDAO.insert(sessione);
                    
                    if (idSessionePratica == -1) {
                        System.err.println("Errore creazione sessione pratica");
                        continue;
                    }
                    
                    // 3. Crea le ricette con ingredienti
                    if (sessionData.getRicette() != null && !sessionData.getRicette().isEmpty()) {
                        for (RicettaData ricettaData : sessionData.getRicette()) {
                            // 3a. Crea la ricetta (solo nome)
                            Ricetta ricetta = new Ricetta(
                                ricettaData.getNome(),
                                idSessionePratica
                            );
                            int idRicetta = RicettaDAO.insert(ricetta);
                            
                            if (idRicetta == -1) {
                                System.err.println("Errore creazione ricetta: " + ricettaData.getNome());
                                continue;
                            }
                            
                            // 3b. Associa gli ingredienti alla ricetta
                            for (IngredienteQuantita iq : ricettaData.getIngredienti()) {
                                RicettaIngrediente ri = new RicettaIngrediente(
                                    idRicetta,
                                    iq.getIngrediente().getIdIngrediente(),
                                    iq.getQuantita()
                                );
                                
                                boolean success = RicettaIngredienteDAO.insert(ri);
                                if (!success) {
                                    System.err.println("Errore aggiunta ingrediente: " + 
                                                     iq.getIngrediente().getNome() + 
                                                     " alla ricetta " + ricettaData.getNome());
                                }
                            }
                        }
                    }
                }
            }
            
            // Reset temp data
            tempCourseData = null;
            return true;
            
        } catch (Exception e) {
            System.err.println("Errore durante creazione corso:");
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
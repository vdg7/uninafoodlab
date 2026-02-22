package it.uninafoodlab.controller;

import java.util.List;

import it.uninafoodlab.app.AppSession;
import it.uninafoodlab.dao.ChefDAO;
import it.uninafoodlab.dao.CorsoDAO;
import it.uninafoodlab.model.domain.Chef;
import it.uninafoodlab.model.domain.Corso;
import it.uninafoodlab.view.HomePanel;
import it.uninafoodlab.view.LoginPanel;
import it.uninafoodlab.view.MainFrame;

/**
 * Controller per gestire l'autenticazione degli chef.
 */
public class AuthController {

    private final MainFrame frame;
    private final HomePanel homePanel;
    private final LoginPanel loginPanel;

    public AuthController(MainFrame frame, HomePanel homePanel, LoginPanel loginPanel) {
        this.frame = frame;
        this.homePanel = homePanel;
        this.loginPanel = loginPanel;
    }

    /**
     * Gestisce il login dello chef.
     */
    public void login(String email, String password) {
        //Chiamata diretta a metodo static (NO istanza DAO)
        Chef chef = ChefDAO.login(email, password);

        if (chef == null) {
            loginPanel.showInvalidCredentials();
            loginPanel.clearFields();
            return;
        }

        // Salva chef in sessione
        AppSession.getInstance().setLoggedChef(chef);

        // Carica corsi dello chef
        List<Corso> corsi = CorsoDAO.getByChef(chef.getIdChef());
        
        // Aggiorna HomePanel
        homePanel.showChef(chef, corsi);
        
        // Passa a fullscreen e mostra HOME
        frame.goFullscreen();
        frame.showView("HOME");
    }
    
    
    /**
     * Esegue il logout: pulisce la sessione, torna alla login in finestra normale.
     */
    public void logout() {
        AppSession.getInstance().clear();
        // Torna alla dimensione originale (non fullscreen)
        frame.goLoginSize();
        frame.showView("LOGIN");
        loginPanel.clearFields();
    }
    
    /**
     * Ricarica la dashboard (dopo creazione nuovo corso).
     */
    public void refreshDashboard() {
        Chef chef = AppSession.getInstance().getLoggedChef();
        if (chef != null) {
            List<Corso> corsi = CorsoDAO.getByChef(chef.getIdChef());
            homePanel.showChef(chef, corsi);
        }
    }
}

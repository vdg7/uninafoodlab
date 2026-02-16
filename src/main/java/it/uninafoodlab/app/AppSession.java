package it.uninafoodlab.app;

import it.uninafoodlab.model.domain.Chef;

/**
 * Singleton per gestire la sessione dell'utente loggato.
 */
public class AppSession {
    
    private static AppSession instance;
    private Chef loggedChef;
    
    private AppSession() {}
    
    public static AppSession getInstance() {
        if (instance == null) {
            instance = new AppSession();
        }
        return instance;
    }
    
    public Chef getLoggedChef() {
        return loggedChef;
    }
    
    public void setLoggedChef(Chef chef) {
        this.loggedChef = chef;
    }
    
    public void clear() {
        this.loggedChef = null;
    }
}
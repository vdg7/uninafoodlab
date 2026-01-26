package it.uninafoodlab.app;

import it.uninafoodlab.domain.Chef;

public class AppSession {
    private static final AppSession instance = new AppSession();
    private Chef loggedChef;

    private AppSession() {}

    public static AppSession getInstance() {
        return instance;
    }

    public Chef getLoggedChef() {
        return loggedChef;
    }

    public void setLoggedChef(Chef chef) {
        this.loggedChef = chef;
    }

    public boolean isLogged() {
        return loggedChef != null;
    }

    public void logout() {
        loggedChef = null;
    }
}


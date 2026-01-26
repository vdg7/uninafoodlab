package it.uninafoodlab.controller;

import it.uninafoodlab.dao.SessioneOnlineDao;
import it.uninafoodlab.dao.SessionePraticaDao;
import it.uninafoodlab.domain.Corso;
import it.uninafoodlab.gui.DashboardPanel;

public class DashboardController {

    private final DashboardPanel dashboard;
    private final SessioneOnlineDao onlineDao = new SessioneOnlineDao();
    private final SessionePraticaDao praticaDao = new SessionePraticaDao();

    public DashboardController(DashboardPanel dashboard) {
        this.dashboard = dashboard;
    }

    public void openCourseDetails(Corso corso) {
        int online = onlineDao.countByCorso(corso.getIdCorso());
        int pratiche = praticaDao.countByCorso(corso.getIdCorso());

        dashboard.getDetailsCard().setCourseDetails(
                corso.getTitolo(),
                corso.getTema(),
                corso.getDataInizio().toString(),
                corso.getFrequenzaSettimanale(),
                online,
                pratiche
        );

        dashboard.showCourseDetails();
    }
}


package it.uninafoodlab.controller;

import it.uninafoodlab.dao.SessioneOnlineDao;
import it.uninafoodlab.dao.SessionePraticaDao;
import it.uninafoodlab.domain.Corso;
import it.uninafoodlab.gui.CourseDetailsPanel;

public class CourseDetailsController {

    private final CourseDetailsPanel view;
    private final SessioneOnlineDao onlineDao;
    private final SessionePraticaDao praticaDao;

    public CourseDetailsController(
            CourseDetailsPanel view,
            SessioneOnlineDao onlineDao,
            SessionePraticaDao praticaDao
    ) {
        this.view = view;
        this.onlineDao = onlineDao;
        this.praticaDao = praticaDao;
    }

    public void showCourse(Corso corso) {
        int online = onlineDao.countByCorso(corso.getIdCorso());
        int pratiche = praticaDao.countByCorso(corso.getIdCorso());

        view.setCourseDetails(
                corso.getTitolo(),
                corso.getTema(),
                corso.getDataInizio().toString(),
                corso.getFrequenzaSettimanale(),
                online,
                pratiche
        );
    }
}


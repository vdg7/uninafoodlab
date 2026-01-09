package it.uninafoodlab.controller;

import it.uninafoodlab.dao.SessioneOnlineDao;
import it.uninafoodlab.dao.SessionePraticaDao;
import it.uninafoodlab.domain.SessioneOnline;
import it.uninafoodlab.domain.SessionePratica;

import java.util.List;

public class SessioneController {

    private final SessioneOnlineDao onlineDao;
    private final SessionePraticaDao praticaDao;

    public SessioneController(SessioneOnlineDao onlineDao,
                              SessionePraticaDao praticaDao) {
        this.onlineDao = onlineDao;
        this.praticaDao = praticaDao;
    }

    public List<SessioneOnline> getSessioniOnline(int idCorso) {
        return onlineDao.findByCorso(idCorso);
    }

    public List<SessionePratica> getSessioniPratiche(int idCorso) {
        return praticaDao.findByCorso(idCorso);
    }
}

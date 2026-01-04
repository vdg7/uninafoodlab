package it.uninafoodlab.controller;

import it.uninafoodlab.dao.SessioneDao;
import it.uninafoodlab.domain.SessioneOnline;
import it.uninafoodlab.domain.SessionePratica;

import java.time.LocalDate;

public class SessioneController {

    private final SessioneDao sessioneDao;

    public SessioneController(SessioneDao sessioneDao) {
        this.sessioneDao = sessioneDao;
    }

    public void generaSessioni(int idCorso, LocalDate dataInizio, int frequenzaSettimanale) {

        LocalDate data = dataInizio;

        for (int i = 0; i < frequenzaSettimanale; i++) {

            if (i % 2 == 0) {
                sessioneDao.insertOnline(new SessioneOnline(data, idCorso));
            } else {
                sessioneDao.insertPratica(new SessionePratica(data, idCorso));
            }

            data = data.plusDays(7);
        }
    }
}

package it.uninafoodlab.controller;

import java.util.List;

import it.uninafoodlab.dao.RicettaDao;
import it.uninafoodlab.domain.Ricetta;

public class RicettaController {

    private RicettaDao ricettaDao = new RicettaDao();

    public List<Ricetta> getRicetteCorso(int idCorso) {
        return ricettaDao.findByCorso(idCorso);
    }
}

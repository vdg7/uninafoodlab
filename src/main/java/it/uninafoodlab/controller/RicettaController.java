package it.uninafoodlab.controller;

import it.uninafoodlab.dao.RicettaDao;
import it.uninafoodlab.domain.Ricetta;

import java.util.List;

public class RicettaController {

    private final RicettaDao ricettaDao;

    public RicettaController(RicettaDao ricettaDao) {
        this.ricettaDao = ricettaDao;
    }

    public List<Ricetta> getRicetteSessione(int idSessione) {
        return ricettaDao.findBySessionePratica(idSessione);
    }

    public void associaRicetta(int idRicetta, int idSessione) {
        ricettaDao.addRicettaToSessione(idRicetta, idSessione);
    }
}

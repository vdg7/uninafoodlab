package it.uninafoodlab.controller;

import it.uninafoodlab.dao.CorsoDao;
import it.uninafoodlab.domain.Corso;

public class CorsoController {
	
	private final CorsoDao corsoDao;
	
	public CorsoController(CorsoDao corsoDao) {
		this.corsoDao = corsoDao;
	}
	
	public Corso creaCorso(Corso corso) {
		if (corso.getTitolo() == null || corso.getTitolo().isBlank()) return null;
        if (corso.getCategoria() == null || corso.getCategoria().isBlank()) return null;
        if (corso.getFrequenzaSettimanale() <= 0) return null;

        int idGenerato = corsoDao.insert(corso);

        if (idGenerato > 0) {
            corso.setIdCorso(idGenerato);
            return corso;
        }

        return null;
	}
}

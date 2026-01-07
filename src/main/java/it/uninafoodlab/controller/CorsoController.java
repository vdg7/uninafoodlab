package it.uninafoodlab.controller;

import it.uninafoodlab.dao.CorsoDao;
import it.uninafoodlab.domain.Corso;
import java.util.List;

public class CorsoController {
	
	private final CorsoDao corsoDao;
	
	public CorsoController(CorsoDao corsoDao) {
		this.corsoDao = corsoDao;
	}
	
	public Corso creaCorso(Corso corso) {
		if (corso.getTitolo() == null || corso.getTitolo().isBlank()) return null;
        if (corso.getTema() == null || corso.getTema().isBlank()) return null;
        if (corso.getFrequenzaSettimanale() <= 0) return null;

        int idGenerato = corsoDao.insert(corso);

        if (idGenerato > 0) {
            corso.setIdCorso(idGenerato);
            return corso;
        }
       
        return null;
	}
	
	public List<Corso> getCorsiChef(int idChef) {
        return corsoDao.findByChef(idChef);
    }

    public List<Corso> getCorsiChefPerCategoria(int idChef, String tema) {
        if (tema == null || tema.isBlank()) {
            return corsoDao.findByChef(idChef);
        }
        return corsoDao.findByChefAndTema(idChef, tema);
    }
}

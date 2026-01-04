package it.uninafoodlab.service;
	
import it.uninafoodlab.dao.ChefDao;
import it.uninafoodlab.domain.Chef;

public class AuthService {

	private final ChefDao chefDao;
	
	public AuthService(ChefDao chefDao) {
		this.chefDao = 	chefDao;
	}
	
	//Return Chef se le cred sono corrette, null altrimenti
	public Chef login (String email, String password ) {
		if (email == null || email.isBlank() || password == null || password.isBlank()) {
			return null;
		}
		
		return chefDao.findByEmailAndPassword(email.trim(), password);
	}
}

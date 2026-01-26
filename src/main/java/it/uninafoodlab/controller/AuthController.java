package it.uninafoodlab.controller;
	
import java.util.List;

import javax.swing.JOptionPane;

import it.uninafoodlab.app.AppSession;
import it.uninafoodlab.dao.ChefDao;
import it.uninafoodlab.dao.CorsoDao;
import it.uninafoodlab.domain.Chef;
import it.uninafoodlab.domain.Corso;
import it.uninafoodlab.gui.HomePanel;
import it.uninafoodlab.gui.MainFrame;


public class AuthController {

    private final ChefDao chefDao;
    private final CorsoDao corsoDao;
    private final MainFrame frame;
    private final HomePanel homePanel;

    public AuthController(ChefDao chefDao, CorsoDao corsoDao,
                          MainFrame frame, HomePanel homePanel) {
        this.chefDao = chefDao;
        this.corsoDao = corsoDao;
        this.frame = frame;
        this.homePanel = homePanel;
    }

    public void login(String email, String password) {
        Chef chef = chefDao.findByEmailAndPassword(email, password);

        if (chef == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Email o password non corrette",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        AppSession.getInstance().setLoggedChef(chef);

        List<Corso> corsi = corsoDao.findByChef(chef.getIdChef());
        homePanel.showChef(chef, corsi);
        frame.goFullscreen();

        frame.showView("HOME");
    }
}


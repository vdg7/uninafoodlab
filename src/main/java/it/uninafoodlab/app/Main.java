package it.uninafoodlab.app;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import it.uninafoodlab.controller.AuthController;
import it.uninafoodlab.controller.CorsoController;
import it.uninafoodlab.controller.NewCourseController;
import it.uninafoodlab.view.LoginPanel;
import it.uninafoodlab.view.HomePanel;
import it.uninafoodlab.view.MainFrame;

/**
 * Entry point dell'applicazione UninaFoodLab.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
            //Look and Feel FlatLaf
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception e) {
                System.err.println("Impossibile impostare FlatLaf, uso default");
            }

            //Frame principale
            MainFrame frame = new MainFrame();

            //Panels
            LoginPanel loginPanel = new LoginPanel();
            HomePanel homePanel = new HomePanel();

            //Controller
            AuthController authController = new AuthController(frame, homePanel, loginPanel);
            CorsoController corsoController = new CorsoController(homePanel, homePanel.getDettagliCorsoPanel());
            NewCourseController newCourseController = new NewCourseController();
            
         // Collega eventi
            homePanel.getNewCoursePanel().setCreateAction(data -> {
                newCourseController.startCourseCreation(data);
                homePanel.getSessionConfigPanel().setupSessions(
                    data.getDataInizio(), 
                    data.getFrequenza(), 
                    data.getNumSessioni()
                );
                homePanel.showPanel("SESSIONCONFIG");
            });
            
            homePanel.getDashboardPanel().setDettagliAction(corsoController::showCorsoDetails);
            
            homePanel.getSessionConfigPanel().setConfermaAction(sessions -> {
                boolean success = newCourseController.confirmCourseWithSessions(sessions);
                if (success) {
                    homePanel.getNewCoursePanel().clearFields();
                    homePanel.showPanel("DASHBOARD");
                    authController.refreshDashboard();
                    JOptionPane.showMessageDialog(frame, "Corso creato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Errore creazione corso", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            });

            homePanel.getSessionConfigPanel().setAnnullaAction(() -> {
                newCourseController.cancelCourseCreation();
                homePanel.showPanel("NEWCOURSES");
            });

            //evento login
            loginPanel.setLoginAction(authController::login);

            frame.addView("LOGIN", loginPanel);
            frame.addView("HOME", homePanel);

            frame.showView("LOGIN");
            frame.setVisible(true);
        });
    }
}
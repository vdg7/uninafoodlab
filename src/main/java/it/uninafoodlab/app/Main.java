package it.uninafoodlab.app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import it.uninafoodlab.controller.AuthController;
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

            //evento login
            loginPanel.setLoginAction(authController::login);

            frame.addView("LOGIN", loginPanel);
            frame.addView("HOME", homePanel);

            frame.showView("LOGIN");
            frame.setVisible(true);
        });
    }
}
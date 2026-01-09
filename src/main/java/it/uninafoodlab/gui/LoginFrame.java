package it.uninafoodlab.gui;

import it.uninafoodlab.controller.AuthController;
import it.uninafoodlab.dao.ChefDao;
import it.uninafoodlab.domain.Chef;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final AuthController authController;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginFrame() {
        this.authController = new AuthController(new ChefDao());
        initUI();
    }

    private void initUI() {
        setTitle("UninaFoodLab - Login");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        panel.add(loginButton);

        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        panel.add(messageLabel);

        add(panel);

        loginButton.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        Chef chef = authController.login(email, password);

        if (chef == null) {
            messageLabel.setText("Credenziali errate");
        } else {
            dispose(); // chiude login
            new DashboardFrame(chef).setVisible(true);
        }
    }
}

package it.uninafoodlab.view;

import java.awt.*;
import java.util.function.BiConsumer;

import javax.swing.*;

public class LoginPanel extends BasePanel{
	
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public LoginPanel() {
    	setLayout(new BorderLayout());
    	setBackground(UiUtil.UNINA_BLUE);
        initHeader();
        initForm();
    }
    
    public void setLoginAction(BiConsumer<String, String> action) {
        loginButton.addActionListener(e ->
                action.accept(
                        emailField.getText(),
                        new String(passwordField.getPassword())
                )
        );
    }
    
    
    private void initHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiUtil.UNINA_BLUE);
        
        JLabel logoUnina = new JLabel(new ImageIcon(
            new ImageIcon(getClass().getResource("/UninaLogoW.png"))
                .getImage()
                .getScaledInstance(75, 75, Image.SCALE_SMOOTH)
        ));
        header.add(logoUnina, BorderLayout.WEST);
        
        JLabel title = new JLabel("UninaFoodLab", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 56));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);
        
        header.setBorder(BorderFactory.createEmptyBorder(40, 20, 0, 20));

        add(header, BorderLayout.NORTH);
    }
    
    private void initForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UiUtil.UNINA_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);

        gbc.gridy = 0;
        form.add(createLabel("Email"), gbc);

        gbc.gridy = 1;
        emailField = new JTextField(22);
        form.add(emailField, gbc);

        gbc.gridy = 2;
        form.add(createLabel("Password"), gbc);

        gbc.gridy = 3;
        passwordField = new JPasswordField(22);
        form.add(passwordField, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBackground(Color.WHITE);
        loginButton.setForeground(UiUtil.UNINA_BLUE);

        form.add(loginButton, gbc);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UiUtil.UNINA_BLUE);
        wrapper.add(form);

        add(wrapper, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }
    
    public void showInvalidCredentials() {
    	showError("Email o password non corrette", "Errore di Login");
    }
	
    public void clearFields() {
        emailField.setText("");
        passwordField.setText("");
        emailField.requestFocus();
    }
    
    
	@Override
	public Dimension getPreferredSize() {
	    return new Dimension(500, 450); 
	}

	@Override
	public Dimension getMaximumSize() {
	    return new Dimension(600, 500);
	}

	@Override
	public Dimension getMinimumSize() {
	    return new Dimension(400, 400);
	}



}

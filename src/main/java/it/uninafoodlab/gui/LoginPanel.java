package it.uninafoodlab.gui;

import java.awt.*;
import java.util.function.BiConsumer;

import javax.swing.*;

public class LoginPanel extends JPanel implements MessageView{
	
	private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public LoginPanel() {
    	setLayout(new BorderLayout());
    	setBackground(Util.UNINA_BLUE);
        initHeader();
        initForm();
    }
    
    public void setLoginAction(BiConsumer<String, String> action) {
    	loginButton.addActionListener(e -> {
            action.accept(
                usernameField.getText(),
                new String(passwordField.getPassword())
            );
        });
    }
    
    
    private void initHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Util.UNINA_BLUE);
        
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
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Util.UNINA_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.insets = new Insets(0, 0, 5, 0);

        // USERNAME LABEL
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(Color.WHITE);
        formPanel.add(usernameLabel, gbc);

        // USERNAME FIELD
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        usernameField = new JTextField(22);
        formPanel.add(usernameField, gbc);

        // PASSWORD LABEL
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);
        formPanel.add(passwordLabel, gbc);

        // PASSWORD FIELD
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        passwordField = new JPasswordField(22);
        formPanel.add(passwordField, gbc);

        // LOGIN BUTTON
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setForeground(Util.UNINA_BLUE);
        loginButton.setBackground(Color.WHITE);

        formPanel.add(loginButton, gbc);

        // wrapper che centra il blocco
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBorder(
        	    BorderFactory.createEmptyBorder(0, 0, 50, 0)
        	);
        centerWrapper.setBackground(Util.UNINA_BLUE);
        centerWrapper.add(formPanel);

        add(centerWrapper, BorderLayout.CENTER);
    }




	@Override
	public void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
	}


	@Override
	public void showSuccess(String message) {
		 JOptionPane.showMessageDialog(this, message, "Successo", JOptionPane.INFORMATION_MESSAGE);
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

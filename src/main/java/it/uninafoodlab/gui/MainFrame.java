package it.uninafoodlab.gui;

import java.awt.Rectangle;
import java.awt.GraphicsEnvironment;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsDevice;

import javax.swing.ImageIcon;
import java.awt.Taskbar;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;

import it.uninafoodlab.controller.Navigator;
import it.uninafoodlab.controller.View;

public class MainFrame extends JFrame implements Navigator {
	
    private CardLayout cardLayout;
    private JPanel container;
    
	public MainFrame(){
		
		setTitle("UninaFoodLab");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initLayout();
		
		if (Taskbar.isTaskbarSupported()) {
		    try {
		        Taskbar taskbar = Taskbar.getTaskbar();
		        Image image = new ImageIcon(MainFrame.class.getResource("/LogoUnina.png")).getImage();
		        taskbar.setIconImage(image);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		
	}

    private void initLayout() {
        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);
        add(container);
    }

    public void addView(View view, JPanel panel) {
    	panel.setName(view.name());
        container.add(panel, view.name());
    }

    @Override
    public void showView(View view) {
    	if(view == View.HOME) {
    		goFullscreen();
    	} else if (view == View.LOGIN) {
    		goLoginSize();
    	}
    	
    	cardLayout.show(container, view.name());
    }
    
    private void goLoginSize() {
        setUndecorated(false);
        setResizable(false);
        setSize(600, 400);
        setLocationRelativeTo(null);
    }
    
    private void goFullscreen() {
		Rectangle screen = getScreenSize();
		int width = 1728;
		int height = 972;
		if(width > screen.width) width = screen.width;
		if(height > screen.height) height = screen.height;
		setSize(width, height);
		setResizable(true);
		setLocationRelativeTo(null);
        setVisible(true);
    }
    
	private Rectangle getScreenSize()
	{
	       GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	       GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
	       return defaultScreen.getDefaultConfiguration().getBounds();
	}
	
	public JPanel getPanel(View view) {
	    for (Component comp : container.getComponents()) {
	        if (comp.getName() != null && comp.getName().equals(view.name())) {
	            return (JPanel) comp;
	        }
	    }
	    return null;
	}

}

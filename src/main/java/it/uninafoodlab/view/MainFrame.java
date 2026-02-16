package it.uninafoodlab.view;

import java.awt.Rectangle;
import java.awt.GraphicsEnvironment;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GraphicsDevice;

import javax.swing.ImageIcon;
import java.awt.Taskbar;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame{
	
    private CardLayout layout;
    private JPanel container;
    
	public MainFrame(){
		
		setTitle("UninaFoodLab");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initLayout();
		goLoginSize();
		
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
        layout = new CardLayout();
        container = new JPanel(layout);
        add(container);
    }

    public void addView(String name, JPanel panel) {
        container.add(panel, name);
    }

    public void showView(String name) {
        layout.show(container, name);
    }
    
    private void goLoginSize() {
        setUndecorated(false);
        setResizable(false);
        setSize(600, 400);
        setLocationRelativeTo(null);
    }
    
    public void goFullscreen() {
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

}

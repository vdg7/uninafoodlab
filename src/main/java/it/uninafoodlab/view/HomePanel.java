package it.uninafoodlab.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.util.List;

import javax.swing.*;

import it.uninafoodlab.model.domain.Chef;
import it.uninafoodlab.model.domain.Corso;

public class HomePanel extends BasePanel {

	private CardLayout contentLayout;
	private JPanel contentPanel;
	private JLabel nameLabel;
	private JLabel emailLabel;
	private DashboardPanel dashboardPanel;
	private NewCoursePanel newCoursePanel;
	private SessionConfigPanel sessionConfigPanel;
	private DettagliCorsoPanel dettagliCorsoPanel;
//	private ReportPanel reportPanel;
	
    public HomePanel() {
    	setLayout(new BorderLayout());
    	setBackground(UiUtil.UNINA_GREY);
    	initSideBar();
    	initContentPanel();
    }
    
    public void initSideBar() {
    	JPanel sideBar = new JPanel();
    	sideBar.setLayout(new BorderLayout());
    	sideBar.setBackground(UiUtil.UNINA_BLUE.darker());
    	sideBar.setPreferredSize(new Dimension(345, 0));
    	
    	sideBar.add(createProfilePanel(), BorderLayout.NORTH);
        sideBar.add(createNavPanel(), BorderLayout.CENTER);

        add(sideBar, BorderLayout.WEST);
    }
    
    private JPanel createProfilePanel() {
    	JPanel profile = new JPanel();
        profile.setLayout(new BoxLayout(profile, BoxLayout.Y_AXIS));
        profile.setBackground(UiUtil.UNINA_BLUE.darker());
        profile.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));

        JLabel avatar = new JLabel(new ImageIcon(
                new ImageIcon(getClass().getResource("/avatar.png"))
                    .getImage()
                    .getScaledInstance(250, 250, Image.SCALE_SMOOTH)
            ));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);

        nameLabel = new JLabel("Chef");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailLabel = new JLabel("email@unina.it");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        profile.add(avatar);
        profile.add(Box.createVerticalStrut(10));
        profile.add(nameLabel);
        profile.add(Box.createVerticalStrut(4));
        profile.add(emailLabel);

        return profile;
    }
    
    private JPanel createNavPanel() {
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(UiUtil.UNINA_BLUE.darker());
        nav.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        nav.add(Box.createVerticalStrut(20));
        nav.add(createNavButton("Dashboard", "DASHBOARD"));
        nav.add(Box.createVerticalStrut(20));
        nav.add(createNavButton("Crea nuovo corso", "NEWCOURSES"));
        nav.add(Box.createVerticalStrut(20));
        nav.add(createNavButton("Report Mensile", "REPORT"));
        nav.add(Box.createVerticalStrut(20));

        return nav;
    }

    
    public void initContentPanel() {
    	contentLayout = new CardLayout();
    	contentPanel = new JPanel(contentLayout);
    	contentPanel.setBackground(UiUtil.UNINA_GREY);
    	
    	dashboardPanel = new DashboardPanel();
    	newCoursePanel = new NewCoursePanel();
    	sessionConfigPanel = new SessionConfigPanel();
    	dettagliCorsoPanel = new DettagliCorsoPanel();
//    	reportPanel = new ReportPanel();
//    	
    	contentPanel.add(dashboardPanel, "DASHBOARD");
        contentPanel.add(newCoursePanel, "NEWCOURSES");
        contentPanel.add(sessionConfigPanel, "SESSIONCONFIG");
        contentPanel.add(dettagliCorsoPanel, "DETTAGLI");
//        contentPanel.add(reportPanel, "REPORT");

        contentLayout.show(contentPanel, "DASHBOARD");
        add(contentPanel, BorderLayout.CENTER);
    }
    
    public DashboardPanel getDashboardPanel() {
        return dashboardPanel;
    }
    
    public NewCoursePanel getNewCoursePanel() {
        return newCoursePanel;
    }
    
    public SessionConfigPanel getSessionConfigPanel() {
        return sessionConfigPanel;
    }
    
    public DettagliCorsoPanel getDettagliCorsoPanel() {
        return dettagliCorsoPanel;
    }
//    
//    public ReportPanel getReportPanel() {
//        return reportPanel;
//    }
    
    public void showPanel(String panelName) {
        contentLayout.show(contentPanel, panelName);
    }
    
    
    private JButton createNavButton(String text, String view) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setFocusPainted(false);
        btn.setBackground(UiUtil.UNINA_BLUE.darker());
        btn.setForeground(Color.WHITE);

        btn.addActionListener(e -> contentLayout.show(contentPanel, view));
        return btn;
    }
    
    /* ---------------- API PER IL CONTROLLER ---------------- */

    public void showChef(Chef chef, List<Corso> corsi) {
        nameLabel.setText(chef.getNome());
        emailLabel.setText(chef.getEmail());
        dashboardPanel.setCorsi(corsi);
    }

    
    
}

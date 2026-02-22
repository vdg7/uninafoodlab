package it.uninafoodlab.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import it.uninafoodlab.model.domain.Chef;
import it.uninafoodlab.model.domain.Corso;

public class HomePanel extends BasePanel {

    private CardLayout contentLayout;
    private JPanel     contentPanel;

    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel specLabel;

    private JButton logoutBtn;
    
    private DashboardPanel    dashboardPanel;
    private NewCoursePanel    newCoursePanel;
    private SessionConfigPanel sessionConfigPanel;
    private DettagliCorsoPanel dettagliCorsoPanel;
    private ReportPanel        reportPanel;

    // Callback iniettato da Main / AuthController
    private Runnable onLogoutAction;
    
    
    public HomePanel() {
        setLayout(new BorderLayout());
        setBackground(UiUtil.UNINA_GREY);
        initSideBar();
        initContentPanel();
    }

    // ── Sidebar ─────────────────────────────────────────────────────────────

    public void initSideBar() {
        JPanel sideBar = new JPanel(new BorderLayout());
        sideBar.setBackground(UiUtil.UNINA_BLUE.darker());
        sideBar.setPreferredSize(new Dimension(345, 0));

        sideBar.add(createProfilePanel(), BorderLayout.NORTH);
        sideBar.add(createNavPanel(),     BorderLayout.CENTER);
        



        add(sideBar, BorderLayout.WEST);
    }

    private JPanel createProfilePanel() {        
    	
    	
        
        JPanel profile = new JPanel();
        profile.setLayout(new BoxLayout(profile, BoxLayout.Y_AXIS));
        profile.setBackground(UiUtil.UNINA_BLUE.darker());
        profile.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UiUtil.UNINA_BLUE.darker());
        
        logoutBtn = new JButton("logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
        	int risposta = JOptionPane.showConfirmDialog(
                HomePanel.this,
                "Sei sicuro di voler effettuare il logout?",
                "Conferma Logout",
                JOptionPane.YES_NO_OPTION,
               JOptionPane.QUESTION_MESSAGE
            );
            if (risposta != JOptionPane.YES_OPTION) return;
            if (onLogoutAction != null) onLogoutAction.run();
        });
        
        topPanel.add(logoutBtn, BorderLayout.WEST);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(UiUtil.UNINA_BLUE.darker());

        JLabel avatar = new JLabel(new ImageIcon(
            new ImageIcon(getClass().getResource("/avatar.png"))
                .getImage()
                .getScaledInstance(250, 250, Image.SCALE_SMOOTH)
        ));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameLabel = new JLabel("Chef");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailLabel = new JLabel("email@unina.it");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        specLabel = new JLabel("Numero specializzazioni: 0");
        specLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        specLabel.setForeground(Color.WHITE);
        specLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(avatar);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(nameLabel);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(emailLabel);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(specLabel);
        
        profile.add(topPanel, BorderLayout.NORTH);
        profile.add(centerPanel, BorderLayout.CENTER);

        return profile;
    }

    private JPanel createNavPanel() {
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(UiUtil.UNINA_BLUE.darker());
        nav.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        nav.add(Box.createVerticalStrut(20));
        nav.add(createNavButton("Dashboard",        "DASHBOARD"));
        nav.add(Box.createVerticalStrut(20));
        nav.add(createNavButton("Crea nuovo corso", "NEWCOURSES"));
        nav.add(Box.createVerticalStrut(20));
        nav.add(createNavButton("Report Mensile",   "REPORT"));
        nav.add(Box.createVerticalStrut(20));

        return nav;
    }

    private JButton createNavButton(String text, String viewName) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setFocusPainted(false);
        btn.setBackground(UiUtil.UNINA_BLUE.darker());
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.addActionListener(e -> contentLayout.show(contentPanel, viewName));
        return btn;
    }

    // ── Content panel ────────────────────────────────────────────────────────

    public void initContentPanel() {
        contentLayout = new CardLayout();
        contentPanel  = new JPanel(contentLayout);
        contentPanel.setBackground(UiUtil.UNINA_GREY);

        dashboardPanel     = new DashboardPanel();
        newCoursePanel     = new NewCoursePanel();
        sessionConfigPanel = new SessionConfigPanel();
        dettagliCorsoPanel = new DettagliCorsoPanel();
        reportPanel        = new ReportPanel();

        contentPanel.add(dashboardPanel,     "DASHBOARD");
        contentPanel.add(newCoursePanel,     "NEWCOURSES");
        contentPanel.add(sessionConfigPanel, "SESSIONCONFIG");
        contentPanel.add(dettagliCorsoPanel, "DETTAGLIO");
        contentPanel.add(reportPanel,        "REPORT");

        contentLayout.show(contentPanel, "DASHBOARD");
        add(contentPanel, BorderLayout.CENTER);
    }

    // ── Getters panel ────────────────────────────────────────────────────────

    public DashboardPanel     getDashboardPanel()     { return dashboardPanel; }
    public NewCoursePanel     getNewCoursePanel()     { return newCoursePanel; }
    public SessionConfigPanel getSessionConfigPanel() { return sessionConfigPanel; }
    public DettagliCorsoPanel getDettagliCorsoPanel() { return dettagliCorsoPanel; }
    public ReportPanel        getReportPanel()        { return reportPanel; }

    // ── Navigazione ──────────────────────────────────────────────────────────

    public void showPanel(String panelName) {
        contentLayout.show(contentPanel, panelName);
    }

    // ── API per il controller ────────────────────────────────────────────────

    public void setLogoutAction(Runnable action) {
        this.onLogoutAction = action;
    }
    
    public void showChef(Chef chef, List<Corso> corsi) {
        nameLabel.setText(chef.getNome());
        emailLabel.setText(chef.getEmail());
        specLabel.setText("Numero specializzazioni: " + chef.getNumeroSpecializzazioni());
        dashboardPanel.setCorsi(corsi);
    }
}

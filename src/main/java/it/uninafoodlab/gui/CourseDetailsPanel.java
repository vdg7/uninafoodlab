package it.uninafoodlab.gui;

import java.awt.*;
import javax.swing.*;

import it.uninafoodlab.controller.CourseDetailsController;
import it.uninafoodlab.dao.SessioneOnlineDao;
import it.uninafoodlab.dao.SessionePraticaDao;
import it.uninafoodlab.domain.Corso;

public class CourseDetailsPanel extends BasePanel {

    private DashboardPanel dashboard;
    private JLabel titleLabel;
    private JLabel temaLabel;
    private JLabel startDateLabel;
    private JLabel frequencyLabel;
    private JLabel onlineSessionLabel;
    private JLabel practicalSessionLabel;

    public CourseDetailsPanel(DashboardPanel dashboard) {

        this.dashboard = dashboard;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UiUtil.UNINA_GREY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(UiUtil.UNINA_BLUE.darker());

        temaLabel = new JLabel();
        temaLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        startDateLabel = new JLabel();
        startDateLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        frequencyLabel = new JLabel();
        frequencyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        
        onlineSessionLabel = new JLabel();
        onlineSessionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        
        practicalSessionLabel = new JLabel();
        practicalSessionLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton backBtn = new JButton("← Torna indietro");
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> dashboard.showOverview());

        add(backBtn);
        add(Box.createVerticalStrut(20));
        add(titleLabel);
        add(Box.createVerticalStrut(10));
        add(temaLabel);
        add(Box.createVerticalStrut(5));
        add(startDateLabel);
        add(Box.createVerticalStrut(5));
        add(frequencyLabel);
        add(Box.createVerticalStrut(5));
        add(onlineSessionLabel);
        add(Box.createVerticalStrut(5));
        add(practicalSessionLabel);
    }

    public void setCourseDetails(
            String titolo,
            String tema,
            String dataInizio,
            int frequenza,
            int sessioniOnline,
            int sessioniPratiche
    ) {
        titleLabel.setText("Titolo: " + titolo);
        temaLabel.setText("Categoria: " + tema);
        startDateLabel.setText("Data inizio: " + dataInizio);
        frequencyLabel.setText("Frequenza settimanale: " + frequenza);
        onlineSessionLabel.setText("Sessioni online: " + sessioniOnline);
        practicalSessionLabel.setText("Sessioni pratiche: " + sessioniPratiche);
    }
}


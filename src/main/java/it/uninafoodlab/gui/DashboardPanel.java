package it.uninafoodlab.gui;

import java.awt.*;
import javax.swing.*;

import it.uninafoodlab.controller.DashboardController;
import it.uninafoodlab.domain.Corso;

public class DashboardPanel extends JPanel {

    private CardLayout centerLayout;
    private JPanel centerPanel;

    private CoursesOverviewCard coursesCard;
    private CourseDetailsPanel detailsCard;

    public DashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Util.UNINA_GREY);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        add(createHeader(), BorderLayout.NORTH);

        // Pannello centrale con card layout
        centerLayout = new CardLayout();
        centerPanel = new JPanel(centerLayout);
        centerPanel.setBackground(Util.UNINA_GREY);

        // Card lista corsi
        DashboardController controller = new DashboardController(this);
        coursesCard = new CoursesOverviewCard(controller);
        centerPanel.add(coursesCard, "OVERVIEW");

        // Card dettagli corso
        detailsCard = new CourseDetailsPanel(this);
        centerPanel.add(detailsCard, "DETAILS");

        add(centerPanel, BorderLayout.CENTER);

        centerLayout.show(centerPanel, "OVERVIEW");
    }

    public CoursesOverviewCard getCoursesCard() {
        return coursesCard;
    }

    public void showCourseDetails() {
        centerLayout.show(centerPanel, "DETAILS");
    }

    public CourseDetailsPanel getDetailsCard() {
        return detailsCard;
    }
    
    public void showOverview() {
        centerLayout.show(centerPanel, "OVERVIEW");
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Util.UNINA_GREY);

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Util.UNINA_BLUE.darker());

        JLabel subtitle = new JLabel("Gestisci i tuoi corsi");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(Color.DARK_GRAY);

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        return header;
    }
}





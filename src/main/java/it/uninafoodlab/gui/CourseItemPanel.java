package it.uninafoodlab.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import it.uninafoodlab.controller.DashboardController;
import it.uninafoodlab.domain.Corso;

public class CourseItemPanel extends JPanel {
	
    private Color normalBg = new Color(248, 249, 251);
    private Color hoverBg = new Color(230, 235, 245);

    public CourseItemPanel(Corso corso, Runnable onDetails) {
        setPreferredSize(new Dimension(280, 160));
        setBackground(new Color(248, 249, 251));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setLayout(new BorderLayout(8, 8));

        JLabel titolo = new JLabel(corso.getTitolo());
        titolo.setFont(new Font("Arial", Font.BOLD, 16));
        titolo.setForeground(Util.UNINA_BLUE);

        JLabel tema = new JLabel("Categoria: " + corso.getTema());
        tema.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel data = new JLabel("Inizio: " + corso.getDataInizio());
        data.setFont(new Font("Arial", Font.PLAIN, 12));
        data.setForeground(Color.DARK_GRAY);

        JButton dettagli = new JButton("Dettagli");
        dettagli.setFocusPainted(false);
        dettagli.setBackground(Util.UNINA_BLUE);
        dettagli.setForeground(Color.WHITE);
        
        dettagli.addActionListener(e -> onDetails.run());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottom.setBackground(getBackground());
        bottom.add(dettagli);

        add(titolo, BorderLayout.NORTH);
        add(tema, BorderLayout.CENTER);
        add(data, BorderLayout.SOUTH);
        add(bottom, BorderLayout.PAGE_END);
        
     // --- HOVER EFFECT ---
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBg);
                bottom.setBackground(hoverBg);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalBg);
                bottom.setBackground(normalBg);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                repaint();
            }
        });
    }
}

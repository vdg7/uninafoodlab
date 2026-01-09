package it.uninafoodlab.gui;

import it.uninafoodlab.domain.Corso;

import javax.swing.*;

public class CorsoFrame extends JFrame {

    private final Corso corso;

    public CorsoFrame(Corso corso) {
        this.corso = corso;
        initUI();
    }

    private void initUI() {
        setTitle("Corso - " + corso.getTitolo());
        setSize(500, 300);
        setLocationRelativeTo(null);

        add(new JLabel(
                "Corso: " + corso.getTitolo() +
                " | Tema: " + corso.getTema(),
                SwingConstants.CENTER));
    }
}

package it.uninafoodlab.view;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.*;

import it.uninafoodlab.model.domain.Notifica;

/**
 * Panel per visualizzare le notifiche dello chef loggato.
 *
 * Layout:
 *   NORTH  – titolo + bottone "Aggiorna"
 *   CENTER – lista scrollabile di card notifica
 *
 * Il caricamento avviene tramite un Supplier iniettato da Main,
 * così il panel non dipende direttamente dal DAO.
 */
public class NotificaPanel extends BasePanel {

    private JPanel           listaContainer;
    private Supplier<List<Notifica>> loader;

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ────────────────────────────────────────────────────────────────────────

    public NotificaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UiUtil.UNINA_GREY);
        setPadding(20);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildLista(),  BorderLayout.CENTER);
    }

    // ── Costruzione UI ────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(UiUtil.UNINA_GREY);

        JLabel title = new JLabel("Notifiche");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(UiUtil.UNINA_BLUE);

        JButton btnAggiorna = new JButton("↻  Aggiorna");
        btnAggiorna.setFont(new Font("Arial", Font.BOLD, 13));
        btnAggiorna.setBackground(UiUtil.UNINA_BLUE);
        btnAggiorna.setForeground(Color.WHITE);
        btnAggiorna.setFocusPainted(false);
        btnAggiorna.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAggiorna.addActionListener(e -> refresh());

        h.add(title,       BorderLayout.WEST);
        h.add(btnAggiorna, BorderLayout.EAST);
        return h;
    }

    private JScrollPane buildLista() {
        listaContainer = new JPanel();
        listaContainer.setLayout(new BoxLayout(listaContainer, BoxLayout.Y_AXIS));
        listaContainer.setBackground(UiUtil.UNINA_GREY);

        JScrollPane scroll = new JScrollPane(listaContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    // ── API pubblica ──────────────────────────────────────────────────────────

    /** Iniettato da Main: () -> NotificaDAO.getByChef(idChef) */
    public void setLoader(Supplier<List<Notifica>> loader) {
        this.loader = loader;
    }

    /** Ricarica le notifiche e aggiorna la lista. */
    public void refresh() {
        listaContainer.removeAll();

        if (loader == null) {
            listaContainer.add(emptyLabel("Loader non configurato."));
            listaContainer.revalidate();
            listaContainer.repaint();
            return;
        }

        List<Notifica> notifiche = loader.get();

        if (notifiche.isEmpty()) {
            listaContainer.add(emptyLabel("Nessuna notifica."));
        } else {
            for (Notifica n : notifiche) {
                listaContainer.add(buildCard(n));
                listaContainer.add(Box.createVerticalStrut(10));
            }
        }

        listaContainer.revalidate();
        listaContainer.repaint();
    }

    // ── Card singola notifica ─────────────────────────────────────────────────

    private JPanel buildCard(Notifica n) {
        // Colore bordo in base al tipo
        Color accent = switch (n.getTipoModifica()) {
            case CAMBIO_DATA    -> new Color(41,  128, 185);
            case CAMBIO_ORA     -> new Color(142,  68, 173);
            case CANCELLAZIONE  -> new Color(180,  40,  40);
            default             -> new Color(100, 100, 100);
        };

        JPanel card = new JPanel(new BorderLayout(10, 6));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent, 2),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Riga superiore: titolo + badge portata
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(Color.WHITE);

        JLabel titoloLbl = new JLabel(n.getTitolo());
        titoloLbl.setFont(new Font("Arial", Font.BOLD, 14));
        titoloLbl.setForeground(accent);

        JLabel badge = new JLabel(n.isGlobale() ? " Tutti i corsi " : " Corso specifico ");
        badge.setFont(new Font("Arial", Font.BOLD, 11));
        badge.setForeground(Color.WHITE);
        badge.setBackground(n.isGlobale() ? new Color(180, 40, 40) : new Color(39, 174, 96));
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));

        topRow.add(titoloLbl, BorderLayout.CENTER);
        topRow.add(badge,     BorderLayout.EAST);

        // Corpo
        JLabel msgLbl = new JLabel("<html><body style='width:500px'>" + n.getMessaggio() + "</body></html>");
        msgLbl.setFont(new Font("Arial", Font.PLAIN, 13));

        // Data
        String dataStr = n.getDataCreazione() != null ? n.getDataCreazione().format(FMT) : "–";
        JLabel dataLbl = new JLabel(dataStr);
        dataLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        dataLbl.setForeground(Color.GRAY);
        dataLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        card.add(topRow,  BorderLayout.NORTH);
        card.add(msgLbl,  BorderLayout.CENTER);
        card.add(dataLbl, BorderLayout.SOUTH);
        return card;
    }

    private JLabel emptyLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.ITALIC, 15));
        l.setForeground(Color.GRAY);
        l.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        return l;
    }
}
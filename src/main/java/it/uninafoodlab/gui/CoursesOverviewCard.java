package it.uninafoodlab.gui;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

import it.uninafoodlab.controller.DashboardController;
import it.uninafoodlab.domain.Corso;

public class CoursesOverviewCard extends JPanel {

    private JComboBox<String> categoriaFilter;
    private JPanel coursesContainer;
    private final DashboardController controller;
    private JTextField searchField;


    private List<Corso> allCorsi = new ArrayList<>();

    public CoursesOverviewCard(DashboardController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        initTop();
        initCenter();
    }

    /* ---------------- TOP: filtro categoria ---------------- */

    private void initTop() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);


        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setBackground(Color.WHITE);

        JLabel categoriaLabel = new JLabel("Categoria:");
        categoriaLabel.setFont(new Font("Arial", Font.BOLD, 14));

        categoriaFilter = new JComboBox<>();
        categoriaFilter.addItem("Tutte");
        categoriaFilter.addActionListener(e -> applyFilters());

        left.add(categoriaLabel);
        left.add(categoriaFilter);


        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        center.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Cerca:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));

        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
        });

        center.add(searchLabel);
        center.add(searchField);

        top.add(left, BorderLayout.WEST);
        top.add(center, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
    }


    /* ---------------- CENTER: scroll + card ---------------- */

    private void initCenter() {
        coursesContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        coursesContainer.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(
                coursesContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    /* ---------------- API usata dal controller ---------------- */

    public void setCourses(List<Corso> corsi) {
        this.allCorsi = new ArrayList<>(corsi);
        updateCategoriaFilter(corsi);
        applyFilters();
    }

    /* ---------------- Logica filtro ---------------- */

    private void updateCategoriaFilter(List<Corso> corsi) {
        Set<String> categorie = new LinkedHashSet<>();

        categoriaFilter.removeAllItems();
        categoriaFilter.addItem("Tutte");

        for (Corso c : corsi) {
            categorie.add(c.getTema());
        }

        categorie.forEach(categoriaFilter::addItem);
    }

    private void applyFilters() {
        String selectedCategoria = (String) categoriaFilter.getSelectedItem();
        String searchText = searchField.getText().trim().toLowerCase();

        List<Corso> filtered = allCorsi.stream()
            .filter(corso -> {
                boolean matchCategoria =
                        selectedCategoria == null ||
                        "Tutte".equals(selectedCategoria) ||
                        selectedCategoria.equals(corso.getTema());

                boolean matchNome =
                        searchText.isEmpty() ||
                        corso.getTitolo().toLowerCase().contains(searchText);

                return matchCategoria && matchNome;
            })
            .toList();

        refreshCards(filtered);
    }


    /* ---------------- Rendering card ---------------- */

    private void refreshCards(List<Corso> corsi) {
        coursesContainer.removeAll();

        for (Corso corso : corsi) {
        	coursesContainer.add(
                    new CourseItemPanel(
                        corso,
                        () -> controller.openCourseDetails(corso)
                    )
                );
        }

        coursesContainer.revalidate();
        coursesContainer.repaint();
    }
}



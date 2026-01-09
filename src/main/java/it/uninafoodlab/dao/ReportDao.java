package it.uninafoodlab.dao;

import it.uninafoodlab.db.DbConnectionFactory;
import it.uninafoodlab.domain.CorsoDettaglio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDao {

    public List<CorsoDettaglio> getCorsiDettaglio() {
        String sql = "SELECT * FROM v_corsodettaglio";

        List<CorsoDettaglio> risultati = new ArrayList<>();

        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CorsoDettaglio cd = new CorsoDettaglio();
                cd.setTitolo(rs.getString("Titolo"));
                cd.setTema(rs.getString("Tema"));
                cd.setNomeChef(rs.getString("NomeChef"));
                cd.setNumeroSessioni(rs.getInt("NumeroSessioni"));
                risultati.add(cd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return risultati;
    }
}

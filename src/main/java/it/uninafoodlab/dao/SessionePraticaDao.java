package it.uninafoodlab.dao;

import it.uninafoodlab.db.DbConnectionFactory;
import it.uninafoodlab.domain.SessionePratica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionePraticaDao {

    public List<SessionePratica> findByCorso(int idCorso) {
        String sql = """
            SELECT ID_Sessione, Data, Durata, Laboratorio
            FROM SessionePratica
            WHERE ID_Corso = ?
        """;

        List<SessionePratica> sessioni = new ArrayList<>();

        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCorso);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SessionePratica s = new SessionePratica();
                s.setIdSessione(rs.getInt("ID_Sessione"));
                s.setData(rs.getDate("Data").toLocalDate());
                s.setDurata(rs.getInt("Durata"));
                s.setLaboratorio(rs.getString("Laboratorio"));
                s.setIdCorso(idCorso);
                sessioni.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sessioni;
    }
}

package it.uninafoodlab.dao;

import it.uninafoodlab.db.DbConnectionFactory;
import it.uninafoodlab.domain.SessioneOnline;
import it.uninafoodlab.domain.SessionePratica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SessioneDao {

    public void insertOnline(SessioneOnline s) {
        String sql = """
            INSERT INTO SessioneOnline (Data, ID_Corso)
            VALUES (?, ?)
        """;

        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(s.getData()));
            ps.setInt(2, s.getIdCorso());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPratica(SessionePratica s) {
        String sql = """
            INSERT INTO SessionePratica (Data, ID_Corso)
            VALUES (?, ?)
        """;

        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(s.getData()));
            ps.setInt(2, s.getIdCorso());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

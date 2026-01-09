package it.uninafoodlab.dao;

import it.uninafoodlab.db.DbConnectionFactory;
import it.uninafoodlab.domain.SessioneOnline;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessioneOnlineDao {

    public List<SessioneOnline> findByCorso(int idCorso) {
        String sql = """
            SELECT ID_Sessione, Data, Durata, Link
            FROM SessioneOnline
            WHERE ID_Corso = ?
        """;

        List<SessioneOnline> sessioni = new ArrayList<>();

        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCorso);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SessioneOnline s = new SessioneOnline();
                s.setIdSessione(rs.getInt("ID_Sessione"));
                s.setData(rs.getDate("Data").toLocalDate());
                s.setDurata(rs.getInt("Durata"));
                s.setLink(rs.getString("Link"));
                s.setIdCorso(idCorso);
                sessioni.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sessioni;
    }
}

package it.uninafoodlab.dao;

import it.uninafoodlab.db.DbConnectionFactory;
import it.uninafoodlab.domain.SessionePratica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionePraticaDao {

	
	private static final String COUNT_BY_CORSO =
	        "SELECT COUNT(*) FROM SessionePratica WHERE ID_corso = ?";

	public int countByCorso(int idCorso) {
        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(COUNT_BY_CORSO)) {

            ps.setInt(1, idCorso);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore conteggio sessioni pratiche", e);
        }

        return 0;
    }    
	  
    public List<SessionePratica> findByCorso(int idCorso) {
        String sql = """
            SELECT ID_SessionePratica, Data, Durata, Luogo
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
                s.setIdSessione(rs.getInt("ID_SessionePratica"));
                s.setData(rs.getDate("Data").toLocalDate());
                s.setDurata(rs.getInt("Durata"));
                s.setLuogo(rs.getString("Luogo"));
                s.setIdCorso(idCorso);
                sessioni.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sessioni;
    }
}

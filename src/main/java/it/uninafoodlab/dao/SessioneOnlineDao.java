package it.uninafoodlab.dao;

import it.uninafoodlab.db.DbConnectionFactory;
import it.uninafoodlab.domain.SessioneOnline;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessioneOnlineDao {
	
	private static final String COUNT_BY_CORSO =
	        "SELECT COUNT(*) FROM SessioneOnline WHERE ID_Corso = ?";

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
	            throw new RuntimeException("Errore conteggio sessioni online", e);
	        }

	        return 0;
	    }

    public List<SessioneOnline> findByCorso(int idCorso) {
        String sql = """
            SELECT ID_SessioneOnline, Data, Durata, Link
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
                s.setIdSessione(rs.getInt("ID_SessioneOnline"));
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
    
    
    public int insert(SessioneOnline sessione) {
        String sql = """
            INSERT INTO SessioneOnline (Data, Durata, Link, ID_Corso)
            VALUES (?, ?, ?, ?)
        """;
        
        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setDate(1, java.sql.Date.valueOf(sessione.getData()));
            ps.setInt(2, sessione.getDurata());
            ps.setString(3, sessione.getLink());
            ps.setInt(4, sessione.getIdCorso());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1;
    }
    
    
    public boolean update(SessioneOnline sessione) {
        String sql = """
            UPDATE SessioneOnline 
            SET Data = ?, Durata = ?, Link = ?
            WHERE ID_SessioneOnline = ?
        """;
        
        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, java.sql.Date.valueOf(sessione.getData()));
            ps.setInt(2, sessione.getDurata());
            ps.setString(3, sessione.getLink());
            ps.setInt(4, sessione.getIdSessione());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    
    public boolean delete(int idSessioneOnline) {
        String sql = "DELETE FROM SessioneOnline WHERE ID_SessioneOnline = ?";
        
        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idSessioneOnline);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}

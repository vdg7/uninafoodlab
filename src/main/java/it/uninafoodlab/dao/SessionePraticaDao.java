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
    
    
    public int insert(SessionePratica sessione) {
        String sql = """
            INSERT INTO SessionePratica (Data, Durata, Luogo, ID_Corso)
            VALUES (?, ?, ?, ?)
        """;
        
        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setDate(1, java.sql.Date.valueOf(sessione.getData()));
            ps.setInt(2, sessione.getDurata());
            ps.setString(3, sessione.getLuogo());
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
    
    
    public boolean update(SessionePratica sessione) {
        String sql = """
            UPDATE SessionePratica 
            SET Data = ?, Durata = ?, Luogo = ?
            WHERE ID_SessionePratica = ?
        """;
        
        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, java.sql.Date.valueOf(sessione.getData()));
            ps.setInt(2, sessione.getDurata());
            ps.setString(3, sessione.getLuogo());
            ps.setInt(4, sessione.getIdSessione());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    
    public boolean delete(int idSessionePratica) {
        String sql = "DELETE FROM SessionePratica WHERE ID_SessionePratica = ?";
        
        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idSessionePratica);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}

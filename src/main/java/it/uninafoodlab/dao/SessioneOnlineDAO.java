package it.uninafoodlab.dao;

import it.uninafoodlab.model.domain.SessioneOnline;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione delle Sessioni Online nel database.
 */
public class SessioneOnlineDAO {
    
    /**
     * Inserisce una nuova sessione online nel database.
     * 
     * @param sessione oggetto SessioneOnline da inserire
     * @return ID della sessione inserita, -1 in caso di errore
     */
    public static int insert(SessioneOnline sessione) {
        String sql = "INSERT INTO SessioneOnline (Data, Ora, Durata, Link, ID_Corso) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setDate(1, Date.valueOf(sessione.getData()));
            ps.setTime(2, sessione.getOra() != null ? Time.valueOf(sessione.getOra()) : null);
            ps.setInt(3, sessione.getDurata());
            ps.setString(4, sessione.getLink());
            ps.setInt(5, sessione.getIdCorso());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Errore inserimento sessione online: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Aggiorna una sessione online esistente.
     * 
     * @param sessione oggetto SessioneOnline con i dati aggiornati
     * @return true se aggiornamento riuscito, false altrimenti
     */
    public static boolean update(SessioneOnline sessione) {
        String sql = "UPDATE SessioneOnline SET Data = ?, Ora = ?, Durata = ?, Link = ? WHERE ID_SessioneOnline = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, Date.valueOf(sessione.getData()));
            ps.setTime(2, sessione.getOra() != null ? Time.valueOf(sessione.getOra()) : null);
            ps.setInt(3, sessione.getDurata());
            ps.setString(4, sessione.getLink());
            ps.setInt(5, sessione.getIdSessioneOnline());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore aggiornamento sessione online: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Ottiene tutte le sessioni online di un corso.
     * 
     * @param idCorso ID del corso
     * @return lista di sessioni online
     */
    public static List<SessioneOnline> getByCorso(int idCorso) {
        List<SessioneOnline> sessioni = new ArrayList<>();
        String sql = "SELECT * FROM SessioneOnline WHERE ID_Corso = ? ORDER BY Data, Ora";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCorso);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                sessioni.add(createSessioneFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero sessioni online: " + e.getMessage());
            e.printStackTrace();
        }
        
        return sessioni;
    }
    
    /**
     * Ottiene una sessione online tramite ID.
     * 
     * @param idSessione ID della sessione
     * @return oggetto SessioneOnline se trovato, null altrimenti
     */
    public static SessioneOnline getById(int idSessione) {
        String sql = "SELECT * FROM SessioneOnline WHERE ID_SessioneOnline = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idSessione);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createSessioneFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero sessione online: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Elimina una sessione online.
     * 
     * @param idSessione ID della sessione da eliminare
     * @return true se eliminazione riuscita, false altrimenti
     */
    public static boolean delete(int idSessione) {
        String sql = "DELETE FROM SessioneOnline WHERE ID_SessioneOnline = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idSessione);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore eliminazione sessione online: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Crea un oggetto SessioneOnline da un ResultSet.
     */
    private static SessioneOnline createSessioneFromResultSet(ResultSet rs) throws SQLException {
    	Time oraSQL = rs.getTime("Ora");
        return new SessioneOnline(
            rs.getInt("ID_SessioneOnline"),
            rs.getDate("Data").toLocalDate(),
            oraSQL != null ? oraSQL.toLocalTime() : null,
            rs.getInt("Durata"),
            rs.getString("Link"),
            rs.getInt("ID_Corso")
        );
    }
}

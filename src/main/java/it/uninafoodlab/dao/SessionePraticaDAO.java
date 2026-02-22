package it.uninafoodlab.dao;

import it.uninafoodlab.model.domain.SessionePratica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione delle Sessioni Pratiche nel database.
 */
public class SessionePraticaDAO {
    
    /**
     * Inserisce una nuova sessione pratica nel database.
     * 
     * @param sessione oggetto SessionePratica da inserire
     * @return ID della sessione inserita, -1 in caso di errore
     */
    public static int insert(SessionePratica sessione) {
        String sql = "INSERT INTO SessionePratica (Data, Ora, Durata, Luogo, ID_Corso) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setDate(1, Date.valueOf(sessione.getData()));
            ps.setTime(2, sessione.getOra() != null ? Time.valueOf(sessione.getOra()) : null);
            ps.setInt(3, sessione.getDurata());
            ps.setString(4, sessione.getLuogo());
            ps.setInt(5, sessione.getIdCorso());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Errore inserimento sessione pratica: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Aggiorna una sessione pratica esistente.
     * 
     * @param sessione oggetto SessionePratica con i dati aggiornati
     * @return true se aggiornamento riuscito, false altrimenti
     */
    public static boolean update(SessionePratica sessione) {
        String sql = "UPDATE SessionePratica SET Data = ?, Ora = ?, Durata = ?, Luogo = ? WHERE ID_SessionePratica = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, Date.valueOf(sessione.getData()));
            ps.setTime(2, sessione.getOra() != null ? Time.valueOf(sessione.getOra()) : null);
            ps.setInt(3, sessione.getDurata());
            ps.setString(4, sessione.getLuogo());
            ps.setInt(5, sessione.getIdSessionePratica());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore aggiornamento sessione pratica: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Ottiene tutte le sessioni pratiche di un corso.
     * 
     * @param idCorso ID del corso
     * @return lista di sessioni pratiche
     */
    public static List<SessionePratica> getByCorso(int idCorso) {
        List<SessionePratica> sessioni = new ArrayList<>();
        String sql = "SELECT * FROM SessionePratica WHERE ID_Corso = ? ORDER BY Data, Ora";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCorso);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                sessioni.add(createSessioneFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero sessioni pratiche: " + e.getMessage());
            e.printStackTrace();
        }
        
        return sessioni;
    }
    
    /**
     * Ottiene una sessione pratica tramite ID.
     * 
     * @param idSessione ID della sessione
     * @return oggetto SessionePratica se trovato, null altrimenti
     */
    public static SessionePratica getById(int idSessione) {
        String sql = "SELECT * FROM SessionePratica WHERE ID_SessionePratica = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idSessione);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createSessioneFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero sessione pratica: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Elimina una sessione pratica.
     * 
     * @param idSessione ID della sessione da eliminare
     * @return true se eliminazione riuscita, false altrimenti
     */
    public static boolean delete(int idSessione) {
        String sql = "DELETE FROM SessionePratica WHERE ID_SessionePratica = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idSessione);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore eliminazione sessione pratica: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Crea un oggetto SessionePratica da un ResultSet.
     */
    private static SessionePratica createSessioneFromResultSet(ResultSet rs) throws SQLException {
    	 Time oraSQL = rs.getTime("Ora");
        return new SessionePratica(
            rs.getInt("ID_SessionePratica"),
            rs.getDate("Data").toLocalDate(),
            oraSQL != null ? oraSQL.toLocalTime() : null,
            rs.getInt("Durata"),
            rs.getString("Luogo"),
            rs.getInt("ID_Corso")
        );
    }
}
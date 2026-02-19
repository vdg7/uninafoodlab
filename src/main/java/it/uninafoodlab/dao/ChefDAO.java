package it.uninafoodlab.dao;

import it.uninafoodlab.model.domain.Chef;
import java.sql.*;

/**
 * Data Access Object per la gestione dei Chef nel database PostgreSQL.
 * Fornisce metodi per autenticazione e recupero informazioni chef.
 */
public class ChefDAO {
    
    /**
     * Autentica uno chef tramite email e password.
     * 
     * @param email email dello chef
     * @param password password dello chef
     * @return oggetto Chef se le credenziali sono corrette, null altrimenti
     */
    public static Chef login(String email, String password) {
        String sql = "SELECT * FROM Chef WHERE Email = ? AND Password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createChefFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore login chef: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Ottiene un chef tramite ID.
     * 
     * @param idChef ID dello chef da recuperare
     * @return oggetto Chef se trovato, null altrimenti
     */
    public static Chef getById(int idChef) {
        String sql = "SELECT * FROM Chef WHERE ID_Chef = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idChef);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createChefFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero chef: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Verifica se un'email esiste già nel database.
     * 
     * @param email email da verificare
     * @return true se l'email esiste, false altrimenti
     */
    public static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Chef WHERE Email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Errore verifica email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Crea un oggetto Chef da un ResultSet.
     * 
     * @param rs ResultSet contenente i dati dello chef
     * @return oggetto Chef creato
     * @throws SQLException se si verifica un errore nel recupero dei dati
     */
    private static Chef createChefFromResultSet(ResultSet rs) throws SQLException {
        return new Chef(
            rs.getInt("ID_Chef"),
            rs.getString("Nome"),
            rs.getString("Email"),
            rs.getString("Password"),
            rs.getInt("AnniEsperienza"),
            rs.getInt("NumeroSpecializzazioni")
        );
    }
}
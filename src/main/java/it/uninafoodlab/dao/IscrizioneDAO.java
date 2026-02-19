package it.uninafoodlab.dao;

import it.uninafoodlab.model.domain.Iscrizione;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione delle Iscrizioni nel database PostgreSQL.
 * Gestisce l'iscrizione degli studenti ai corsi con data di iscrizione.
 */
public class IscrizioneDAO {
    
    /**
     * Inserisce una nuova iscrizione nel database.
     * La data di iscrizione viene impostata automaticamente dal DB (DEFAULT CURRENT_TIMESTAMP).
     * 
     * @param iscrizione oggetto Iscrizione da inserire
     * @return true se inserimento riuscito, false altrimenti
     */
    public static boolean insert(Iscrizione iscrizione) {
        String sql = "INSERT INTO Iscrizione (ID_Studente, ID_Corso) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, iscrizione.getIdStudente());
            ps.setInt(2, iscrizione.getIdCorso());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore inserimento iscrizione: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Ottiene tutte le iscrizioni di uno studente.
     * 
     * @param idStudente ID dello studente
     * @return lista di iscrizioni dello studente
     */
    public static List<Iscrizione> getByStudente(int idStudente) {
        List<Iscrizione> iscrizioni = new ArrayList<>();
        String sql = "SELECT * FROM Iscrizione WHERE ID_Studente = ? ORDER BY DataIscrizione DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idStudente);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                iscrizioni.add(createIscrizioneFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero iscrizioni studente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return iscrizioni;
    }
    
    /**
     * Ottiene tutte le iscrizioni a un corso.
     * 
     * @param idCorso ID del corso
     * @return lista di iscrizioni al corso
     */
    public static List<Iscrizione> getByCorso(int idCorso) {
        List<Iscrizione> iscrizioni = new ArrayList<>();
        String sql = "SELECT * FROM Iscrizione WHERE ID_Corso = ? ORDER BY DataIscrizione";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCorso);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                iscrizioni.add(createIscrizioneFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero iscrizioni corso: " + e.getMessage());
            e.printStackTrace();
        }
        
        return iscrizioni;
    }
    
    /**
     * Ottiene una specifica iscrizione.
     * 
     * @param idStudente ID dello studente
     * @param idCorso ID del corso
     * @return oggetto Iscrizione se trovato, null altrimenti
     */
    public static Iscrizione get(int idStudente, int idCorso) {
        String sql = "SELECT * FROM Iscrizione WHERE ID_Studente = ? AND ID_Corso = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idStudente);
            ps.setInt(2, idCorso);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createIscrizioneFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero iscrizione: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Verifica se uno studente è iscritto a un corso.
     * 
     * @param idStudente ID dello studente
     * @param idCorso ID del corso
     * @return true se lo studente è iscritto, false altrimenti
     */
    public static boolean isIscritto(int idStudente, int idCorso) {
        String sql = "SELECT COUNT(*) FROM Iscrizione WHERE ID_Studente = ? AND ID_Corso = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idStudente);
            ps.setInt(2, idCorso);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Errore verifica iscrizione: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina un'iscrizione dal database.
     * 
     * @param idStudente ID dello studente
     * @param idCorso ID del corso
     * @return true se eliminazione riuscita, false altrimenti
     */
    public static boolean delete(int idStudente, int idCorso) {
        String sql = "DELETE FROM Iscrizione WHERE ID_Studente = ? AND ID_Corso = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idStudente);
            ps.setInt(2, idCorso);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore eliminazione iscrizione: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Conta il numero di iscritti a un corso.
     * 
     * @param idCorso ID del corso
     * @return numero di studenti iscritti
     */
    public static int countByCorso(int idCorso) {
        String sql = "SELECT COUNT(*) FROM Iscrizione WHERE ID_Corso = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCorso);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore conteggio iscrizioni: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Crea un oggetto Iscrizione da un ResultSet.
     * 
     * @param rs ResultSet contenente i dati dell'iscrizione
     * @return oggetto Iscrizione creato
     * @throws SQLException se si verifica un errore nel recupero dei dati
     */
    private static Iscrizione createIscrizioneFromResultSet(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("DataIscrizione");
        LocalDateTime dataIscrizione = timestamp != null ? timestamp.toLocalDateTime() : null;
        
        return new Iscrizione(
            rs.getInt("ID_Studente"),
            rs.getInt("ID_Corso"),
            dataIscrizione
        );
    }
}
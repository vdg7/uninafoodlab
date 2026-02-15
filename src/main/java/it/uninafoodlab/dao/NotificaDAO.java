package it.uninafoodlab.dao;

import it.uninafoodlab.model.domain.Notifica;
import it.uninafoodlab.model.enums.TipoModifica;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione delle Notifiche nel database.
 */
public class NotificaDAO {
    
    /**
     * Inserisce una nuova notifica nel database.
     * 
     * @param notifica oggetto Notifica da inserire
     * @return ID della notifica inserita, -1 in caso di errore
     */
    public static int insert(Notifica notifica) {
        String sql = "INSERT INTO Notifica (ID_Chef, ID_Corso, Titolo, Messaggio, TipoModifica, IsGlobale) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, notifica.getIdChef());
            
            // ID_Corso può essere null se notifica globale
            if (notifica.getIdCorso() != null) {
                ps.setInt(2, notifica.getIdCorso());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            
            ps.setString(3, notifica.getTitolo());
            ps.setString(4, notifica.getMessaggio());
            ps.setString(5, notifica.getTipoModifica().name());
            ps.setBoolean(6, notifica.isGlobale());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Errore inserimento notifica: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Ottiene tutte le notifiche di uno chef.
     * 
     * @param idChef ID dello chef
     * @return lista di notifiche ordinate per data (più recenti prime)
     */
    public static List<Notifica> getByChef(int idChef) {
        List<Notifica> notifiche = new ArrayList<>();
        String sql = "SELECT * FROM Notifica WHERE ID_Chef = ? ORDER BY DataCreazione DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idChef);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                notifiche.add(createNotificaFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero notifiche chef: " + e.getMessage());
            e.printStackTrace();
        }
        
        return notifiche;
    }
    
    /**
     * Ottiene tutte le notifiche relative a un corso specifico.
     * 
     * @param idCorso ID del corso
     * @return lista di notifiche per il corso
     */
    public static List<Notifica> getByCorso(int idCorso) {
        List<Notifica> notifiche = new ArrayList<>();
        String sql = "SELECT * FROM Notifica WHERE ID_Corso = ? ORDER BY DataCreazione DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCorso);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                notifiche.add(createNotificaFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero notifiche corso: " + e.getMessage());
            e.printStackTrace();
        }
        
        return notifiche;
    }
    
    /**
     * Ottiene tutte le notifiche globali di uno chef.
     * 
     * @param idChef ID dello chef
     * @return lista di notifiche globali
     */
    public static List<Notifica> getGlobaliByChef(int idChef) {
        List<Notifica> notifiche = new ArrayList<>();
        String sql = "SELECT * FROM Notifica WHERE ID_Chef = ? AND IsGlobale = TRUE ORDER BY DataCreazione DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idChef);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                notifiche.add(createNotificaFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero notifiche globali: " + e.getMessage());
            e.printStackTrace();
        }
        
        return notifiche;
    }
    
    /**
     * Ottiene una notifica tramite ID.
     * 
     * @param idNotifica ID della notifica
     * @return oggetto Notifica se trovato, null altrimenti
     */
    public static Notifica getById(int idNotifica) {
        String sql = "SELECT * FROM Notifica WHERE ID_Notifica = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idNotifica);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createNotificaFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero notifica: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Elimina una notifica dal database.
     * 
     * @param idNotifica ID della notifica da eliminare
     * @return true se eliminazione riuscita, false altrimenti
     */
    public static boolean delete(int idNotifica) {
        String sql = "DELETE FROM Notifica WHERE ID_Notifica = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idNotifica);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore eliminazione notifica: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Crea un oggetto Notifica da un ResultSet.
     */
    private static Notifica createNotificaFromResultSet(ResultSet rs) throws SQLException {
        Integer idCorso = rs.getInt("ID_Corso");
        if (rs.wasNull()) {
            idCorso = null;
        }
        
        Timestamp timestamp = rs.getTimestamp("DataCreazione");
        LocalDateTime dataCreazione = timestamp != null ? timestamp.toLocalDateTime() : null;
        
        return new Notifica(
            rs.getInt("ID_Notifica"),
            rs.getInt("ID_Chef"),
            idCorso,
            rs.getString("Titolo"),
            rs.getString("Messaggio"),
            TipoModifica.fromString(rs.getString("TipoModifica")),
            rs.getBoolean("IsGlobale"),
            dataCreazione
        );
    }
}
package it.uninafoodlab.dao;

import it.uninafoodlab.model.domain.Corso;
import it.uninafoodlab.model.enums.Categoria;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione dei Corsi nel database.
 */
public class CorsoDAO {
    
    /**
     * Inserisce un nuovo corso nel database.
     * 
     * @param corso oggetto Corso da inserire
     * @return ID del corso inserito, -1 in caso di errore
     */
    public static int insert(Corso corso) {
        String sql = "INSERT INTO Corso (Titolo, Categoria, DataInizio, Frequenza, NumeroSessioni, ID_Chef) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, corso.getTitolo());
            ps.setString(2, corso.getCategoria().getDisplayName());
            ps.setDate(3, Date.valueOf(corso.getDataInizio()));
            ps.setInt(4, corso.getFrequenza());
            ps.setInt(5, corso.getNumeroSessioni());
            ps.setInt(6, corso.getIdChef());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Errore inserimento corso: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Ottiene tutti i corsi di uno chef.
     * 
     * @param idChef ID dello chef
     * @return lista di corsi dello chef
     */
    public static List<Corso> getByChef(int idChef) {
        List<Corso> corsi = new ArrayList<>();
        String sql = "SELECT * FROM Corso WHERE ID_Chef = ? ORDER BY DataInizio DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idChef);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                corsi.add(createCorsoFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero corsi chef: " + e.getMessage());
            e.printStackTrace();
        }
        
        return corsi;
    }
    
    /**
     * Ottiene i corsi di uno chef filtrati per categoria.
     * 
     * @param idChef ID dello chef
     * @param categoria categoria da filtrare
     * @return lista di corsi filtrati
     */
    public static List<Corso> getByChefAndCategoria(int idChef, Categoria categoria) {
        List<Corso> corsi = new ArrayList<>();
        String sql = "SELECT * FROM Corso WHERE ID_Chef = ? AND Categoria = ? ORDER BY DataInizio DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idChef);
            ps.setString(2, categoria.getDisplayName());
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                corsi.add(createCorsoFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero corsi per categoria: " + e.getMessage());
            e.printStackTrace();
        }
        
        return corsi;
    }
    
    /**
     * Ottiene un corso tramite ID.
     * 
     * @param idCorso ID del corso
     * @return oggetto Corso se trovato, null altrimenti
     */
    public static Corso getById(int idCorso) {
        String sql = "SELECT * FROM Corso WHERE ID_Corso = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCorso);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createCorsoFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero corso: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Aggiorna un corso esistente.
     * 
     * @param corso oggetto Corso con i dati aggiornati
     * @return true se aggiornamento riuscito, false altrimenti
     */
    public static boolean update(Corso corso) {
        String sql = "UPDATE Corso SET Titolo = ?, Categoria = ?, DataInizio = ?, " +
                     "Frequenza = ?, NumeroSessioni = ? WHERE ID_Corso = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, corso.getTitolo());
            ps.setString(2, corso.getCategoria().getDisplayName());
            ps.setDate(3, Date.valueOf(corso.getDataInizio()));
            ps.setInt(4, corso.getFrequenza());
            ps.setInt(5, corso.getNumeroSessioni());
            ps.setInt(6, corso.getIdCorso());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore aggiornamento corso: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina un corso dal database.
     * 
     * @param idCorso ID del corso da eliminare
     * @return true se eliminazione riuscita, false altrimenti
     */
    public static boolean delete(int idCorso) {
        String sql = "DELETE FROM Corso WHERE ID_Corso = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCorso);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore eliminazione corso: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Crea un oggetto Corso da un ResultSet.
     * 
     * @param rs ResultSet contenente i dati del corso
     * @return oggetto Corso
     * @throws SQLException se errore nel ResultSet
     */
    private static Corso createCorsoFromResultSet(ResultSet rs) throws SQLException {
        return new Corso(
            rs.getInt("ID_Corso"),
            rs.getString("Titolo"),
            Categoria.fromString(rs.getString("Categoria")),
            rs.getDate("DataInizio").toLocalDate(),
            rs.getInt("Frequenza"),
            rs.getInt("NumeroSessioni"),
            rs.getInt("ID_Chef")
        );
    }
}
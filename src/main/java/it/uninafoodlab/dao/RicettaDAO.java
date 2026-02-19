package it.uninafoodlab.dao;

import it.uninafoodlab.model.domain.Ricetta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione delle Ricette nel database PostgreSQL.
 * Le ricette non contengono più ingredienti testuali, ma riferimenti alla tabella Ingrediente.
 */
public class RicettaDAO {
    
    /**
     * Inserisce una nuova ricetta nel database (solo nome).
     * 
     * @param ricetta oggetto Ricetta da inserire
     * @return ID della ricetta inserita, -1 in caso di errore
     */
    public static int insert(Ricetta ricetta) {
        String sql = "INSERT INTO Ricetta (Nome, ID_SessionePratica) VALUES (?, ?) RETURNING ID_Ricetta";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ricetta.getNome());
            ps.setInt(2, ricetta.getIdSessionePratica());
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore inserimento ricetta: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Ottiene tutte le ricette di una sessione pratica.
     * 
     * @param idSessionePratica ID della sessione pratica
     * @return lista di ricette
     */
    public static List<Ricetta> getBySessionePratica(int idSessionePratica) {
        List<Ricetta> ricette = new ArrayList<>();
        String sql = "SELECT * FROM Ricetta WHERE ID_SessionePratica = ? ORDER BY Nome";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idSessionePratica);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ricette.add(createRicettaFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero ricette: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ricette;
    }
    
    /**
     * Ottiene tutte le ricette di un corso (da tutte le sue sessioni pratiche).
     * 
     * @param idCorso ID del corso
     * @return lista di ricette
     */
    public static List<Ricetta> getByCorso(int idCorso) {
        List<Ricetta> ricette = new ArrayList<>();
        String sql = "SELECT R.* FROM Ricetta R " +
                     "JOIN SessionePratica SP ON R.ID_SessionePratica = SP.ID_SessionePratica " +
                     "WHERE SP.ID_Corso = ? ORDER BY R.Nome";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCorso);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ricette.add(createRicettaFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero ricette corso: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ricette;
    }
    
    /**
     * Ottiene una ricetta tramite ID.
     * 
     * @param idRicetta ID della ricetta
     * @return oggetto Ricetta se trovato, null altrimenti
     */
    public static Ricetta getById(int idRicetta) {
        String sql = "SELECT * FROM Ricetta WHERE ID_Ricetta = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idRicetta);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createRicettaFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero ricetta: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Ottiene una ricetta con tutti i suoi ingredienti caricati.
     * 
     * @param idRicetta ID della ricetta
     * @return oggetto Ricetta con lista ingredienti popolata, null se non trovata
     */
    public static Ricetta getByIdWithIngredienti(int idRicetta) {
        Ricetta ricetta = getById(idRicetta);
        
        if (ricetta != null) {
            // Carica gli ingredienti
            ricetta.setIngredienti(RicettaIngredienteDAO.getByRicetta(idRicetta));
        }
        
        return ricetta;
    }
    
    /**
     * Aggiorna una ricetta esistente (solo il nome).
     * 
     * @param ricetta oggetto Ricetta con i dati aggiornati
     * @return true se aggiornamento riuscito, false altrimenti
     */
    public static boolean update(Ricetta ricetta) {
        String sql = "UPDATE Ricetta SET Nome = ? WHERE ID_Ricetta = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ricetta.getNome());
            ps.setInt(2, ricetta.getIdRicetta());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore aggiornamento ricetta: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina una ricetta dal database.
     * NOTA: Elimina automaticamente anche i record in Ricetta_Ingrediente (CASCADE).
     * 
     * @param idRicetta ID della ricetta da eliminare
     * @return true se eliminazione riuscita, false altrimenti
     */
    public static boolean delete(int idRicetta) {
        String sql = "DELETE FROM Ricetta WHERE ID_Ricetta = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idRicetta);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore eliminazione ricetta: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Conta il numero di ricette di una sessione pratica.
     * 
     * @param idSessionePratica ID della sessione pratica
     * @return numero di ricette
     */
    public static int countBySessionePratica(int idSessionePratica) {
        String sql = "SELECT COUNT(*) FROM Ricetta WHERE ID_SessionePratica = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idSessionePratica);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore conteggio ricette: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Crea un oggetto Ricetta da un ResultSet.
     * 
     * @param rs ResultSet contenente i dati della ricetta
     * @return oggetto Ricetta creato
     * @throws SQLException se si verifica un errore nel recupero dei dati
     */
    private static Ricetta createRicettaFromResultSet(ResultSet rs) throws SQLException {
        return new Ricetta(
            rs.getInt("ID_Ricetta"),
            rs.getString("Nome"),
            rs.getInt("ID_SessionePratica")
        );
    }
}
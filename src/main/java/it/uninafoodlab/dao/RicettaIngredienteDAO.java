package it.uninafoodlab.dao;

import it.uninafoodlab.model.domain.Ingrediente;
import it.uninafoodlab.model.domain.RicettaIngrediente;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione della relazione Ricetta-Ingrediente nel database PostgreSQL.
 * Gestisce l'associazione tra ricette e ingredienti con le relative quantità.
 */
public class RicettaIngredienteDAO {
    
    /**
     * Inserisce un nuovo ingrediente per una ricetta.
     * 
     * @param ricettaIngrediente oggetto RicettaIngrediente da inserire
     * @return true se inserimento riuscito, false altrimenti
     */
    public static boolean insert(RicettaIngrediente ricettaIngrediente) {
        String sql = "INSERT INTO Ricetta_Ingrediente (ID_Ricetta, ID_Ingrediente, Quantita) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, ricettaIngrediente.getIdRicetta());
            ps.setInt(2, ricettaIngrediente.getIdIngrediente());
            ps.setBigDecimal(3, ricettaIngrediente.getQuantita());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore inserimento ingrediente ricetta: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Ottiene tutti gli ingredienti di una ricetta con i dettagli completi.
     * 
     * @param idRicetta ID della ricetta
     * @return lista di RicettaIngrediente con oggetti Ingrediente popolati
     */
    public static List<RicettaIngrediente> getByRicetta(int idRicetta) {
        List<RicettaIngrediente> ingredienti = new ArrayList<>();
        String sql = "SELECT RI.*, I.Nome, I.Categoria, I.UnitaMisura " +
                     "FROM Ricetta_Ingrediente RI " +
                     "JOIN Ingrediente I ON RI.ID_Ingrediente = I.ID_Ingrediente " +
                     "WHERE RI.ID_Ricetta = ? " +
                     "ORDER BY I.Nome";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idRicetta);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                // Crea oggetto Ingrediente completo
                Ingrediente ingrediente = new Ingrediente(
                    rs.getInt("ID_Ingrediente"),
                    rs.getString("Nome"),
                    rs.getString("Categoria"),
                    rs.getString("UnitaMisura")
                );
                
                // Crea RicettaIngrediente con ingrediente popolato
                RicettaIngrediente ri = new RicettaIngrediente(
                    rs.getInt("ID_Ricetta"),
                    rs.getInt("ID_Ingrediente"),
                    rs.getBigDecimal("Quantita"),
                    null,
                    ingrediente
                );
                
                ingredienti.add(ri);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero ingredienti ricetta: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ingredienti;
    }
    
    /**
     * Ottiene tutte le ricette che usano un ingrediente specifico.
     * 
     * @param idIngrediente ID dell'ingrediente
     * @return lista di RicettaIngrediente
     */
    public static List<RicettaIngrediente> getByIngrediente(int idIngrediente) {
        List<RicettaIngrediente> ricette = new ArrayList<>();
        String sql = "SELECT * FROM Ricetta_Ingrediente WHERE ID_Ingrediente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idIngrediente);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ricette.add(createRicettaIngredienteFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero ricette per ingrediente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ricette;
    }
    
    /**
     * Aggiorna la quantità di un ingrediente in una ricetta.
     * 
     * @param idRicetta ID della ricetta
     * @param idIngrediente ID dell'ingrediente
     * @param nuovaQuantita nuova quantità
     * @return true se aggiornamento riuscito, false altrimenti
     */
    public static boolean updateQuantita(int idRicetta, int idIngrediente, BigDecimal nuovaQuantita) {
        String sql = "UPDATE Ricetta_Ingrediente SET Quantita = ? WHERE ID_Ricetta = ? AND ID_Ingrediente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setBigDecimal(1, nuovaQuantita);
            ps.setInt(2, idRicetta);
            ps.setInt(3, idIngrediente);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore aggiornamento quantità ingrediente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina un ingrediente da una ricetta.
     * 
     * @param idRicetta ID della ricetta
     * @param idIngrediente ID dell'ingrediente
     * @return true se eliminazione riuscita, false altrimenti
     */
    public static boolean delete(int idRicetta, int idIngrediente) {
        String sql = "DELETE FROM Ricetta_Ingrediente WHERE ID_Ricetta = ? AND ID_Ingrediente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idRicetta);
            ps.setInt(2, idIngrediente);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore eliminazione ingrediente da ricetta: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina tutti gli ingredienti di una ricetta.
     * 
     * @param idRicetta ID della ricetta
     * @return true se eliminazione riuscita, false altrimenti
     */
    public static boolean deleteAllByRicetta(int idRicetta) {
        String sql = "DELETE FROM Ricetta_Ingrediente WHERE ID_Ricetta = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idRicetta);
            return ps.executeUpdate() >= 0;  // >= 0 perché potrebbe non esserci nulla da eliminare
            
        } catch (SQLException e) {
            System.err.println("Errore eliminazione ingredienti ricetta: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Conta quanti ingredienti ha una ricetta.
     * 
     * @param idRicetta ID della ricetta
     * @return numero di ingredienti
     */
    public static int countByRicetta(int idRicetta) {
        String sql = "SELECT COUNT(*) FROM Ricetta_Ingrediente WHERE ID_Ricetta = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idRicetta);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore conteggio ingredienti: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Crea un oggetto RicettaIngrediente da un ResultSet (senza join).
     * 
     * @param rs ResultSet contenente i dati
     * @return oggetto RicettaIngrediente creato
     * @throws SQLException se si verifica un errore nel recupero dei dati
     */
    private static RicettaIngrediente createRicettaIngredienteFromResultSet(ResultSet rs) throws SQLException {
        return new RicettaIngrediente(
            rs.getInt("ID_Ricetta"),
            rs.getInt("ID_Ingrediente"),
            rs.getBigDecimal("Quantita")
        );
    }
}
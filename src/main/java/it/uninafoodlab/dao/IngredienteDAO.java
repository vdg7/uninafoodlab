package it.uninafoodlab.dao;

import it.uninafoodlab.model.domain.Ingrediente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione degli Ingredienti nel database PostgreSQL.
 * Fornisce metodi CRUD completi per la gestione degli ingredienti delle ricette.
 */
public class IngredienteDAO {
    
    /**
     * Inserisce un nuovo ingrediente nel database.
     * 
     * @param ingrediente oggetto Ingrediente da inserire
     * @return ID dell'ingrediente inserito, -1 in caso di errore
     */
    public static int insert(Ingrediente ingrediente) {
        String sql = "INSERT INTO Ingrediente (Nome, Categoria, UnitaMisura) VALUES (?, ?, ?) RETURNING ID_Ingrediente";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ingrediente.getNome());
            ps.setString(2, ingrediente.getCategoria());
            ps.setString(3, ingrediente.getUnitaMisura());
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore inserimento ingrediente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Ottiene un ingrediente tramite ID.
     * 
     * @param idIngrediente ID dell'ingrediente da recuperare
     * @return oggetto Ingrediente se trovato, null altrimenti
     */
    public static Ingrediente getById(int idIngrediente) {
        String sql = "SELECT * FROM Ingrediente WHERE ID_Ingrediente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idIngrediente);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createIngredienteFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero ingrediente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Ottiene un ingrediente tramite nome.
     * 
     * @param nome nome dell'ingrediente da cercare
     * @return oggetto Ingrediente se trovato, null altrimenti
     */
    public static Ingrediente getByNome(String nome) {
        String sql = "SELECT * FROM Ingrediente WHERE Nome = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createIngredienteFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero ingrediente per nome: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Ottiene tutti gli ingredienti dal database.
     * 
     * @return lista di tutti gli ingredienti ordinata per nome
     */
    public static List<Ingrediente> getAll() {
        List<Ingrediente> ingredienti = new ArrayList<>();
        String sql = "SELECT * FROM Ingrediente ORDER BY Nome";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ingredienti.add(createIngredienteFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero ingredienti: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ingredienti;
    }
    
    /**
     * Ottiene tutti gli ingredienti di una specifica categoria.
     * 
     * @param categoria categoria degli ingredienti da recuperare (es: Verdura, Carne, Latticini)
     * @return lista di ingredienti della categoria specificata
     */
    public static List<Ingrediente> getByCategoria(String categoria) {
        List<Ingrediente> ingredienti = new ArrayList<>();
        String sql = "SELECT * FROM Ingrediente WHERE Categoria = ? ORDER BY Nome";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, categoria);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ingredienti.add(createIngredienteFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero ingredienti per categoria: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ingredienti;
    }
    
    /**
     * Cerca ingredienti il cui nome contiene la stringa specificata (case-insensitive).
     * 
     * @param searchTerm termine di ricerca
     * @return lista di ingredienti che contengono il termine cercato nel nome
     */
    public static List<Ingrediente> searchByNome(String searchTerm) {
        List<Ingrediente> ingredienti = new ArrayList<>();
        String sql = "SELECT * FROM Ingrediente WHERE LOWER(Nome) LIKE LOWER(?) ORDER BY Nome";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + searchTerm + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ingredienti.add(createIngredienteFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore ricerca ingredienti: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ingredienti;
    }
    
    /**
     * Aggiorna un ingrediente esistente nel database.
     * 
     * @param ingrediente oggetto Ingrediente con i dati aggiornati
     * @return true se l'aggiornamento è riuscito, false altrimenti
     */
    public static boolean update(Ingrediente ingrediente) {
        String sql = "UPDATE Ingrediente SET Nome = ?, Categoria = ?, UnitaMisura = ? WHERE ID_Ingrediente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ingrediente.getNome());
            ps.setString(2, ingrediente.getCategoria());
            ps.setString(3, ingrediente.getUnitaMisura());
            ps.setInt(4, ingrediente.getIdIngrediente());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore aggiornamento ingrediente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina un ingrediente dal database.
     * 
     * @param idIngrediente ID dell'ingrediente da eliminare
     * @return true se l'eliminazione è riuscita, false altrimenti
     */
    public static boolean delete(int idIngrediente) {
        String sql = "DELETE FROM Ingrediente WHERE ID_Ingrediente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idIngrediente);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore eliminazione ingrediente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Conta il numero totale di ingredienti nel database.
     * 
     * @return numero totale di ingredienti
     */
    public static int count() {
        String sql = "SELECT COUNT(*) FROM Ingrediente";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
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
     * Verifica se un ingrediente con il nome specificato esiste già nel database.
     * 
     * @param nome nome dell'ingrediente da verificare
     * @return true se l'ingrediente esiste, false altrimenti
     */
    public static boolean exists(String nome) {
        String sql = "SELECT COUNT(*) FROM Ingrediente WHERE Nome = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Errore verifica esistenza ingrediente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Crea un oggetto Ingrediente da un ResultSet.
     * 
     * @param rs ResultSet contenente i dati dell'ingrediente
     * @return oggetto Ingrediente creato
     * @throws SQLException se si verifica un errore nel recupero dei dati
     */
    private static Ingrediente createIngredienteFromResultSet(ResultSet rs) throws SQLException {
        return new Ingrediente(
            rs.getInt("ID_Ingrediente"),
            rs.getString("Nome"),
            rs.getString("Categoria"),
            rs.getString("UnitaMisura")
        );
    }
}
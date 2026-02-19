package it.uninafoodlab.dao;

import it.uninafoodlab.model.domain.Studente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object per la gestione degli Studenti nel database.
 * Fornisce metodi per autenticazione, recupero e verifica studenti.
 */
public class StudenteDAO {
    
    /**
     * Autentica uno studente tramite email e password.
     * 
     * @param email email dello studente
     * @param password password dello studente
     * @return oggetto Studente se le credenziali sono corrette, null altrimenti
     */
    public static Studente login(String email, String password) {
        String sql = "SELECT * FROM Studente WHERE Email = ? AND Password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createStudenteFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore login studente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Ottiene uno studente tramite ID.
     * 
     * @param idStudente ID dello studente da recuperare
     * @return oggetto Studente se trovato, null altrimenti
     */
    public static Studente getById(int idStudente) {
        String sql = "SELECT * FROM Studente WHERE ID_Studente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idStudente);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createStudenteFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero studente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Ottiene uno studente tramite matricola.
     * 
     * @param matricola matricola dello studente
     * @return oggetto Studente se trovato, null altrimenti
     */
    public static Studente getByMatricola(String matricola) {
        String sql = "SELECT * FROM Studente WHERE Matricola = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, matricola);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createStudenteFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero studente per matricola: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Ottiene tutti gli studenti dal database.
     * 
     * @return lista di tutti gli studenti ordinata per nome
     */
    public static List<Studente> getAll() {
        List<Studente> studenti = new ArrayList<>();
        String sql = "SELECT * FROM Studente ORDER BY Nome";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                studenti.add(createStudenteFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero studenti: " + e.getMessage());
            e.printStackTrace();
        }
        
        return studenti;
    }
    
    /**
     * Ottiene tutti gli studenti iscritti a un corso specifico.
     * 
     * @param idCorso ID del corso
     * @return lista di studenti iscritti al corso
     */
    public static List<Studente> getByCorso(int idCorso) {
        List<Studente> studenti = new ArrayList<>();
        String sql = "SELECT S.* FROM Studente S " +
                     "JOIN Iscrizione I ON S.ID_Studente = I.ID_Studente " +
                     "WHERE I.ID_Corso = ? " +
                     "ORDER BY S.Nome";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCorso);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                studenti.add(createStudenteFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero studenti per corso: " + e.getMessage());
            e.printStackTrace();
        }
        
        return studenti;
    }
    
    /**
     * Verifica se una matricola esiste già nel database.
     * 
     * @param matricola matricola da verificare
     * @return true se la matricola esiste, false altrimenti
     */
    public static boolean matricolaExists(String matricola) {
        String sql = "SELECT COUNT(*) FROM Studente WHERE Matricola = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, matricola);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Errore verifica matricola: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Verifica se un'email esiste già nel database.
     * 
     * @param email email da verificare
     * @return true se l'email esiste, false altrimenti
     */
    public static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Studente WHERE Email = ?";
        
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
     * Conta il numero totale di studenti nel database.
     * 
     * @return numero totale di studenti
     */
    public static int count() {
        String sql = "SELECT COUNT(*) FROM Studente";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore conteggio studenti: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Crea un oggetto Studente da un ResultSet.
     * 
     * @param rs ResultSet contenente i dati dello studente
     * @return oggetto Studente creato
     * @throws SQLException se si verifica un errore nel recupero dei dati
     */
    private static Studente createStudenteFromResultSet(ResultSet rs) throws SQLException {
        return new Studente(
            rs.getInt("ID_Studente"),
            rs.getString("Nome"),
            rs.getString("Matricola"),
            rs.getString("Email"),
            rs.getString("Password")
        );
    }
}
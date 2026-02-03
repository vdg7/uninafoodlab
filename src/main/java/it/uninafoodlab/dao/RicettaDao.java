package it.uninafoodlab.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import it.uninafoodlab.domain.Ricetta;
import it.uninafoodlab.db.DbConnectionFactory;

public class RicettaDao {

    public List<Ricetta> findBySessionePratica(int idCorso) {
        List<Ricetta> ricette = new ArrayList<>();

        String sql = """
            SELECT ID_Ricetta, Nome, Descrizione, ID_Corso
            FROM Ricetta
            WHERE ID_SessionePratica = ?
        """;

        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCorso);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ricetta r = new Ricetta();
                r.setIdRicetta(rs.getInt("ID_Ricetta"));
                r.setNome(rs.getString("Nome"));
                r.setIngredienti(rs.getString("Descrizione"));
                r.setIdSessionePratica(rs.getInt("ID_SessionePratica"));

                ricette.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ricette;
    }
    
    
    public List<Ricetta> findByCorso(int idCorso) {
        List<Ricetta> ricette = new ArrayList<>();

        String sql = """
            SELECT r.ID_Ricetta, r.Nome, r.Ingredienti, r.ID_SessionePratica
            FROM Ricetta r
            JOIN SessionePratica sp ON r.ID_SessionePratica = sp.ID_SessionePratica
            WHERE sp.ID_Corso = ?
        """;

        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCorso);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ricetta r = new Ricetta();
                r.setIdRicetta(rs.getInt("ID_Ricetta"));
                r.setNome(rs.getString("Nome"));
                r.setIngredienti(rs.getString("Ingredienti"));
                r.setIdSessionePratica(rs.getInt("ID_SessionePratica"));
                ricette.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ricette;
    }
    
    
    public int insert(Ricetta ricetta) {
        String sql = """
            INSERT INTO Ricetta (Nome, Ingredienti, ID_SessionePratica)
            VALUES (?, ?, ?)
        """;
        
        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, ricetta.getNome());
            ps.setString(2, ricetta.getIngredienti());
            ps.setInt(3, ricetta.getIdSessionePratica());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1;
    }
    
    
    public boolean update(Ricetta ricetta) {
        String sql = """
            UPDATE Ricetta 
            SET Nome = ?, Ingredienti = ?, ID_SessionePratica = ?
            WHERE ID_Ricetta = ?
        """;
        
        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ricetta.getNome());
            ps.setString(2, ricetta.getIngredienti());
            ps.setInt(3, ricetta.getIdSessionePratica());
            ps.setInt(4, ricetta.getIdRicetta());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean delete(int idRicetta) {
        String sql = "DELETE FROM Ricetta WHERE ID_Ricetta = ?";
        
        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idRicetta);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}

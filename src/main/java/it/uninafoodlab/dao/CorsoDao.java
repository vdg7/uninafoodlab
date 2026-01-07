package it.uninafoodlab.dao;

import it.uninafoodlab.db.DbConnectionFactory;
import it.uninafoodlab.domain.Corso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class CorsoDao {

	 public int insert(Corso corso) {

	        String sql = """
	            INSERT INTO Corso (Titolo, Tema, DataInizio, Frequenza, ID_Chef)
	            VALUES (?, ?, ?, ?, ?)
	        """;

	        try (Connection conn = DbConnectionFactory.openConnection();
	             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	            ps.setString(1, corso.getTitolo());
	            ps.setString(2, corso.getTema());
	            ps.setDate(3, java.sql.Date.valueOf(corso.getDataInizio()));
	            ps.setInt(4, corso.getFrequenzaSettimanale());
	            ps.setInt(5, corso.getIdChef());
	            
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
	 
	 public List<Corso> findByChef(int idChef) {
	        String sql = """
	            SELECT ID_Corso, Titolo, Tema, DataInizio, Frequenza
	            FROM Corso
	            WHERE ID_Chef = ?
	        """;

	        List<Corso> corsi = new ArrayList<>();

	        try (Connection conn = DbConnectionFactory.openConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setInt(1, idChef);
	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	                Corso c = new Corso();
	                c.setIdCorso(rs.getInt("ID_Corso"));
	                c.setTitolo(rs.getString("Titolo"));
	                c.setTema(rs.getString("Tema"));
	                c.setDataInizio(rs.getDate("DataInizio").toLocalDate());
	                c.setFrequenzaSettimanale(rs.getInt("Frequenza"));
	                c.setIdChef(idChef);
	                corsi.add(c);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return corsi;
	    }

	    public List<Corso> findByChefAndTema(int idChef, String tema) {
	        String sql = """
	            SELECT ID_Corso, Titolo, Tema, DataInizio, Frequenza
	            FROM Corso
	            WHERE ID_Chef = ? AND Tema = ?
	        """;

	        List<Corso> corsi = new ArrayList<>();

	        try (Connection conn = DbConnectionFactory.openConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setInt(1, idChef);
	            ps.setString(2, tema);

	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	                Corso c = new Corso();
	                c.setIdCorso(rs.getInt("ID_Corso"));
	                c.setTitolo(rs.getString("Titolo"));
	                c.setTema(rs.getString("Tema"));
	                c.setDataInizio(rs.getDate("DataInizio").toLocalDate());
	                c.setFrequenzaSettimanale(rs.getInt("Frequenza"));
	                c.setIdChef(idChef);
	                corsi.add(c);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return corsi;
	    }
}

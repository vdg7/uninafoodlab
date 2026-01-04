package it.uninafoodlab.dao;

import it.uninafoodlab.db.DbConnectionFactory;
import it.uninafoodlab.domain.Corso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class CorsoDao {

	 public int insert(Corso corso) {

	        String sql = """
	            INSERT INTO Corso (Titolo, Categoria, DataInizio, Frequenza, ID_Chef)
	            VALUES (?, ?, ?, ?, ?)
	        """;

	        try (Connection conn = DbConnectionFactory.openConnection();
	             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	            ps.setString(1, corso.getTitolo());
	            ps.setString(2, corso.getCategoria());
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
}

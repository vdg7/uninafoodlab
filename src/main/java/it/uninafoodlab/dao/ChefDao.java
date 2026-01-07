package it.uninafoodlab.dao;

import it.uninafoodlab.db.DbConnectionFactory;
import it.uninafoodlab.domain.Chef;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChefDao {

    public Chef findByEmailAndPassword(String email, String password) {

        String sql = """
            SELECT ID_Chef, Nome, Email, Password
            FROM Chef
            WHERE Email = ? AND Password = ?
        """;

        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Chef chef = new Chef();
                chef.setIdChef(rs.getInt("ID_Chef"));
                chef.setNome(rs.getString("Nome"));
                chef.setEmail(rs.getString("Email"));
                chef.setPassword(rs.getString("Password"));
                return chef;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

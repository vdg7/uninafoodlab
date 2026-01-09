package it.uninafoodlab.dao;

import it.uninafoodlab.db.DbConnectionFactory;
import it.uninafoodlab.domain.Ricetta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RicettaDao {

    public List<Ricetta> findBySessionePratica(int idSessione) {
        String sql = """
            SELECT r.ID_Ricetta, r.Nome, r.Descrizione
            FROM Ricetta r
            JOIN Partecipazione p ON r.ID_Ricetta = p.ID_Ricetta
            WHERE p.ID_Sessione = ?
        """;

        List<Ricetta> ricette = new ArrayList<>();

        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSessione);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ricetta r = new Ricetta();
                r.setIdRicetta(rs.getInt("ID_Ricetta"));
                r.setNome(rs.getString("Nome"));
                r.setDescrizione(rs.getString("Descrizione"));
                ricette.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ricette;
    }
    
    public void addRicettaToSessione(int idRicetta, int idSessione) {
        String sql = """
            INSERT INTO Partecipazione (ID_Sessione, ID_Ricetta)
            VALUES (?, ?)
        """;

        try (Connection conn = DbConnectionFactory.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSessione);
            ps.setInt(2, idRicetta);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

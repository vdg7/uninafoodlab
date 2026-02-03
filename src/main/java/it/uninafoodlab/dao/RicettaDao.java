package it.uninafoodlab.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import it.uninafoodlab.domain.Ricetta;
import it.uninafoodlab.db.DbConnectionFactory;

public class RicettaDao {

    public List<Ricetta> findByCorso(int idCorso) {
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
                r.setDescrizione(rs.getString("Descrizione"));
                r.setIdSessionePratica(rs.getInt("ID_SessionePratica"));

                ricette.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ricette;
    }
}

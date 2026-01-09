package it.uninafoodlab.controller;

import it.uninafoodlab.dao.ReportDao;
import it.uninafoodlab.db.DbConnectionFactory;
import it.uninafoodlab.domain.CorsoDettaglio;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReportController {

    private final ReportDao reportDao;

    public ReportController(ReportDao reportDao) {
        this.reportDao = reportDao;
    }

    public List<CorsoDettaglio> getReportCorsi() {
        return reportDao.getCorsiDettaglio();
    }
    
    public int getReportMensileChef(int idChef, int mese, int anno) {
        String sql = "{ CALL ReportMensile(?, ?, ?) }";

        try (Connection conn = DbConnectionFactory.openConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idChef);
            cs.setInt(2, mese);
            cs.setInt(3, anno);

            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                return rs.getInt("TotaleSessioni");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}

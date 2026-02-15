package it.uninafoodlab.dao;

import java.sql.*;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object per la generazione di report mensili.
 * Fornisce statistiche aggregate sui corsi e le sessioni.
 */
public class ReportDAO {
    
    /**
     * Classe interna per rappresentare i dati del report mensile.
     */
    public static class ReportMensile {
        private int numeroCorsi;
        private int numeroSessioniOnline;
        private int numeroSessioniPratiche;
        private int maxRicettePerSessione;
        private int minRicettePerSessione;
        private double mediaRicettePerSessione;
        
        public ReportMensile(int numeroCorsi, int numeroSessioniOnline, int numeroSessioniPratiche,
                             int maxRicette, int minRicette, double mediaRicette) {
            this.numeroCorsi = numeroCorsi;
            this.numeroSessioniOnline = numeroSessioniOnline;
            this.numeroSessioniPratiche = numeroSessioniPratiche;
            this.maxRicettePerSessione = maxRicette;
            this.minRicettePerSessione = minRicette;
            this.mediaRicettePerSessione = mediaRicette;
        }
        
        // Getters
        public int getNumeroCorsi() { return numeroCorsi; }
        public int getNumeroSessioniOnline() { return numeroSessioniOnline; }
        public int getNumeroSessioniPratiche() { return numeroSessioniPratiche; }
        public int getMaxRicettePerSessione() { return maxRicettePerSessione; }
        public int getMinRicettePerSessione() { return minRicettePerSessione; }
        public double getMediaRicettePerSessione() { return mediaRicettePerSessione; }
    }
    
    /**
     * Genera un report mensile per uno chef specifico.
     * 
     * @param idChef ID dello chef
     * @param yearMonth mese e anno per il report
     * @return oggetto ReportMensile con le statistiche
     */
    public static ReportMensile getReportMensile(int idChef, YearMonth yearMonth) {
        String sql = "SELECT " +
                     "  COUNT(DISTINCT C.ID_Corso) AS NumeroCorsi, " +
                     "  COUNT(DISTINCT SO.ID_SessioneOnline) AS NumeroSessioniOnline, " +
                     "  COUNT(DISTINCT SP.ID_SessionePratica) AS NumeroSessioniPratiche, " +
                     "  COALESCE(MAX(RC.NumRicette), 0) AS MaxRicette, " +
                     "  COALESCE(MIN(RC.NumRicette), 0) AS MinRicette, " +
                     "  COALESCE(AVG(RC.NumRicette), 0) AS MediaRicette " +
                     "FROM Corso C " +
                     "LEFT JOIN SessioneOnline SO ON C.ID_Corso = SO.ID_Corso " +
                     "  AND YEAR(SO.Data) = ? AND MONTH(SO.Data) = ? " +
                     "LEFT JOIN SessionePratica SP ON C.ID_Corso = SP.ID_Corso " +
                     "  AND YEAR(SP.Data) = ? AND MONTH(SP.Data) = ? " +
                     "LEFT JOIN ( " +
                     "  SELECT R.ID_SessionePratica, COUNT(*) AS NumRicette " +
                     "  FROM Ricetta R " +
                     "  JOIN SessionePratica SP2 ON R.ID_SessionePratica = SP2.ID_SessionePratica " +
                     "  WHERE YEAR(SP2.Data) = ? AND MONTH(SP2.Data) = ? " +
                     "  GROUP BY R.ID_SessionePratica " +
                     ") RC ON SP.ID_SessionePratica = RC.ID_SessionePratica " +
                     "WHERE C.ID_Chef = ? " +
                     "  AND (SO.ID_SessioneOnline IS NOT NULL OR SP.ID_SessionePratica IS NOT NULL)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            int year = yearMonth.getYear();
            int month = yearMonth.getMonthValue();
            
            ps.setInt(1, year);
            ps.setInt(2, month);
            ps.setInt(3, year);
            ps.setInt(4, month);
            ps.setInt(5, year);
            ps.setInt(6, month);
            ps.setInt(7, idChef);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new ReportMensile(
                    rs.getInt("NumeroCorsi"),
                    rs.getInt("NumeroSessioniOnline"),
                    rs.getInt("NumeroSessioniPratiche"),
                    rs.getInt("MaxRicette"),
                    rs.getInt("MinRicette"),
                    rs.getDouble("MediaRicette")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Errore generazione report mensile: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Report vuoto in caso di errore
        return new ReportMensile(0, 0, 0, 0, 0, 0.0);
    }
    
    /**
     * Ottiene il numero di sessioni online e pratiche per ogni mese dell'anno.
     * Utile per generare grafici JFreeChart.
     * 
     * @param idChef ID dello chef
     * @param anno anno di riferimento
     * @return Map con mese (1-12) come chiave e array [sessioniOnline, sessioniPratiche] come valore
     */
    public static Map<Integer, int[]> getSessioniPerMese(int idChef, int anno) {
        Map<Integer, int[]> risultato = new HashMap<>();
        
        // Inizializza tutti i mesi a 0
        for (int i = 1; i <= 12; i++) {
            risultato.put(i, new int[]{0, 0});
        }
        
        String sql = "SELECT " +
                     "  MONTH(SO.Data) AS Mese, " +
                     "  COUNT(DISTINCT SO.ID_SessioneOnline) AS SessioniOnline " +
                     "FROM SessioneOnline SO " +
                     "JOIN Corso C ON SO.ID_Corso = C.ID_Corso " +
                     "WHERE C.ID_Chef = ? AND YEAR(SO.Data) = ? " +
                     "GROUP BY MONTH(SO.Data) " +
                     "UNION ALL " +
                     "SELECT " +
                     "  MONTH(SP.Data) AS Mese, " +
                     "  COUNT(DISTINCT SP.ID_SessionePratica) AS SessioniPratiche " +
                     "FROM SessionePratica SP " +
                     "JOIN Corso C ON SP.ID_Corso = C.ID_Corso " +
                     "WHERE C.ID_Chef = ? AND YEAR(SP.Data) = ? " +
                     "GROUP BY MONTH(SP.Data)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idChef);
            ps.setInt(2, anno);
            ps.setInt(3, idChef);
            ps.setInt(4, anno);
            
            ResultSet rs = ps.executeQuery();
            
            boolean isOnline = true;
            while (rs.next()) {
                int mese = rs.getInt("Mese");
                int count = rs.getInt(2);
                
                if (isOnline) {
                    risultato.get(mese)[0] = count;
                } else {
                    risultato.get(mese)[1] = count;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Errore recupero sessioni per mese: " + e.getMessage());
            e.printStackTrace();
        }
        
        return risultato;
    }
}
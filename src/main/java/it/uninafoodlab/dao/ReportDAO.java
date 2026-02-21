package it.uninafoodlab.dao;

import java.sql.*;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object per la generazione di report mensili.
 * Fornisce statistiche aggregate sui corsi e le sessioni.
 * Query scritte per PostgreSQL (usa EXTRACT invece di YEAR/MONTH).
 */
public class ReportDAO {

    // ==================== CLASSE INTERNA ====================

    /**
     * Classe interna per rappresentare i dati del report mensile.
     */
    public static class ReportMensile {
        private final int numeroCorsi;
        private final int numeroSessioniOnline;
        private final int numeroSessioniPratiche;
        private final int maxRicettePerSessione;
        private final int minRicettePerSessione;
        private final double mediaRicettePerSessione;

        public ReportMensile(int numeroCorsi, int numeroSessioniOnline, int numeroSessioniPratiche,
                             int maxRicette, int minRicette, double mediaRicette) {
            this.numeroCorsi             = numeroCorsi;
            this.numeroSessioniOnline    = numeroSessioniOnline;
            this.numeroSessioniPratiche  = numeroSessioniPratiche;
            this.maxRicettePerSessione   = maxRicette;
            this.minRicettePerSessione   = minRicette;
            this.mediaRicettePerSessione = mediaRicette;
        }

        public int    getNumeroCorsi()             { return numeroCorsi; }
        public int    getNumeroSessioniOnline()    { return numeroSessioniOnline; }
        public int    getNumeroSessioniPratiche()  { return numeroSessioniPratiche; }
        public int    getMaxRicettePerSessione()   { return maxRicettePerSessione; }
        public int    getMinRicettePerSessione()   { return minRicettePerSessione; }
        public double getMediaRicettePerSessione() { return mediaRicettePerSessione; }
    }

    // ==================== METODI PUBBLICI ====================

    /**
     * Genera un report mensile per uno chef specifico.
     *
     * @param idChef    ID dello chef
     * @param yearMonth mese e anno per il report
     * @return oggetto ReportMensile con le statistiche
     */
    public static ReportMensile getReportMensile(int idChef, YearMonth yearMonth) {
        int year  = yearMonth.getYear();
        int month = yearMonth.getMonthValue();

        int    numeroCorsi   = countCorsiNelMese(idChef, year, month);
        int    sessOnline    = countSessioniOnlineNelMese(idChef, year, month);
        int    sessPratiche  = countSessioniPraticheNelMese(idChef, year, month);
        int[]  maxMin        = getRicetteMaxMin(idChef, year, month);
        double media         = getRicetteMedia(idChef, year, month);

        return new ReportMensile(numeroCorsi, sessOnline, sessPratiche,
                                 maxMin[0], maxMin[1], media);
    }

    /**
     * Restituisce il numero di sessioni online e pratiche per ogni mese
     * dell'anno specificato. Utile per i grafici annuali JFreeChart.
     *
     * @param idChef ID dello chef
     * @param anno   anno di riferimento
     * @return Map con mese (1-12) come chiave e int[]{sessOnline, sessPratiche} come valore
     */
    public static Map<Integer, int[]> getSessioniPerMese(int idChef, int anno) {
        Map<Integer, int[]> risultato = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            risultato.put(i, new int[]{0, 0});
        }

        // --- Sessioni online ---
        String sqlOnline =
            "SELECT EXTRACT(MONTH FROM SO.Data)::int AS Mese, " +
            "       COUNT(SO.ID_SessioneOnline) AS Conteggio " +
            "FROM SessioneOnline SO " +
            "JOIN Corso C ON SO.ID_Corso = C.ID_Corso " +
            "WHERE C.ID_Chef = ? " +
            "  AND EXTRACT(YEAR FROM SO.Data)::int = ? " +
            "GROUP BY EXTRACT(MONTH FROM SO.Data)::int";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlOnline)) {
            ps.setInt(1, idChef);
            ps.setInt(2, anno);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int mese = rs.getInt("Mese");
                risultato.get(mese)[0] = rs.getInt("Conteggio");
            }
        } catch (SQLException e) {
            System.err.println("Errore sessioni online per mese: " + e.getMessage());
            e.printStackTrace();
        }

        // --- Sessioni pratiche ---
        String sqlPratiche =
            "SELECT EXTRACT(MONTH FROM SP.Data)::int AS Mese, " +
            "       COUNT(SP.ID_SessionePratica) AS Conteggio " +
            "FROM SessionePratica SP " +
            "JOIN Corso C ON SP.ID_Corso = C.ID_Corso " +
            "WHERE C.ID_Chef = ? " +
            "  AND EXTRACT(YEAR FROM SP.Data)::int = ? " +
            "GROUP BY EXTRACT(MONTH FROM SP.Data)::int";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlPratiche)) {
            ps.setInt(1, idChef);
            ps.setInt(2, anno);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int mese = rs.getInt("Mese");
                risultato.get(mese)[1] = rs.getInt("Conteggio");
            }
        } catch (SQLException e) {
            System.err.println("Errore sessioni pratiche per mese: " + e.getMessage());
            e.printStackTrace();
        }

        return risultato;
    }

    // ==================== METODI PRIVATI DI SUPPORTO ====================

    /**
     * Conta i corsi con almeno una sessione nel mese/anno.
     */
    private static int countCorsiNelMese(int idChef, int year, int month) {
        String sql =
            "SELECT COUNT(DISTINCT C.ID_Corso) AS Totale " +
            "FROM Corso C " +
            "WHERE C.ID_Chef = ? " +
            "  AND ( " +
            "    EXISTS ( " +
            "      SELECT 1 FROM SessioneOnline SO " +
            "      WHERE SO.ID_Corso = C.ID_Corso " +
            "        AND EXTRACT(YEAR  FROM SO.Data)::int = ? " +
            "        AND EXTRACT(MONTH FROM SO.Data)::int = ? " +
            "    ) " +
            "    OR EXISTS ( " +
            "      SELECT 1 FROM SessionePratica SP " +
            "      WHERE SP.ID_Corso = C.ID_Corso " +
            "        AND EXTRACT(YEAR  FROM SP.Data)::int = ? " +
            "        AND EXTRACT(MONTH FROM SP.Data)::int = ? " +
            "    ) " +
            "  )";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idChef);
            ps.setInt(2, year);
            ps.setInt(3, month);
            ps.setInt(4, year);
            ps.setInt(5, month);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("Totale");
        } catch (SQLException e) {
            System.err.println("Errore conteggio corsi nel mese: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Conta le sessioni online dello chef nel mese/anno.
     */
    private static int countSessioniOnlineNelMese(int idChef, int year, int month) {
        String sql =
            "SELECT COUNT(SO.ID_SessioneOnline) AS Totale " +
            "FROM SessioneOnline SO " +
            "JOIN Corso C ON SO.ID_Corso = C.ID_Corso " +
            "WHERE C.ID_Chef = ? " +
            "  AND EXTRACT(YEAR  FROM SO.Data)::int = ? " +
            "  AND EXTRACT(MONTH FROM SO.Data)::int = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idChef);
            ps.setInt(2, year);
            ps.setInt(3, month);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("Totale");
        } catch (SQLException e) {
            System.err.println("Errore conteggio sessioni online: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Conta le sessioni pratiche dello chef nel mese/anno.
     */
    private static int countSessioniPraticheNelMese(int idChef, int year, int month) {
        String sql =
            "SELECT COUNT(SP.ID_SessionePratica) AS Totale " +
            "FROM SessionePratica SP " +
            "JOIN Corso C ON SP.ID_Corso = C.ID_Corso " +
            "WHERE C.ID_Chef = ? " +
            "  AND EXTRACT(YEAR  FROM SP.Data)::int = ? " +
            "  AND EXTRACT(MONTH FROM SP.Data)::int = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idChef);
            ps.setInt(2, year);
            ps.setInt(3, month);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("Totale");
        } catch (SQLException e) {
            System.err.println("Errore conteggio sessioni pratiche: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Restituisce [MAX, MIN] ricette per sessione pratica nel mese.
     * Se non ci sono sessioni pratiche ritorna [0, 0].
     */
    private static int[] getRicetteMaxMin(int idChef, int year, int month) {
        String sql =
            "SELECT COALESCE(MAX(rc.NumRicette), 0) AS MaxRicette, " +
            "       COALESCE(MIN(rc.NumRicette), 0) AS MinRicette " +
            "FROM ( " +
            "  SELECT SP.ID_SessionePratica, COUNT(R.ID_Ricetta) AS NumRicette " +
            "  FROM SessionePratica SP " +
            "  JOIN Corso C ON SP.ID_Corso = C.ID_Corso " +
            "  LEFT JOIN Ricetta R ON R.ID_SessionePratica = SP.ID_SessionePratica " +
            "  WHERE C.ID_Chef = ? " +
            "    AND EXTRACT(YEAR  FROM SP.Data)::int = ? " +
            "    AND EXTRACT(MONTH FROM SP.Data)::int = ? " +
            "  GROUP BY SP.ID_SessionePratica " +
            ") rc";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idChef);
            ps.setInt(2, year);
            ps.setInt(3, month);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new int[]{ rs.getInt("MaxRicette"), rs.getInt("MinRicette") };
            }
        } catch (SQLException e) {
            System.err.println("Errore max/min ricette: " + e.getMessage());
            e.printStackTrace();
        }
        return new int[]{0, 0};
    }

    /**
     * Restituisce la media di ricette per sessione pratica nel mese.
     */
    private static double getRicetteMedia(int idChef, int year, int month) {
        String sql =
            "SELECT COALESCE(AVG(rc.NumRicette), 0.0) AS MediaRicette " +
            "FROM ( " +
            "  SELECT SP.ID_SessionePratica, COUNT(R.ID_Ricetta) AS NumRicette " +
            "  FROM SessionePratica SP " +
            "  JOIN Corso C ON SP.ID_Corso = C.ID_Corso " +
            "  LEFT JOIN Ricetta R ON R.ID_SessionePratica = SP.ID_SessionePratica " +
            "  WHERE C.ID_Chef = ? " +
            "    AND EXTRACT(YEAR  FROM SP.Data)::int = ? " +
            "    AND EXTRACT(MONTH FROM SP.Data)::int = ? " +
            "  GROUP BY SP.ID_SessionePratica " +
            ") rc";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idChef);
            ps.setInt(2, year);
            ps.setInt(3, month);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("MediaRicette");
        } catch (SQLException e) {
            System.err.println("Errore media ricette: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }
}
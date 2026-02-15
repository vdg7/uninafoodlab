package it.uninafoodlab.app;

import it.uninafoodlab.dao.DatabaseConnection;

/**
 * Classe principale dell'applicazione UninaFoodLab.
 * Entry point del programma.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   UninaFoodLab - Sistema Gestione Corsi");
        System.out.println("==============================================\n");
        
        // Test connessione database
        System.out.println("Test connessione database...");
        boolean connected = DatabaseConnection.testConnection();
        
        if (connected) {
            System.out.println("\n✓ Sistema pronto all'uso!");
            
            // TODO: Avviare GUI
            // javax.swing.SwingUtilities.invokeLater(() -> {
            //     new MainController();
            // });
            
        } else {
            System.err.println("\n✗ Impossibile avviare l'applicazione.");
            System.err.println("  Controlla che:");
            System.err.println("  1. MySQL sia avviato");
            System.err.println("  2. Il database 'UninaFoodLab_App' esista");
            System.err.println("  3. Le credenziali siano corrette");
        }
    }
}
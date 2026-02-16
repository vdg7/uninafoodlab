package it.uninafoodlab.view;

import javax.swing.*;
import java.awt.*;


public abstract class BasePanel extends JPanel {
    
    public BasePanel() {}
    
    /**
     * Costruttore con layout personalizzato.
     * @param layout Il layout manager da utilizzare
     */
    public BasePanel(LayoutManager layout) {
        super(layout);
    }
    
    // ==================== GESTIONE MESSAGGI ====================
    
    
    public void showError(String message) {
        showMessage(message, "Errore", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showError(String message, String title) {
        showMessage(message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public void showSuccess(String message) {
        showMessage(message, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showSuccess(String message, String title) {
        showMessage(message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showWarning(String message) {
        showMessage(message, "Attenzione", JOptionPane.WARNING_MESSAGE);
    }
    

    public void showWarning(String message, String title) {
        showMessage(message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    public boolean showConfirmation(String message) {
        return showConfirmation(message, "Conferma");
    }
    
    /**
     * @return true se l'utente ha confermato, false altrimenti
     */
    public boolean showConfirmation(String message, String title) {
        int result = JOptionPane.showConfirmDialog(
            this,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
    
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(
            this,
            message,
            title,
            messageType
        );
    }
    
    // ==================== METODI DI UTILITÀ ====================
    
    protected void setPadding(int padding) {
        setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    }
    
    protected void setPadding(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }
}

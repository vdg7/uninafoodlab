package it.uninafoodlab.model.domain;

import it.uninafoodlab.model.enums.TipoModifica;
import java.time.LocalDateTime;

/**
 * Classe che rappresenta una Notifica inviata da uno chef.
 * Corrisponde alla tabella Notifica nel database.
 */
public class Notifica {
    
    private int idNotifica;
    private int idChef;
    private Integer idCorso;
    private String titolo;
    private String messaggio;
    private TipoModifica tipoModifica;
    private boolean isGlobale;
    private LocalDateTime dataCreazione;
    
    /**
     * Costruttore vuoto.
     */
    public Notifica() {}
    
    /**
     * Costruttore per creazione nuova notifica (senza ID e data).
     * 
     * @param idChef ID dello chef che crea la notifica
     * @param idCorso ID del corso (null se globale)
     * @param titolo titolo della notifica
     * @param messaggio testo del messaggio
     * @param tipoModifica tipo di modifica
     * @param isGlobale true se notifica per tutti i corsi
     */
    public Notifica(int idChef, Integer idCorso, String titolo, String messaggio,
                    TipoModifica tipoModifica, boolean isGlobale) {
        this.idChef = idChef;
        this.idCorso = idCorso;
        this.titolo = titolo;
        this.messaggio = messaggio;
        this.tipoModifica = tipoModifica;
        this.isGlobale = isGlobale;
    }
    
    /**
     * Costruttore completo.
     * 
     * @param idNotifica ID della notifica
     * @param idChef ID dello chef
     * @param idCorso ID del corso (null se globale)
     * @param titolo titolo della notifica
     * @param messaggio testo del messaggio
     * @param tipoModifica tipo di modifica
     * @param isGlobale true se notifica globale
     * @param dataCreazione timestamp di creazione
     */
    public Notifica(int idNotifica, int idChef, Integer idCorso, String titolo,
                    String messaggio, TipoModifica tipoModifica, boolean isGlobale,
                    LocalDateTime dataCreazione) {
        this.idNotifica = idNotifica;
        this.idChef = idChef;
        this.idCorso = idCorso;
        this.titolo = titolo;
        this.messaggio = messaggio;
        this.tipoModifica = tipoModifica;
        this.isGlobale = isGlobale;
        this.dataCreazione = dataCreazione;
    }
    
    // ==================== GETTERS E SETTERS ====================
    
    public int getIdNotifica() {
        return idNotifica;
    }
    
    public void setIdNotifica(int idNotifica) {
        this.idNotifica = idNotifica;
    }
    
    public int getIdChef() {
        return idChef;
    }
    
    public void setIdChef(int idChef) {
        this.idChef = idChef;
    }
    
    public Integer getIdCorso() {
        return idCorso;
    }
    
    public void setIdCorso(Integer idCorso) {
        this.idCorso = idCorso;
    }
    
    public String getTitolo() {
        return titolo;
    }
    
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    
    public String getMessaggio() {
        return messaggio;
    }
    
    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
    
    public TipoModifica getTipoModifica() {
        return tipoModifica;
    }
    
    public void setTipoModifica(TipoModifica tipoModifica) {
        this.tipoModifica = tipoModifica;
    }
    
    public boolean isGlobale() {
        return isGlobale;
    }
    
    public void setGlobale(boolean globale) {
        isGlobale = globale;
    }
    
    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }
    
    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }
    
    // ==================== METODI UTILITY ====================
    
    @Override
    public String toString() {
        return "Notifica{" +
                "idNotifica=" + idNotifica +
                ", titolo='" + titolo + '\'' +
                ", tipoModifica=" + tipoModifica +
                ", isGlobale=" + isGlobale +
                ", dataCreazione=" + dataCreazione +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Notifica notifica = (Notifica) o;
        return idNotifica == notifica.idNotifica;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idNotifica);
    }
}
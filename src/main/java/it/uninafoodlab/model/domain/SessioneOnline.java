package it.uninafoodlab.model.domain;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Classe che rappresenta una Sessione Online di un corso.
 * Corrisponde alla tabella SessioneOnline nel database.
 */
public class SessioneOnline {
    
    private int idSessioneOnline;
    private LocalDate data;
    private LocalTime ora;
    private int durata;  // Durata in minuti
    private String link;
    private int idCorso;
    
    /**
     * Costruttore vuoto.
     */
    public SessioneOnline() {}
    
    /**
     * Costruttore per creazione nuova sessione (senza ID).
     * 
     * @param data data della sessione
     * @param ora orario della sessione
     * @param durata durata in minuti
     * @param link link per partecipare online
     * @param idCorso ID del corso a cui appartiene
     */
    public SessioneOnline(LocalDate data, LocalTime ora, int durata, String link, int idCorso) {
        this.data = data;
        this.ora = ora;
        this.durata = durata;
        this.link = link;
        this.idCorso = idCorso;
    }
    
    /**
     * Costruttore completo.
     * 
     * @param idSessioneOnline ID della sessione
     * @param data data della sessione
     * @param ora ora della sessione
     * @param durata durata in minuti
     * @param link link per partecipare online
     * @param idCorso ID del corso a cui appartiene
     */
    public SessioneOnline(int idSessioneOnline, LocalDate data, LocalTime ora, int durata, String link, int idCorso) {
        this.idSessioneOnline = idSessioneOnline;
        this.data = data;
        this.ora = ora;
        this.durata = durata;
        this.link = link;
        this.idCorso = idCorso;
    }
    
    // ==================== GETTERS E SETTERS ====================
    
    public int getIdSessioneOnline() {
        return idSessioneOnline;
    }
    public void setIdSessioneOnline(int idSessioneOnline) {
        this.idSessioneOnline = idSessioneOnline;
    }
    
    
    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }
    
    
    public LocalTime getOra() {
    	return ora;
    }
    public void setOra(LocalTime ora) {
    	this.ora = ora;
    }
    
    
    public int getDurata() {
        return durata;
    }
    public void setDurata(int durata) {
        this.durata = durata;
    }
    
    
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    
    
    public int getIdCorso() {
        return idCorso;
    }   
    public void setIdCorso(int idCorso) {
        this.idCorso = idCorso;
    }
    
    // ==================== METODI UTILITY ====================
    
    @Override
    public String toString() {
        return "SessioneOnline{" +
                "idSessioneOnline=" + idSessioneOnline +
                ", data=" + data +
                ", ora=" + ora +
                ", durata=" + durata +
                ", link='" + link + '\'' +
                ", idCorso=" + idCorso +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        SessioneOnline that = (SessioneOnline) o;
        return idSessioneOnline == that.idSessioneOnline;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idSessioneOnline);
    }
}
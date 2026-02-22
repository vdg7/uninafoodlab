package it.uninafoodlab.model.domain;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Classe che rappresenta una Sessione Pratica di un corso.
 * Corrisponde alla tabella SessionePratica nel database.
 */
public class SessionePratica {
    
    private int idSessionePratica;
    private LocalDate data;
    private LocalTime ora;
    private int durata;  // Durata in minuti
    private String luogo;
    private int idCorso;
    
    /**
     * Costruttore vuoto.
     */
    public SessionePratica() {}
    
    /**
     * Costruttore per creazione nuova sessione (senza ID).
     * 
     * @param data data della sessione
     * @parma ora orario in cui si svolge la sessione
     * @param durata durata in minuti
     * @param luogo luogo dove si svolge
     * @param idCorso ID del corso a cui appartiene
     */
    public SessionePratica(LocalDate data, LocalTime ora, int durata, String luogo, int idCorso) {
        this.data = data;
        this.ora = ora;
        this.durata = durata;
        this.luogo = luogo;
        this.idCorso = idCorso;
    }
    
    /**
     * Costruttore completo.
     * 
     * @param idSessionePratica ID della sessione
     * @param data data della sessione
     * @parma ora orario in cui si svolge la sessione
     * @param durata durata in minuti
     * @param luogo luogo dove si svolge
     * @param idCorso ID del corso a cui appartiene
     */
    public SessionePratica(int idSessionePratica, LocalDate data, LocalTime ora, int durata, String luogo, int idCorso) {
        this.idSessionePratica = idSessionePratica;
        this.data = data;
        this.ora = ora;
        this.durata = durata;
        this.luogo = luogo;
        this.idCorso = idCorso;
    }
    
    // ==================== GETTERS E SETTERS ====================
    
    public int getIdSessionePratica() {
        return idSessionePratica;
    }
    public void setIdSessionePratica(int idSessionePratica) {
        this.idSessionePratica = idSessionePratica;
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
    
    
    public String getLuogo() {
        return luogo;
    }
    public void setLuogo(String luogo) {
        this.luogo = luogo;
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
        return "SessionePratica{" +
                "idSessionePratica=" + idSessionePratica +
                ", data=" + data +
                ", ora=" + ora +
                ", durata=" + durata +
                ", luogo='" + luogo + '\'' +
                ", idCorso=" + idCorso +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        SessionePratica that = (SessionePratica) o;
        return idSessionePratica == that.idSessionePratica;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idSessionePratica);
    }
}
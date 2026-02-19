package it.uninafoodlab.model.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Classe domain per rappresentare un'iscrizione di uno studente a un corso.
 */
public class Iscrizione {
    
    private int idStudente;
    private int idCorso;
    private LocalDateTime dataIscrizione;
    
    // Costruttore vuoto
    public Iscrizione() {}
    
    /**
     * Costruttore senza data (la data impostata dal DB con DEFAULT)
     * 
     * @param idStudente ID dello Studente che si iscrive al corso
     * @param idCorso ID del corso a cui si iscrive lo studente
     */
    public Iscrizione(int idStudente, int idCorso) {
        this.idStudente = idStudente;
        this.idCorso = idCorso;
    }
    
    /**
     * 
     * Costruttore completo
     * 
     * @param idStudente ID dello Studente che si iscrive al corso
     * @param idCorso ID del corso a cui si iscrive lo studente
     * @param dataIscrizione data nella quale lo studente si iscrive al corso
     */ 
    public Iscrizione(int idStudente, int idCorso, LocalDateTime dataIscrizione) {
        this.idStudente = idStudente;
        this.idCorso = idCorso;
        this.dataIscrizione = dataIscrizione;
    }
    
 
    // ==================== GETTERS E SETTERS ====================
    
    public int getIdStudente() {
        return idStudente;
    }
    
    public void setIdStudente(int idStudente) {
        this.idStudente = idStudente;
    }
    
    public int getIdCorso() {
        return idCorso;
    }
    
    public void setIdCorso(int idCorso) {
        this.idCorso = idCorso;
    }
    
    public LocalDateTime getDataIscrizione() {
        return dataIscrizione;
    }
    
    public void setDataIscrizione(LocalDateTime dataIscrizione) {
        this.dataIscrizione = dataIscrizione;
    }
    
    // ==================== METODI UTILITY ====================
    
    @Override
    public String toString() {
        return String.format("Iscrizione[studente=%d, corso=%d, data=%s]",
                idStudente, idCorso, dataIscrizione);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Iscrizione that = (Iscrizione) o;
        return idStudente == that.idStudente && idCorso == that.idCorso;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idStudente, idCorso);
    }
}
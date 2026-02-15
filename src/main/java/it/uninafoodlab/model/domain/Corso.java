package it.uninafoodlab.model.domain;

import it.uninafoodlab.model.enums.Categoria;
import java.time.LocalDate;

/**
 * Classe che rappresenta un Corso nel sistema.
 * Corrisponde alla tabella Corso nel database.
 */
public class Corso {
    
    private int idCorso;
    private String titolo;
    private Categoria categoria;
    private LocalDate dataInizio;
    private int frequenza;  // Giorni tra le sessioni
    private int numeroSessioni;
    private int idChef;
    
    /**
     * Costruttore vuoto.
     */
    public Corso() {}
    
    /**
     * Costruttore per creazione nuovo corso (senza ID).
     * 
     * @param titolo titolo del corso
     * @param categoria categoria del corso
     * @param dataInizio data di inizio del corso
     * @param frequenza frequenza delle sessioni (giorni)
     * @param numeroSessioni numero totale di sessioni
     * @param idChef ID dello chef che gestisce il corso
     */
    public Corso(String titolo, Categoria categoria, LocalDate dataInizio, 
                 int frequenza, int numeroSessioni, int idChef) {
        this.titolo = titolo;
        this.categoria = categoria;
        this.dataInizio = dataInizio;
        this.frequenza = frequenza;
        this.numeroSessioni = numeroSessioni;
        this.idChef = idChef;
    }
    
    /**
     * Costruttore completo.
     * 
     * @param idCorso ID del corso
     * @param titolo titolo del corso
     * @param categoria categoria del corso
     * @param dataInizio data di inizio del corso
     * @param frequenza frequenza delle sessioni (giorni)
     * @param numeroSessioni numero totale di sessioni
     * @param idChef ID dello chef che gestisce il corso
     */
    public Corso(int idCorso, String titolo, Categoria categoria, LocalDate dataInizio,
                 int frequenza, int numeroSessioni, int idChef) {
        this.idCorso = idCorso;
        this.titolo = titolo;
        this.categoria = categoria;
        this.dataInizio = dataInizio;
        this.frequenza = frequenza;
        this.numeroSessioni = numeroSessioni;
        this.idChef = idChef;
    }
    
    // ==================== GETTERS E SETTERS ====================
    
    public int getIdCorso() {
        return idCorso;
    }
    
    public void setIdCorso(int idCorso) {
        this.idCorso = idCorso;
    }
    
    public String getTitolo() {
        return titolo;
    }
    
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    public LocalDate getDataInizio() {
        return dataInizio;
    }
    
    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }
    
    public int getFrequenza() {
        return frequenza;
    }
    
    public void setFrequenza(int frequenza) {
        this.frequenza = frequenza;
    }
    
    public int getNumeroSessioni() {
        return numeroSessioni;
    }
    
    public void setNumeroSessioni(int numeroSessioni) {
        this.numeroSessioni = numeroSessioni;
    }
    
    public int getIdChef() {
        return idChef;
    }
    
    public void setIdChef(int idChef) {
        this.idChef = idChef;
    }
    
    // ==================== METODI UTILITY ====================
    
    @Override
    public String toString() {
        return "Corso{" +
                "idCorso=" + idCorso +
                ", titolo='" + titolo + '\'' +
                ", categoria=" + categoria +
                ", dataInizio=" + dataInizio +
                ", frequenza=" + frequenza +
                ", numeroSessioni=" + numeroSessioni +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Corso corso = (Corso) o;
        return idCorso == corso.idCorso;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idCorso);
    }
}
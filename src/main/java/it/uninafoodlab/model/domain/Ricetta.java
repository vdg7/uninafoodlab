package it.uninafoodlab.model.domain;

/**
 * Classe che rappresenta una Ricetta da realizzare in una sessione pratica.
 * Corrisponde alla tabella Ricetta nel database.
 */
public class Ricetta {
    
    private int idRicetta;
    private String nome;
    private String ingredienti;
    private int idSessionePratica;
    
    /**
     * Costruttore vuoto.
     */
    public Ricetta() {}
    
    /**
     * Costruttore per creazione nuova ricetta (senza ID).
     * 
     * @param nome nome della ricetta
     * @param ingredienti ingredienti in formato testuale
     * @param idSessionePratica ID della sessione pratica
     */
    public Ricetta(String nome, String ingredienti, int idSessionePratica) {
        this.nome = nome;
        this.ingredienti = ingredienti;
        this.idSessionePratica = idSessionePratica;
    }
    
    /**
     * Costruttore completo.
     * 
     * @param idRicetta ID della ricetta
     * @param nome nome della ricetta
     * @param ingredienti ingredienti in formato testuale
     * @param idSessionePratica ID della sessione pratica
     */
    public Ricetta(int idRicetta, String nome, String ingredienti, int idSessionePratica) {
        this.idRicetta = idRicetta;
        this.nome = nome;
        this.ingredienti = ingredienti;
        this.idSessionePratica = idSessionePratica;
    }
    
    // ==================== GETTERS E SETTERS ====================
    
    public int getIdRicetta() {
        return idRicetta;
    }
    
    public void setIdRicetta(int idRicetta) {
        this.idRicetta = idRicetta;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getIngredienti() {
        return ingredienti;
    }
    
    public void setIngredienti(String ingredienti) {
        this.ingredienti = ingredienti;
    }
    
    public int getIdSessionePratica() {
        return idSessionePratica;
    }
    
    public void setIdSessionePratica(int idSessionePratica) {
        this.idSessionePratica = idSessionePratica;
    }
    
    // ==================== METODI UTILITY ====================
    
    @Override
    public String toString() {
        return "Ricetta{" +
                "idRicetta=" + idRicetta +
                ", nome='" + nome + '\'' +
                ", idSessionePratica=" + idSessionePratica +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Ricetta ricetta = (Ricetta) o;
        return idRicetta == ricetta.idRicetta;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idRicetta);
    }
}
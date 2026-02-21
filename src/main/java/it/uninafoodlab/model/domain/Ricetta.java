package it.uninafoodlab.model.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe che rappresenta una Ricetta da realizzare in una sessione pratica.
 * Corrisponde alla tabella Ricetta nel database.
 */
public class Ricetta {
    
    private int idRicetta;
    private String nome;
    private int idSessionePratica;
    private List<RicettaIngrediente> ingredienti;
    
    /**
     * Costruttore vuoto.
     */
    public Ricetta() {
        this.ingredienti = new ArrayList<>();
    }
    
    /**
     * Costruttore per creazione nuova ricetta (senza ID).
     * 
     * @param nome nome della ricetta
     * @param idSessionePratica ID della sessione pratica
     */
    public Ricetta(String nome, int idSessionePratica) {
        this.nome = nome;
        this.idSessionePratica = idSessionePratica;
        this.ingredienti = new ArrayList<>();
    }
    
    /**
     * Costruttore completo.
     * 
     * @param idRicetta ID della ricetta
     * @param nome nome della ricetta
     * @param idSessionePratica ID della sessione pratica
     */
    public Ricetta(int idRicetta, String nome, int idSessionePratica) {
        this.idRicetta = idRicetta;
        this.nome = nome;
        this.idSessionePratica = idSessionePratica;
        this.ingredienti = new ArrayList<>();
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
    
    public int getIdSessionePratica() {
        return idSessionePratica;
    }
    
    public List<RicettaIngrediente> getIngredienti() {
        return ingredienti;
    }
    
    public void setIdSessionePratica(int idSessionePratica) {
        this.idSessionePratica = idSessionePratica;
    }
    
    public void setIngredienti(List<RicettaIngrediente> byRicetta) {
    	 this.ingredienti = byRicetta;
	}
    
    public void addIngrediente(RicettaIngrediente ingrediente) {
        this.ingredienti.add(ingrediente);
    }
    
    // ==================== METODI UTILITY ====================
    
    @Override
    public String toString() {
        return String.format("Ricetta[id=%d, nome='%s', sessione=%d, ingredienti=%d]",
                idRicetta, nome, idSessionePratica, ingredienti.size());
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
        return Objects.hash(idRicetta);
    }


}
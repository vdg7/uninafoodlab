package it.uninafoodlab.model.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Classe domain per rappresentare la relazione N:M tra Ricetta e Ingrediente.
 * Ogni record indica che una ricetta usa un certo ingrediente con una specifica quantità.
 */
public class RicettaIngrediente {
    
    private int idRicetta;
    private int idIngrediente;
    private BigDecimal quantita;
    
    // Riferimenti agli oggetti completi (opzionali, per comodità)
    private Ricetta ricetta;
    private Ingrediente ingrediente;
    
    // Costruttore vuoto
    public RicettaIngrediente() {}
    
    /**
     * 
     * Costruttore base (solo IDs e quantità)
     * 
     * @param idRicetta ID della ricetta
     * @param idIngrediente ID dell'ingrediente
     * @param quantita quantità per persona dell'ingrediente nell ricetta 
     * */ 
    public RicettaIngrediente(int idRicetta, int idIngrediente, BigDecimal quantita) {
        this.idRicetta = idRicetta;
        this.idIngrediente = idIngrediente;
        this.quantita = quantita;
    }
    
    /**
     * 
     * Costruttore completo (con oggetti Ricetta e Ingrediente)
     * 
     * @param idRicetta ID della ricetta
     * @param idIngrediente ID dell'ingrediente
     * @param quantita quantità per persona dell'ingrediente nell ricetta 
     * @param ricetta ricetta
     * @param ingrediente ingrediente
     * 
     */ 
    public RicettaIngrediente(int idRicetta, int idIngrediente, BigDecimal quantita,
                              Ricetta ricetta, Ingrediente ingrediente) {
        this.idRicetta = idRicetta;
        this.idIngrediente = idIngrediente;
        this.quantita = quantita;
        this.ricetta = ricetta;
        this.ingrediente = ingrediente;
    }
    
    
    // ==================== GETTERS E SETTERS ====================
    
    public int getIdRicetta() {
        return idRicetta;
    }
    
    public void setIdRicetta(int idRicetta) {
        this.idRicetta = idRicetta;
    }
    
    public int getIdIngrediente() {
        return idIngrediente;
    }
    
    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }
    
    public BigDecimal getQuantita() {
        return quantita;
    }
    
    public void setQuantita(BigDecimal quantita) {
        this.quantita = quantita;
    }
    
    public Ricetta getRicetta() {
        return ricetta;
    }
    
    public void setRicetta(Ricetta ricetta) {
        this.ricetta = ricetta;
    }
    
    public Ingrediente getIngrediente() {
        return ingrediente;
    }
    
    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }
    
    
    // ==================== METODI UTILITY ====================
    
    @Override
    public String toString() {
        String ingredienteStr = (ingrediente != null) 
            ? ingrediente.getNome() + " " + quantita + " " + ingrediente.getUnitaMisura()
            : "Ingrediente ID " + idIngrediente + ": " + quantita;
        
        return String.format("RicettaIngrediente[ricetta=%d, %s]", idRicetta, ingredienteStr);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RicettaIngrediente that = (RicettaIngrediente) o;
        return idRicetta == that.idRicetta && idIngrediente == that.idIngrediente;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idRicetta, idIngrediente);
    }
}
package it.uninafoodlab.model.domain;

import java.util.Objects;

/**
 * Classe domain per rappresentare un Ingrediente.
 */
public class Ingrediente {
    
    private int idIngrediente;
    private String nome;
    private String categoria;  // Es: Verdura, Carne, Latticini
    private String unitaMisura; // Es: g, kg, ml, l, unità
    
    // Costruttore vuoto
    public Ingrediente() {}
    
    /**
     * Costruttore senza ID (per insert)
     * 
     *@param nome nome dell'ingrediente
     *@param categoria categoria dell'ingrediente
     *@param unitaMisura unità di misura dell'ingrediente 
     */ 
    public Ingrediente(String nome, String categoria, String unitaMisura) {
        this.nome = nome;
        this.categoria = categoria;
        this.unitaMisura = unitaMisura;
    }
    
    /**
     * Costruttore completo (per select)
     * 
     *@param idIngrediente ID dell'ingrediente
     *@param nome nome dell'ingrediente
     *@param categoria categoria dell'ingrediente
     *@param unitaMisura unità di misura dell'ingrediente 
     * 
     * */ 
    public Ingrediente(int idIngrediente, String nome, String categoria, String unitaMisura) {
        this.idIngrediente = idIngrediente;
        this.nome = nome;
        this.categoria = categoria;
        this.unitaMisura = unitaMisura;
    }
    
    // Getters e Setters
    public int getIdIngrediente() {
        return idIngrediente;
    }
    
    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getUnitaMisura() {
        return unitaMisura;
    }
    
    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }
    
    @Override
    public String toString() {
        return String.format("Ingrediente[id=%d, nome='%s', categoria='%s', unità='%s']",
                idIngrediente, nome, categoria, unitaMisura);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingrediente that = (Ingrediente) o;
        return idIngrediente == that.idIngrediente;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idIngrediente);
    }
}

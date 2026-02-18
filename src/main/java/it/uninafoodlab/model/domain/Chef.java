package it.uninafoodlab.model.domain;

/**
 * Classe che rappresenta uno Chef nel sistema.
 * Corrisponde alla tabella Chef nel database.
 */
public class Chef {
    
    private int idChef;
    private String nome;
    private String email;
    private String password;
    private int anniEsperienza;
    private int numeroSpecializzazioni;

    public Chef() {}
    
    /**
     * Costruttore per login (senza ID).
     * 
     * @param email email dello chef
     * @param password password dello chef
     */
    public Chef(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    /**
     * Costruttore completo.
     * 
     * @param idChef ID dello chef
     * @param nome nome dello chef
     * @param email email dello chef
     * @param password password dello chef
     * @param anniEsperienza anni di esperienza dello chef
     * @param numeroSpecializzazioni numero di specializzazioni conseguite dallo chef
     */
    public Chef(int idChef, String nome, String email, String password, int anniEsperienza, int numeroSpecializzazioni) {
        this.idChef = idChef;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.anniEsperienza = anniEsperienza;
        this.numeroSpecializzazioni = numeroSpecializzazioni;
    }
    
    // ==================== GETTERS E SETTERS ====================
    
    public int getIdChef() {
        return idChef;
    }
    
    public void setIdChef(int idChef) {
        this.idChef = idChef;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getAnniEsperienza() {
    	return anniEsperienza;
    }
    
    public void setAnniEsperienza(int anniEsperienza) {
    	this.anniEsperienza = anniEsperienza;
    }
    
    public int getNumeroSpecializzazioni() {
    	return numeroSpecializzazioni;
    }
    
    public void setNumeroSpecializzazioni(int numeroSpecializzazioni) {
    	this.numeroSpecializzazioni = numeroSpecializzazioni;
    }
    
    // ==================== METODI UTILITY ====================
    
    @Override
    public String toString() {
        return "Chef{" +
                "idChef=" + idChef +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", anni di esperienza='" + anniEsperienza + '\'' +
                ", specializzazioni ='" + numeroSpecializzazioni + '\'' +
                '}';

    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Chef chef = (Chef) o;
        return idChef == chef.idChef;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idChef);
    }
}
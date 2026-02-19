package it.uninafoodlab.model.domain;


/**
 * Classe che rappresenta uno studente nel sistema
 * Corrsiponde alla tabella Studente nel database
 */
public class Studente {

	 private int idStudente;
	 private String nome;
	 private String email;
	 private String password;
	 private String matricola;
	 
	 /**
	  * Costruttore vuoto
	  */
	 public Studente() {}
	 
	 /**
	  * Costruttore completo
	  * 
	  *@param idStudente ID dello studente
	  *@param nome nome dello studente
	  *@param email email dello studente
	  *@param password password dello studente
	  *@param matricola numero di matricola dello studente 
	  */
	 public Studente(int idStudente, String nome, String matricola, String email, String password) {
		 this.idStudente = idStudente;
		 this.nome = nome;		 
		 this.matricola = matricola;
		 this.email = email;
		 this.password = password;

	 }
	 
	// ==================== GETTERS E SETTERS ====================
	 
	public int getIdStudente() {
		return idStudente;
	}
	
	public void setIdStudente(int idStudente) {
		this.idStudente = idStudente;
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
	
	public String getMatricola() {
		return matricola;
	}
	
	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}
	
	// ==================== METODI UTILITY ====================
    
	@Override
	public String toString() {
		return "Studente{" +
				"idStudente=" + idStudente +
				", nome=" + nome + '\'' +
				", email=" + email + '\'' +
				", matricola=" + matricola +
				'}';
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Studente studente = (Studente) o;
        return idStudente == studente.idStudente;
    }
	
	@Override
    public int hashCode() {
        return Integer.hashCode(idStudente);
    }
}

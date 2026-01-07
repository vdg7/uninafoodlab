package it.uninafoodlab.domain;

public class Chef {
	private int idChef;
	private String nome;
	private String email;
	private String password;
	
	public Chef() {}

	public Chef(int idChef, String nome, String email, String password) {
		this.idChef = idChef;
		this.nome = nome;
		this.email = email;
		this.password = password;
	}

	// Getters e Setters
	public int getIdChef() { return idChef; }
	public void setIdChef(int idChef) { this.idChef = idChef; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	@Override
	public String toString() {
	    return "Chef [idChef=" + idChef + ", nome=" + nome + ", email=" + email + "]";
	}
}

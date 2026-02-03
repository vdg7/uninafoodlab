package it.uninafoodlab.domain;

public class Ricetta {

    private int idRicetta;
    private String nome;
    private String ingredienti;
    private int idSessionePratica;

    public Ricetta() {}

    public Ricetta(int idRicetta, String nome, String ingredienti, int idSessionePratica) {
        this.idRicetta = idRicetta;
        this.nome = nome;
        this.ingredienti = ingredienti;
        this.idSessionePratica = idSessionePratica;
    }

    public int getIdRicetta() { return idRicetta; }
    public void setIdRicetta(int idRicetta) { this.idRicetta = idRicetta; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getIngredienti() { return ingredienti; }
    public void setIngredienti(String Ingredienti) { this.ingredienti = Ingredienti; }

    public int getIdSessionePratica() { return idSessionePratica; }
    public void setIdSessionePratica(int idSessionePratica) { this.idSessionePratica = idSessionePratica; }

    @Override
    public String toString() {
        return nome;
    }
}

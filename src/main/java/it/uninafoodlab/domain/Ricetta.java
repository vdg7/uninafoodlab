package it.uninafoodlab.domain;

public class Ricetta {

    private int idRicetta;
    private String nome;
    private String descrizione;
    private int idCorso;

    public Ricetta() {}

    public Ricetta(int idRicetta, String nome, String descrizione, int idCorso) {
        this.idRicetta = idRicetta;
        this.nome = nome;
        this.descrizione = descrizione;
        this.idCorso = idCorso;
    }

    public int getIdRicetta() { return idRicetta; }
    public void setIdRicetta(int idRicetta) { this.idRicetta = idRicetta; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public int getIdCorso() { return idCorso; }
    public void setIdCorso(int idCorso) { this.idCorso = idCorso; }

    @Override
    public String toString() {
        return nome;
    }
}

package it.uninafoodlab.domain;

public class Ricetta {

    private int idRicetta;
    private String nome;
    private String descrizione;

    public Ricetta() {}

    public int getIdRicetta() { return idRicetta; }
    public void setIdRicetta(int idRicetta) { this.idRicetta = idRicetta; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    @Override
    public String toString() {
        return "Ricetta [id=" + idRicetta + ", nome=" + nome + "]";
    }
}

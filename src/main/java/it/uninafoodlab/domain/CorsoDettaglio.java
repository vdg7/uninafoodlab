package it.uninafoodlab.domain;

public class CorsoDettaglio {

    private String titolo;
    private String tema;
    private String nomeChef;
    private int numeroSessioni;

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }

    public String getNomeChef() { return nomeChef; }
    public void setNomeChef(String nomeChef) { this.nomeChef = nomeChef; }

    public int getNumeroSessioni() { return numeroSessioni; }
    public void setNumeroSessioni(int numeroSessioni) {
        this.numeroSessioni = numeroSessioni;
    }

    @Override
    public String toString() {
        return titolo + " | " + tema + " | " + nomeChef +
               " | sessioni: " + numeroSessioni;
    }
}

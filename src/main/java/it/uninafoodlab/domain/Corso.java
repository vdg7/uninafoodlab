package it.uninafoodlab.domain;

import java.time.LocalDate;

public class Corso {

	private int idCorso;
	private String titolo;
	private String tema;
	private LocalDate dataInizio;
	private int frequenzaSettimanale;
	private int idChef;
	
	public Corso() {}

    public Corso(String titolo, String tema, LocalDate dataInizio,
                 int frequenzaSettimanale, int idChef) {
        this.titolo = titolo;
        this.tema = tema;
        this.dataInizio = dataInizio;
        this.frequenzaSettimanale = frequenzaSettimanale;
        this.idChef = idChef;
    }
    
    public int getIdCorso() { return idCorso; }
    public void setIdCorso(int idCorso) { this.idCorso = idCorso; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }

    public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }

    public int getFrequenzaSettimanale() { return frequenzaSettimanale; }
    public void setFrequenzaSettimanale(int frequenzaSettimanale) {
        this.frequenzaSettimanale = frequenzaSettimanale;
    }
    
    public int getIdChef() { return idChef; }
    public void setIdChef(int idChef) { this.idChef = idChef; }

    @Override
    public String toString() {
        return "Corso [titolo=" + titolo + ", tema=" + tema +
               ", dataInizio=" + dataInizio + "]";
    }
}

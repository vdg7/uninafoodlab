package it.uninafoodlab.domain;

import java.time.LocalDate;

public abstract class Sessione {

    protected int idSessione;
    protected LocalDate data;
    protected int idCorso;

    public Sessione() {}

    public Sessione(LocalDate data, int idCorso) {
        this.data = data;
        this.idCorso = idCorso;
    }

    public int getIdSessione() { return idSessione; }
    public void setIdSessione(int idSessione) { this.idSessione = idSessione; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public int getIdCorso() { return idCorso; }
    public void setIdCorso(int idCorso) { this.idCorso = idCorso; }
}

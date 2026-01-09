package it.uninafoodlab.domain;

import java.time.LocalDate;

public class SessioneOnline {

    private int idSessione;
    private LocalDate data;
    private int durata;
    private String link;
    private int idCorso;

    public SessioneOnline() {}

    public int getIdSessione() { return idSessione; }
    public void setIdSessione(int idSessione) { this.idSessione = idSessione; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public int getDurata() { return durata; }
    public void setDurata(int durata) { this.durata = durata; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public int getIdCorso() { return idCorso; }
    public void setIdCorso(int idCorso) { this.idCorso = idCorso; }

    @Override
    public String toString() {
        return "SessioneOnline [data=" + data + ", durata=" + durata + " min, link=" + link + "]";
    }
}


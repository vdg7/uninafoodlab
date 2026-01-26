package it.uninafoodlab.domain;

import java.time.LocalDate;

public class SessionePratica {

    private int idSessione;
    private LocalDate data;
    private int durata;
    private String luogo;
    private int idCorso;

    public SessionePratica() {}

    public int getIdSessione() { return idSessione; }
    public void setIdSessione(int idSessione) { this.idSessione = idSessione; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public int getDurata() { return durata; }
    public void setDurata(int durata) { this.durata = durata; }

    public String getLuogo() { return luogo; }
    public void setLuogo(String laboratorio) { this.luogo = laboratorio; }

    public int getIdCorso() { return idCorso; }
    public void setIdCorso(int idCorso) { this.idCorso = idCorso; }

    @Override
    public String toString() {
        return "SessionePratica [data=" + data + ", durata=" + durata + " min, lab=" + luogo + "]";
    }
}

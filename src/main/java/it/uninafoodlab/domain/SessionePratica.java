package it.uninafoodlab.domain;

import java.time.LocalDate;

public class SessionePratica extends Sessione {

    public SessionePratica() {}

    public SessionePratica(LocalDate data, int idCorso) {
        super(data, idCorso);
    }
}

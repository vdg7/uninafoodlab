package it.uninafoodlab.domain;

import java.time.LocalDate;

public class SessioneOnline extends Sessione {

    public SessioneOnline() {}

    public SessioneOnline(LocalDate data, int idCorso) {
        super(data, idCorso);
    }
}

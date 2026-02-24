-- ============================================================
--  UninaFoodLab — Script di creazione database
--  Università degli Studi di Napoli Federico II
-- ============================================================

-- Rimuove le tabelle se già esistono (ordine inverso rispetto alle FK)
DROP TABLE IF EXISTS Notifica          CASCADE;
DROP TABLE IF EXISTS Ricetta_Ingrediente CASCADE;
DROP TABLE IF EXISTS Ricetta           CASCADE;
DROP TABLE IF EXISTS Iscrizione        CASCADE;
DROP TABLE IF EXISTS SessionePratica   CASCADE;
DROP TABLE IF EXISTS SessioneOnline    CASCADE;
DROP TABLE IF EXISTS Ingrediente       CASCADE;
DROP TABLE IF EXISTS Corso             CASCADE;
DROP TABLE IF EXISTS Studente          CASCADE;
DROP TABLE IF EXISTS Chef              CASCADE;

/* ============================================================
   ENUM TYPES
   ============================================================ */
CREATE TYPE categoria_enum AS ENUM (
    'TRADIZIONE',
    'INNOVAZIONE', 
    'PASTICCERIA',
    'CUCINA ASIATICA',
    'PRIMI',
    'SECONDI',
    'MARE',
    'CARNE',
    'VEGETARIANO',
    'PANIFICAZIONE'
);

CREATE TYPE tipo_modifica_enum AS ENUM (
    'CAMBIO_DATA',
    'CAMBIO_ORA',
    'CANCELLAZIONE',
    'ALTRO'
);

-- ============================================================
--  CHEF
-- ============================================================
CREATE TABLE Chef (
    ID_Chef                 SERIAL          PRIMARY KEY,
    Nome                    VARCHAR(100)    NOT NULL,
    Email                   VARCHAR(150)    NOT NULL UNIQUE,
    Password                VARCHAR(255)    NOT NULL,
    AnniEsperienza          INTEGER         NOT NULL DEFAULT 0 CHECK (AnniEsperienza >= 0),
    NumeroSpecializzazioni  INTEGER         NOT NULL DEFAULT 0 CHECK (NumeroSpecializzazioni >= 0)
);

-- ============================================================
--  STUDENTE
-- ============================================================
CREATE TABLE Studente (
    ID_Studente SERIAL          PRIMARY KEY,
    Nome        VARCHAR(100)    NOT NULL,
    Matricola   VARCHAR(20)     NOT NULL UNIQUE,
    Email       VARCHAR(150)    NOT NULL UNIQUE,
    Password    VARCHAR(255)    NOT NULL
);

-- ============================================================
--  CORSO
-- ============================================================
CREATE TABLE Corso (
    ID_Corso        SERIAL          PRIMARY KEY,
    Titolo          VARCHAR(200)    NOT NULL,
    Categoria       categoria_enum     NOT NULL,
    DataInizio      DATE            NOT NULL,
    Frequenza       INTEGER         NOT null CHECK (Frequenza > 0),   -- giorni tra le sessioni
    NumeroSessioni  INTEGER         NOT null CHECK (NumeroSessioni > 0),
    ID_Chef         INTEGER         NOT NULL REFERENCES Chef(ID_Chef) ON DELETE cascade,
    CONSTRAINT ValidCourseDate CHECK (DataInizio >= CURRENT_DATE)
);

-- ============================================================
--  SESSIONE ONLINE
-- ============================================================
CREATE TABLE SessioneOnline (
    ID_SessioneOnline   SERIAL          PRIMARY KEY,
    Data                DATE            NOT NULL,
    Ora                 TIME           NOT NULL,     
    Durata              INTEGER         NOT NULL,   -- minuti
    Link                VARCHAR(500)    NOT NULL,
    ID_Corso            INTEGER         NOT NULL REFERENCES Corso(ID_Corso) ON DELETE CASCADE
);

-- ============================================================
--  SESSIONE PRATICA
-- ============================================================
CREATE TABLE SessionePratica (
    ID_SessionePratica  SERIAL          PRIMARY KEY,
    Data                DATE            NOT NULL,
    Ora                 TIME           NOT NULL,
    Durata              INTEGER         NOT NULL,   -- minuti
    Luogo               VARCHAR(300)    NOT NULL,
    ID_Corso            INTEGER         NOT NULL REFERENCES Corso(ID_Corso) ON DELETE CASCADE
);

-- ============================================================
--  INGREDIENTE
-- ============================================================
CREATE TABLE Ingrediente (
    ID_Ingrediente  SERIAL          PRIMARY KEY,
    Nome            VARCHAR(150)    NOT NULL UNIQUE,
    Categoria       VARCHAR(100)    NOT NULL,
    UnitaMisura     VARCHAR(20)     NOT NULL    -- es: g, kg, ml, l, unità
);

-- ============================================================
--  RICETTA
-- ============================================================
CREATE TABLE Ricetta (
    ID_Ricetta          SERIAL          PRIMARY KEY,
    Nome                VARCHAR(200)    NOT NULL,
    ID_SessionePratica  INTEGER         NOT NULL REFERENCES SessionePratica(ID_SessionePratica) ON DELETE CASCADE
);

-- ============================================================
--  RICETTA_INGREDIENTE  (tabella ponte N:M con quantità)
-- ============================================================
CREATE TABLE Ricetta_Ingrediente (
    ID_Ricetta      INTEGER         NOT NULL REFERENCES Ricetta(ID_Ricetta)      ON DELETE CASCADE,
    ID_Ingrediente  INTEGER         NOT NULL REFERENCES Ingrediente(ID_Ingrediente) ON DELETE CASCADE,
    Quantita        NUMERIC(10, 3)  NOT NULL,
    PRIMARY KEY (ID_Ricetta, ID_Ingrediente)
);

-- ============================================================
--  ISCRIZIONE  (tabella ponte N:M Studente ↔ Corso)
-- ============================================================
CREATE TABLE Iscrizione (
    ID_Studente     INTEGER         NOT NULL REFERENCES Studente(ID_Studente) ON DELETE CASCADE,
    ID_Corso        INTEGER         NOT NULL REFERENCES Corso(ID_Corso)       ON DELETE CASCADE,
    DataIscrizione  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (ID_Studente, ID_Corso)
);

-- ============================================================
--  NOTIFICA
-- ============================================================
CREATE TABLE Notifica (
    ID_Notifica     SERIAL          PRIMARY KEY,
    ID_Chef         INTEGER         NOT NULL REFERENCES Chef(ID_Chef) ON DELETE CASCADE,
    ID_Corso        INTEGER         REFERENCES Corso(ID_Corso) ON DELETE SET NULL,
    Titolo          VARCHAR(300)    NOT NULL,
    Messaggio       TEXT            NOT NULL,
    TipoModifica    tipo_modifica_enum     NOT NULL,
    IsGlobale       BOOLEAN         NOT NULL DEFAULT FALSE,
    DataCreazione   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
    CHECK (
    (IsGlobale = TRUE AND ID_Corso IS NULL)
    OR
    (IsGlobale = FALSE AND ID_Corso IS NOT NULL)
	)
);

/* ============================================================
   POPOLAMENTO
   ============================================================ */

-- Chef
INSERT INTO Chef (Nome, Email, Password, AnniEsperienza, NumeroSpecializzazioni) VALUES
('Gennaro Esposito', 'g.esposito@foodlab.it', 'chef123', 15, 3),
('Antonino Cannavacciuolo', 'a.cannavacciuolo@foodlab.it', 'chef456', 20, 5),

-- Studenti
INSERT INTO Studente (Nome, Matricola, Email, Password) VALUES
('Mario Rossi', 'N86001234', 'm.rossi@studenti.unina.it', 'stud123'),
('Laura Bianchi', 'N86002345', 'l.bianchi@studenti.unina.it', 'stud456'),
('Giuseppe Verdi', 'N86003456', 'g.verdi@studenti.unina.it', 'stud789'),
('Anna Neri', 'N86004567', 'a.neri@studenti.unina.it', 'stud101'),
('Paolo Ferrari', 'N86005678', 'p.ferrari@studenti.unina.it', 'stud202');

-- Ingredienti
INSERT INTO Ingrediente (Nome, Categoria, UnitaMisura) VALUES
('Pasta', 'Cereali', 'g'),
('Riso Carnaroli', 'Cereali', 'g'),
('Farina 00', 'Cereali', 'g'),
('Carne mista macinata', 'Carne', 'g'),
('Pollo', 'Carne', 'g'),
('Manzo', 'Carne', 'g'),
('Salmone', 'Pesce', 'g'),
('Branzino', 'Pesce', 'g'),
('Gamberi', 'Pesce', 'g'),
('Pomodori pelati', 'Verdura', 'g'),
('Melanzane', 'Verdura', 'kg'),
('Zucchine', 'Verdura', 'g'),
('Cipolla', 'Verdura', 'unità'),
('Aglio', 'Verdura', 'spicchi'),
('Mozzarella', 'Latticini', 'g'),
('Parmigiano', 'Latticini', 'g'),
('Ricotta', 'Latticini', 'g'),
('Burro', 'Latticini', 'g'),
('Olio EVO', 'Condimenti', 'ml'),
('Sale', 'Condimenti', 'g'),
('Pepe nero', 'Spezie', 'g'),
('Basilico', 'Erbe', 'foglie'),
('Uova', 'Altro', 'unità'),
('Vino rosso', 'Bevande', 'ml'),
('Lievito', 'Lievitanti', 'g');

-- Corsi
INSERT INTO Corso (Titolo, Categoria, DataInizio, Frequenza, NumeroSessioni, ID_Chef) VALUES
('Cucina Tradizionale Napoletana', 'Tradizione', '2026-03-03', 7, 4, 1),
('Pasta Fresca Fatta in Casa', 'Primi', '2026-03-10', 7, 2, 1),
('Pizza Napoletana DOC', 'Panificazione', '2026-03-17', 3, 2, 2),
('Pasticceria Base', 'Pasticceria', '2026-05-05', 3, 3, 2),
('Pasticceria Avanzata', 'Pasticceria', '2026-05-12', 3, 3, 2),

-- Iscrizioni
INSERT INTO Iscrizione (ID_Studente, ID_Corso, DataIscrizione) VALUES
(1, 1, '2026-02-15 10:30:00'),
(1, 3, '2026-02-16 14:20:00'),
(2, 2, '2026-02-17 11:00:00'),
(3, 1, '2026-02-15 15:45:00'),
(4, 5, '2026-04-15 14:00:00'),
(5, 4, '2026-02-20 09:00:00');

-- Sessioni Online
INSERT INTO SessioneOnline (Data, Ora, Durata, Link, ID_Corso) VALUES
('2026-03-03', '12:00', 120, 'https://meet.uninafoodlab.it/tradizione-napoletana-intro', 1),
('2026-03-10', '12:00', 90, 'https://meet.uninafoodlab.it/tradizione-napoletana-teoria', 1),
('2026-03-10', '12:00', 120, 'https://meet.uninafoodlab.it/pasta-fresca-intro', 2),
('2026-03-17', '12:00', 60, 'https://meet.uninafoodlab.it/pizza-storia', 3),
('2026-05-05', '12:00', 90, 'https://meet.uninafoodlab.it/pasticceria-base-intro', 4),
('2026-05-26', '14:00', 90, 'https://meet.uninafoodlab.it/pasticceria-avanzata-intro', 5);


-- Sessioni Pratiche
INSERT INTO SessionePratica (Data, Ora, Durata, Luogo, ID_Corso) VALUES
('2026-03-17', '12:00', 180, 'Laboratorio Cucina - Via Toledo 123, Napoli', 1),
('2026-03-24', '12:00', 180, 'Laboratorio Cucina - Via Toledo 123, Napoli', 1),
('2026-03-17', '12:00', 200, 'Laboratorio Pasta - Corso Umberto I, Napoli', 2),
('2026-03-20', '12:00', 240, 'Pizzeria Didattica - Spaccanapoli, Napoli', 3),
('2026-05-08', '12:00', 180, 'Laboratorio Pasticceria - Via Toledo 456, Napoli', 4),
('2026-05-11', '12:00', 180, 'Laboratorio Pasticceria - Via Toledo 456, Napoli', 4),
('2026-05-29', '14:00', 200, 'Laboratorio Pasticceria - Via Toledo 456, Napoli', 5),
('2026-06-01', '12:00', 200, 'Laboratorio Pasticceria - Via Toledo 456, Napoli', 5);

-- Ricette (senza ingredienti testuali)
INSERT INTO Ricetta (Nome, ID_SessionePratica) VALUES
('Ragù Napoletano', 1),
('Parmigiana di Melanzane', 2),
('Gnocchi alla Sorrentina', 3),
('Ravioli Ricotta e Spinaci', 3),
('Pizza Margherita', 4),
('Pizza Marinara', 4),
('Croissant Base', 5),
('Biscotti al Burro', 6),
('Torta Sacher', 7),
('Macaron Francesi', 8),
('Croquembouche', 8);

-- ============================================================
-- RICETTA_INGREDIENTE
-- ============================================================

-- 1. Ragù Napoletano
INSERT INTO Ricetta_Ingrediente (ID_Ricetta, ID_Ingrediente, Quantita) VALUES
(1, 1, 100),  -- Pasta
(1, 4, 150),  -- Carne mista macinata
(1, 10, 100), -- Pomodori pelati
(1, 13, 1),   -- Cipolla
(1, 19, 10),  -- Burro
(1, 24, 50);  -- Uova

-- 2. Parmigiana di Melanzane
INSERT INTO Ricetta_Ingrediente (ID_Ricetta, ID_Ingrediente, Quantita) VALUES
(2, 11, 0.3),  -- Melanzane in kg
(2, 15, 100),  -- Mozzarella
(2, 16, 50),   -- Parmigiano
(2, 10, 100),  -- Pomodori pelati
(2, 22, 5);    -- Basilico

-- 3. Gnocchi alla Sorrentina
INSERT INTO Ricetta_Ingrediente (ID_Ricetta, ID_Ingrediente, Quantita) VALUES
(3, 3, 150),  -- Farina 00
(3, 24, 1),   -- Uova
(3, 15, 50),  -- Mozzarella
(3, 16, 30),  -- Parmigiano
(3, 10, 100); -- Pomodori pelati

-- 4. Ravioli Ricotta e Zucchine
INSERT INTO Ricetta_Ingrediente (ID_Ricetta, ID_Ingrediente, Quantita) VALUES
(4, 3, 150),  -- Farina 00
(4, 24, 1),   -- Uova
(4, 17, 80),  -- Ricotta
(4, 12, 50),  -- Zucchine
(4, 16, 20);  -- Parmigiano

-- 5. Pizza Margherita
INSERT INTO Ricetta_Ingrediente (ID_Ricetta, ID_Ingrediente, Quantita) VALUES
(5, 3, 200),  -- Farina 00
(5, 24, 1),   -- Uova
(5, 15, 100), -- Mozzarella
(5, 10, 50),  -- Pomodori pelati
(5, 22, 5),   -- Basilico
(5, 19, 5);   -- Burro

-- 6. Pizza Marinara
INSERT INTO Ricetta_Ingrediente (ID_Ricetta, ID_Ingrediente, Quantita) VALUES
(6, 3, 200),  -- Farina 00
(6, 10, 50),  -- Pomodori pelati
(6, 13, 1),   -- Cipolla
(6, 14, 2),   -- Aglio
(6, 19, 5),   -- Burro/olio
(6, 22, 5);   -- Basilico

-- 7. Croissant Base
INSERT INTO Ricetta_Ingrediente (ID_Ricetta, ID_Ingrediente, Quantita) VALUES
(7, 3, 50),  -- Farina 00
(7, 18, 20), -- Burro
(7, 24, 1),  -- Uova
(7, 19, 1);  -- Sale

-- 8. Biscotti al Burro
INSERT INTO Ricetta_Ingrediente (ID_Ricetta, ID_Ingrediente, Quantita) VALUES
(8, 3, 40),  -- Farina 00
(8, 18, 20), -- Burro
(8, 24, 1),  -- Uova
(8, 19, 1);  -- Sale

-- 9. Torta Sacher
INSERT INTO Ricetta_Ingrediente (ID_Ricetta, ID_Ingrediente, Quantita) VALUES
(9, 3, 60),  -- Farina 00
(9, 18, 30), -- Burro
(9, 24, 2),  -- Uova
(9, 22, 50); -- Basilico/Cioccolato 

-- 10. Macaron Francesi
INSERT INTO Ricetta_Ingrediente (ID_Ricetta, ID_Ingrediente, Quantita) VALUES
(10, 24, 2), -- Uova
(10, 18, 20), -- Burro
(10, 19, 1); -- Sale

-- 11. Croquembouche
INSERT INTO Ricetta_Ingrediente (ID_Ricetta, ID_Ingrediente, Quantita) VALUES
(11, 18, 40), -- Burro
(11, 24, 3),  -- Uova
(11, 19, 2);  -- Sale 

-- Partecipazioni
INSERT INTO Partecipazione (ID_Studente, ID_SessionePratica, Confermata) VALUES
(1, 1, TRUE), (1, 2, TRUE), (2, 3, TRUE), (3, 1, TRUE), (4, 7, TRUE), (4, 8, TRUE), (5, 5, TRUE), (5, 6, TRUE);
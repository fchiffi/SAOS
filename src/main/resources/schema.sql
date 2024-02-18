--Creazione tabella RUOLO su H2
CREATE TABLE RUOLO (ruolo_id INT AUTO_INCREMENT PRIMARY KEY,ruolo_descrizione VARCHAR(30) NOT NULL);

--Creazione tabella UTENTE su H2
CREATE TABLE UTENTE (utente_id INT AUTO_INCREMENT PRIMARY KEY,
utente_nome VARCHAR(30) NOT NULL,
utente_cognome VARCHAR(50) NOT NULL,
utente_user_name VARCHAR(30) NOT NULL,
utente_password VARCHAR(60) NOT NULL,
utente_data_creazione VARCHAR(10) NULL,
utente_attivo BOOLEAN NOT NULL,
utente_id_ruolo INT NOT NULL,
FOREIGN KEY (utente_id_ruolo) REFERENCES RUOLO(ruolo_id));

--Creazione tabella LIBRO su H2
CREATE TABLE LIBRO (libro_id INT AUTO_INCREMENT  PRIMARY KEY,
libro_titolo VARCHAR(50) NOT NULL,
libro_commento VARCHAR(150) NOT NULL,
libro_data_creazione VARCHAR(10) NOT NULL,
libro_attivo BOOLEAN NOT NULL,
libro_id_utente INT NOT NULL,
FOREIGN KEY (libro_id_utente) REFERENCES UTENTE(utente_id));  

--Creazione vista LIBRO EXTENDED su H2
CREATE VIEW LIBRO_EXTENDED AS 
select libro_id, libro_titolo, libro_commento, libro_data_creazione, libro_id_utente,
utente_nome AS libro_utente_nome, utente_cognome AS libro_utente_cognome, 
utente_user_name AS libro_utente_user_name
FROM LIBRO JOIN UTENTE ON libro_id_utente = utente_id
WHERE libro_attivo = TRUE;

--Creazione vista UTENTE EXTENDED su H2
CREATE VIEW UTENTE_EXTENDED AS 
select utente_id, utente_nome, utente_cognome, utente_user_name, utente_password,
ruolo_descrizione AS utente_ruolo
FROM UTENTE JOIN RUOLO ON utente_id_ruolo = ruolo_id
WHERE utente_attivo = TRUE


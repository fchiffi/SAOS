--Dati per la tabella RUOLO
INSERT INTO RUOLO (ruolo_descrizione) VALUES ('VIEWER');  
INSERT INTO RUOLO (ruolo_descrizione) VALUES ('USER');  
INSERT INTO RUOLO (ruolo_descrizione) VALUES ('ADMIN');  

--Dati per la tabella UTENTE
--user name: mrossi, password: test01
INSERT INTO UTENTE (utente_nome, utente_cognome, utente_user_name, utente_password, utente_data_creazione, utente_attivo, utente_id_ruolo)
VALUES ('Mario', 'Rossi', 'mrossi', '$2a$10$YRVdpymeAF17wg.UbM7FrOL8KDzIi/QaYzjzm6P7/yqk7uegUuH3.', '12/02/2024', TRUE, 1);  
--user name: gverdi, password: test02
INSERT INTO UTENTE (utente_nome, utente_cognome, utente_user_name, utente_password, utente_data_creazione, utente_attivo, utente_id_ruolo)
VALUES ('Giuseppe', 'Verdi', 'gverdi', '$2a$10$OlCOZ4EZ/0tla9H17KDWnubzTGwkH61cQ5toCSg/ljBG1nrTKsKyW', '13/02/2024', TRUE, 2); 
--user name: aneri, password: test03
INSERT INTO UTENTE (utente_nome, utente_cognome, utente_user_name, utente_password, utente_data_creazione, utente_attivo, utente_id_ruolo)
VALUES ('Antonio', 'Neri', 'aneri', '$2a$10$09lKcObTiokbmm991xcvPuzPOzDeBkFOAOM5F.mfkWXYr4Qj1T6/m', '12/02/2024', TRUE, 3); 

--Dati per la tabella LIBRO
INSERT INTO LIBRO (libro_titolo, libro_commento, libro_data_creazione, libro_attivo, libro_id_utente)
VALUES ('Dune', 'Veramente molto bello', '09/02/2024', TRUE, 3); 
INSERT INTO LIBRO (libro_titolo, libro_commento, libro_data_creazione, libro_attivo, libro_id_utente)
VALUES ('Messia di Dune', 'Bello ma non come il primo', '10/02/2024', TRUE, 2); 
INSERT INTO LIBRO (libro_titolo, libro_commento, libro_data_creazione, libro_attivo, libro_id_utente)
VALUES ('Figli di Dune', 'Molto confuso', '11/02/2024', TRUE, 2); 
INSERT INTO LIBRO (libro_titolo, libro_commento, libro_data_creazione, libro_attivo, libro_id_utente)
VALUES ('Imperatore dio di Dune', 'Quasi bello come il primo', '12/02/2024', TRUE, 2); 
INSERT INTO LIBRO (libro_titolo, libro_commento, libro_data_creazione, libro_attivo, libro_id_utente)
VALUES ('Eretici di Dune', 'Non si capisce dove vada a parare', '12/02/2024', TRUE, 2); 
INSERT INTO LIBRO (libro_titolo, libro_commento, libro_data_creazione, libro_attivo, libro_id_utente)
VALUES ('Rifondazione di Dune', 'Senza conclusione', '15/02/2024', TRUE, 3); 
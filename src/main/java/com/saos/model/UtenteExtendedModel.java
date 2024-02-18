package com.saos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//Entità per la vista UTENTE_EXTENDED
//Rispetto a UTENTE, acquisisce anche dettagli sui ruoli
//In generale sono presenti solo metodi GET poichè la vista è in sola lettura
@Entity
@Table(name = "UTENTE_EXTENDED")

public class UtenteExtendedModel {

	@Id
	private int utenteId;

	@Column(name = "utente_nome")
	private String utenteNome;

	@Column(name = "utente_cognome")
	private String utenteCognome;

	@Column(name = "utente_user_name")
	private String utenteUserName;

	@Column(name = "utente_password")
	private String utentePassword;

	@Column(name = "utente_ruolo")
	private String utenteRuolo;

	public int getUtenteId() {
		return utenteId;
	}

	public String getUtenteNome() {
		return utenteNome;
	}

	public String getUtenteCognome() {
		return utenteCognome;
	}

	public String getUtenteUserName() {
		return utenteUserName;
	}

	public String getUtentePassword() {
		return utentePassword;
	}

	public String getUtenteRuolo() {
		return utenteRuolo;
	}

}

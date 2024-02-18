package com.saos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//Entità per la tabella UTENTE
@Entity
@Table(name = "UTENTE")
public class UtenteModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int utenteId;

	@Column(name = "utente_nome")
	private String utenteNome;

	@Column(name = "utente_cognome")
	private String utenteCognome;

	@Column(name = "utente_user_name")
	private String utenteUserName;

	@Column(name = "utente_password")
	private String utentePassword;

	@Column(name = "utente_data_creazione")
	private String utenteDataCreazione;

	@Column(name = "utente_attivo")
	private Boolean utenteAttivo;

	@Column(name = "utente_id_ruolo")
	private int utenteIdRuolo;

	public int getUtenteId() {
		return utenteId;
	}

	public void setUtenteId(int utenteId) {
		this.utenteId = utenteId;
	}

	public String getUtenteNome() {
		return utenteNome;
	}

	public void setUtenteNome(String utenteNome) {
		this.utenteNome = utenteNome;
	}

	public String getUtenteCognome() {
		return utenteCognome;
	}

	public void setUtenteCognome(String utenteCognome) {
		this.utenteCognome = utenteCognome;
	}

	public String getUtenteUserName() {
		return utenteUserName;
	}

	public void setUtenteUserName(String utenteUserName) {
		this.utenteUserName = utenteUserName;
	}

	public String getUtentePassword() {
		return utentePassword;
	}

	public void setUtentePassword(String utentePassword) {
		this.utentePassword = utentePassword;
	}

	public String getUtenteDataCreazione() {
		return utenteDataCreazione;
	}

	public void setUtenteDataCreazione(String utenteDataCreazione) {
		this.utenteDataCreazione = utenteDataCreazione;
	}

	public Boolean getUtenteAttivo() {
		return utenteAttivo;
	}

	public void setUtenteAttivo(Boolean utenteAttivo) {
		this.utenteAttivo = utenteAttivo;
	}

	public int getUtenteIdRuolo() {
		return utenteIdRuolo;
	}

	public void setUtenteIdRuolo(int utenteIdRuolo) {
		this.utenteIdRuolo = utenteIdRuolo;
	}

}

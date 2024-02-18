package com.saos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//Entità per la vista LIBRO_EXTENDED
//Rispetto a LIBRO, acquisisce anche dati di anagrafica
//In generale sono presenti solo metodi GET poichè la vista è in sola lettura
//Tranne per LibroID che può essere annullato per la logica di visualizzazione
@Entity
@Table(name = "LIBRO_EXTENDED")
public class LibroExtendedModel {

	@Id
	private Integer libroId;

	@Column(name = "libro_titolo")
	private String libroTitolo;

	@Column(name = "libro_commento")
	private String libroCommento;

	@Column(name = "libro_data_creazione")
	private String libroDataCreazione;

	@Column(name = "libro_id_utente")
	private Integer libroIdUtente;

	@Column(name = "libro_utente_nome")
	private String libroUtenteNome;

	@Column(name = "libro_utente_cognome")
	private String libroUtenteCognome;
	
	@Column(name = "libro_utente_user_name")
	private String libroUtenteUserName;

	public String getLibroUtenteUserName() {
		return libroUtenteUserName;
	}

	public Integer getLibroId() {
		return libroId;
	}

	public void setLibroId(Integer libroId) {
		this.libroId = libroId;
	}

	public String getLibroTitolo() {
		return libroTitolo;
	}

	public String getLibroCommento() {
		return libroCommento;
	}

	public String getLibroDataCreazione() {
		return libroDataCreazione;
	}

	public int getLibroIdUtente() {
		return libroIdUtente;
	}

	public String getLibroUtenteNome() {
		return libroUtenteNome;
	}

	public String getLibroUtenteCognome() {
		return libroUtenteCognome;
	}
	

}

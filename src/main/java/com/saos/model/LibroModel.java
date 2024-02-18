package com.saos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//Entità per la tabella LIBRO
@Entity
@Table(name = "LIBRO")
public class LibroModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int libroId;

	@Column(name = "libro_titolo")
	private String libroTitolo;

	@Column(name = "libro_commento")
	private String libroCommento;

	@Column(name = "libro_data_creazione")
	private String libroDataCreazione;

	@Column(name = "libro_attivo")
	private Boolean libroAttivo;

	@Column(name = "libro_id_utente")
	private int libroIdUtente;

	public int getLibroId() {
		return libroId;
	}

	public void setLibroId(int libroId) {
		this.libroId = libroId;
	}

	public String getLibroTitolo() {
		return libroTitolo;
	}

	public void setLibroTitolo(String libroTitolo) {
		this.libroTitolo = libroTitolo;
	}

	public String getLibroCommento() {
		return libroCommento;
	}

	public void setLibroCommento(String libroCommento) {
		this.libroCommento = libroCommento;
	}

	public String getLibroDataCreazione() {
		return libroDataCreazione;
	}

	public void setLibroDataCreazione(String libroDataCreazione) {
		this.libroDataCreazione = libroDataCreazione;
	}

	public Boolean getLibroAttivo() {
		return libroAttivo;
	}

	public void setLibroAttivo(Boolean libroAttivo) {
		this.libroAttivo = libroAttivo;
	}

	public int getLibroIdUtente() {
		return libroIdUtente;
	}

	public void setLibroIdUtente(int libroIdUtente) {
		this.libroIdUtente = libroIdUtente;
	}

}

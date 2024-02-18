package com.saos.service.interfaces;

import com.saos.model.UtenteModel;

//Interfaccia implementata per consentire l'accesso alla tabella UTENTE
public interface UtenteService {

	UtenteModel findByUtenteUserName(String user);

}

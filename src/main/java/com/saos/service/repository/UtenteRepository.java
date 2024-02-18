package com.saos.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.saos.model.UtenteModel;

//Interfaccia implementata per consentire l'accesso alla sorgente dati per la tabella UTENTE
public interface UtenteRepository extends JpaRepository<UtenteModel, Integer> {

	UtenteModel findByUtenteUserName(String user);

}

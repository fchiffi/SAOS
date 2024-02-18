package com.saos.service.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.saos.model.UtenteExtendedModel;

//Interfaccia implementata per consentire l'accesso alla sorgente dati per la vista UTENTE_EXTENDED
public interface UtenteExtendedRepository extends JpaRepository<UtenteExtendedModel, Integer> {

	Optional<UtenteExtendedModel> findByUtenteUserName(String user);

}

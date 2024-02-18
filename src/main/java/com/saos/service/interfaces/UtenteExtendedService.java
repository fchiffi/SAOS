package com.saos.service.interfaces;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.saos.model.UtenteExtendedModel;

//Interfaccia implementata per consentire l'accesso alla vista UTENTE_EXTENDED
public interface UtenteExtendedService {

	UtenteExtendedModel findByUtenteUserName(String username) throws UsernameNotFoundException;

}

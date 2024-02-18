package com.saos.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.saos.model.UtenteModel;
import com.saos.service.interfaces.UtenteService;
import com.saos.service.repository.UtenteRepository;

//Classe implementata per consentire l'accesso alla tabella UTENTE
@Service
@Transactional
public class UtenteServiceImpl implements UtenteService {

	private final UtenteRepository utenteRepository;

	UtenteServiceImpl(UtenteRepository utenteRepository) {
		this.utenteRepository = utenteRepository;
	}

	@Override
	public UtenteModel findByUtenteUserName(String user) {
		return this.utenteRepository.findByUtenteUserName(user);
	}

}

package com.saos.service.implementation;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.saos.model.LibroExtendedModel;
import com.saos.service.interfaces.LibroExtendedService;
import com.saos.service.repository.LibroExtendedRepository;

//Classe implementata per consentire l'accesso alla vista LIBRO_EXTENDED
@Service
@Transactional
public class LibroExtendedServiceImpl implements LibroExtendedService {

	private final LibroExtendedRepository libroExtendedRepository;

	LibroExtendedServiceImpl(LibroExtendedRepository libroExtendedRepository) {
		this.libroExtendedRepository = libroExtendedRepository;
	}

	@Override
	public List<LibroExtendedModel> getAllLibroExtended() {
		return this.libroExtendedRepository.findAll();
	}

	@Override
	public LibroExtendedModel getLibroExtendedByLibroId(int libroId) {
		return this.libroExtendedRepository.findByLibroId(libroId);
	}

}

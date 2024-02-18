package com.saos.service.implementation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.saos.model.LibroModel;
import com.saos.service.interfaces.LibroService;
import com.saos.service.repository.LibroRepository;

//Classe implementata per consentire l'accesso alla tabella LIBRO
@Service
@Transactional
public class LibroServiceImpl implements LibroService {

	private final LibroRepository libroRepository;

	LibroServiceImpl(LibroRepository libroRepository) {
		this.libroRepository = libroRepository;
	}

	@Override
	public List<LibroModel> getAllLibro() {
		return this.libroRepository.findAll();
	}

	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	@Override
	public LibroModel createLibro(LibroModel libroModel) {

		libroModel.setLibroAttivo(Boolean.TRUE);

		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		libroModel.setLibroDataCreazione(LocalDate.now().format(pattern));

		return libroRepository.save(libroModel);
	}

	public LibroModel getLibroByLibroId(int libroId) {

		return libroRepository.getLibroByLibroId(libroId);
	}

	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public void deleteLibro(int libroId) {

		LibroModel libroModel = getLibroByLibroId(libroId);

		libroModel.setLibroAttivo(Boolean.FALSE);

		libroRepository.save(libroModel);

	}

}

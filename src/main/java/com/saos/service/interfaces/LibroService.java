package com.saos.service.interfaces;

import java.util.List;
import com.saos.model.LibroModel;

//Interfaccia implementata per consentire l'accesso alla tabella LIBRO
public interface LibroService {

	List<LibroModel> getAllLibro();

	LibroModel createLibro(LibroModel libroModel);

	LibroModel getLibroByLibroId(int libroId);

	void deleteLibro(int libroId);

}

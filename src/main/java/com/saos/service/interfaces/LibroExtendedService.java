package com.saos.service.interfaces;

import java.util.List;
import com.saos.model.LibroExtendedModel;

//Interfaccia implementata per consentire l'accesso alla vista LIBRO_EXTENDED
public interface LibroExtendedService {

	List<LibroExtendedModel> getAllLibroExtended();

	LibroExtendedModel getLibroExtendedByLibroId(int libroId);

}

package com.saos.service.repository;

import org.springframework.data.jpa.repository.*;
import com.saos.model.LibroExtendedModel;

//Interfaccia implementata per consentire l'accesso alla sorgente dati per la vista LIBRO_EXTENDED
public interface LibroExtendedRepository extends JpaRepository<LibroExtendedModel, Integer> {

	LibroExtendedModel findByLibroId(int libroId);

}

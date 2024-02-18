package com.saos.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.saos.model.LibroModel;

//Interfaccia implementata per consentire l'accesso alla sorgente dati per la tabella LIBRO
public interface LibroRepository extends JpaRepository<LibroModel, Integer>  {

	LibroModel getLibroByLibroId(int libroId);

}

package com.saos.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.saos.model.LibroExtendedModel;
import com.saos.model.LibroModel;
import com.saos.model.UtenteModel;
import com.saos.service.interfaces.LibroExtendedService;
import com.saos.service.interfaces.LibroService;
import com.saos.service.interfaces.UtenteService;

@Controller
public class HomePageController {

	private final LibroService libroService;
	private final LibroExtendedService libroExtendedService;
	private final UtenteService utenteService;

	public HomePageController(LibroService libroService, LibroExtendedService libroExtendedService,
			UtenteService utenteService) {

		this.libroService = libroService;
		this.libroExtendedService = libroExtendedService;
		this.utenteService = utenteService;
	}

	//Controller HomePage.html
	@GetMapping("/Homepage")
	public String HomePage(Model model, Authentication authentication) {

		//Acquisisce anagrafica dell'utente, attraverso lo user name presente nel contesto
		UtenteModel utenteModel = utenteService.findByUtenteUserName(authentication.getName());

		//Verifica che l'utente abbia ruolo USER dal contesto
		Boolean user = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"));

		//Importa l'elenco del libri
		List<LibroExtendedModel> libroExtendedModelArray = new ArrayList<LibroExtendedModel>();

		libroExtendedModelArray = libroExtendedService.getAllLibroExtended();

		//Se l'utente ha ruolo USER pone a NULL gli ID Libro, in modo che non siano visualizzati 
		//su pagina i tasti di cancellazione per i libri di cui l'utente con ruolo USER non è owner
		//L'utente con ruolo ADMIN può cancellare tutti i libri
		if (user) {

			String userName = authentication.getName();

			for (LibroExtendedModel libroExtendedModel : libroExtendedModelArray) {

				if (!libroExtendedModel.getLibroUtenteUserName().equals(userName)) {

					libroExtendedModel.setLibroId(null);

				}
			}

		}

		//Carica l'elenco dei libri opportunamente modificato su pagina
		model.addAttribute("libri", libroExtendedModelArray);

		//Carica nome e cognome dell'utente su pagina
		model.addAttribute("user", utenteModel.getUtenteNome() + " " + utenteModel.getUtenteCognome());

		//Verifica che l'utente abbia ruolo VIEWER dal contesto
		Boolean viewer = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("VIEWER"));

		//Se l'utente ha ruolo VIEWER non permette l'inserimento e la cancellazione
		model.addAttribute("notviewer", !viewer);

		return "HomePage";
	}

	//Controller per inserimento nuovo libro
	@PostMapping("/new_libro")
	public String newLibro(@ModelAttribute LibroModel newLibro, Authentication authentication) {

		//Acquisisce ID utente, attraverso lo user name presente nel contesto
		UtenteModel utenteModel = utenteService.findByUtenteUserName(authentication.getName());

		//Inserisce l' ID utente nel modello del nuovo libro
		newLibro.setLibroIdUtente(utenteModel.getUtenteId());

		//Crea il nuovo libro
		libroService.createLibro(newLibro);

		return "redirect:/Homepage";

	}

	//Controller per cancellazione logica libro esistente
	@PostMapping("/delete/libro/{LibroId}")
	public String deleteLibro(@PathVariable int LibroId, Authentication authentication) {

		//Verifica che l'utente abbia ruolo ADMIN dal contesto
		Boolean admin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));

		
		if (admin) {
			
			//Se l'utente ha ruolo ADMIN può cancellare qualunque libro
			libroService.deleteLibro(LibroId);
			
		} else {
			
			//In caso contrario potrà cancellare solamente i libri di cui è owner
			LibroExtendedModel libroExtendedModel = libroExtendedService.getLibroExtendedByLibroId(LibroId);
			
			if (libroExtendedModel.getLibroUtenteUserName().equals(authentication.getName())) {

				libroService.deleteLibro(LibroId);

			}
		}
		
		//NOTA BENE
		//L'utente VIEWER non potrà comunque accedere a questa funzionalità perchè l'accesso 
		//è inibito sia su SecurityFilterChain che a livello di metodo

		return "redirect:/Homepage";
	}

}

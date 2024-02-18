# Spring Security

## Cosa è Spring Security
 - Spring Security è un framework dedicato alla sicurezza delle    applicazioni Spring sviluppato fin dal 2003.
 - Questo framework non è proprietario ma è stato sviluppato grazie ai contributi dei maggiori    esperti di sicurezza del mondo.   
 - E' mantenuto e sviluppato da una porzione ad esso dedicata del gruppo di sviluppo Spring

Le funzionalità offerte da Spring Security sono estremamente numerose, citiamo le principali:

 - Gestione di autenticazione ed autorizzazione degli utenti basandosi su varie sorgenti:
	 - DB locale
	 - Provider esterni:
		 - LDAP
		 - OAuth2
		 - CAS
		 - Ecc.
 - Servizi di sicurezza a più livelli:
	 - Rotte web ed in generale delle chiamate HTTP.
	 - Metodi di accesso ai dati.
	 - ACL su domini.
 - Ulteriori funzionalità, tra cui ad esempio:
	 - Funzionalità di "Remember me".
	 - Gestione dei cookie.
	 - Gestione delle sessioni.
	 - Gestione dei certificati X.509 per la mutua autenticazione.
	 - CSRF
	 - CORS
	 
## L'applicazione sviluppata
### Architettura
L' applicazione è stata sviluppata seguendo il pattern **Transaction script**.
"Transaction Script" organizza la business logic  in base a procedure in cui ciascuna procedura  gestisce una singola richiesta dalla presentazione.
Tale architettura è sempre stata considerata meno sofisticata e flessibile di un'architettura a più livelli con un modello di dominio ricco, tuttavia è da preferire per applicazioni più semplici, consentendo di non introdurre eccessiva complessità laddove non sia realmente necessario.

### Tecnologie
Le tecnologie utilizzate per l'implementazione sono:
 - Linguaggio: Java (JDK 1.8.0)
 - Framework: Spring Boot 3.2.2
 - Application server: Apache Tomcat 10.1.18 (embedded)
 - Data Base: H2 in memory (embedded)
 - Template: Mustache
 
 ### Funzionalità
 L' applicativo consente:
 - Visualizzazione di:
	 - Titolo di un libro
	 - Commento relativo
	 - Autore
	 - Data inserimento
 - Inserimento di:
	  - Titolo di un libro
	  - Commento relativo
- Eliminazione logica di:
	- Titolo di un libro
	- Commento relativo

Tali funzionalità sono  profilate secondo il ruolo dell'utente, in particolare:
 - **VIEWER**:
	 - Visualizzazione
 - **USER**:
	 - Visualizzazione
	 - Inserimento
	 - Eliminazione dei soli titoli e commenti di cui si è owner
 - **ADMIN**:
	 - Visualizzazione
	 - Inserimento
	 - Eliminazione di tutti i titoli e commenti

### Funzionamento
#### Modello dati
Il modello dati si basa su tre tabelle e due viste:
 - **RUOLO**: tabella contenente i ruoli per gli utenti.
 - **UTENTE**: tabella contenente i dati degli utenti LIBRO: tabella contenente i titoli ed i commenti.
 - **UTENTE_EXTENDED**: vista contenente i dati degli utenti e  dettagli sul ruolo assegnato.
 - **LIBRO_EXTENDED**: vista contenente i dati    su titoli e commenti e dettagli sull'owner.

#### Accesso ai dati
L'accesso ai dati è garantito dalle seguente classi:
 - **Repository**: accesso al data source JPA, una classe per ogni tabella:
	 - LibroExtendedRepository.java
	 - LibroRepository.java
	 - UtenteExtendedRepository.java
	 - UtenteRepository.java

```java
package com.saos.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.saos.model.UtenteModel;

public interface UtenteRepository extends JpaRepository<UtenteModel, Integer> {

	UtenteModel findByUtenteUserName(String user);

}
``` 
 - **Interfacce dei servizi**: interfacce per i servizi per l'accesso ai dati:
   	 - LibroExtendedService.java
	 - LibroService.java
	 - UtenteExtendedService.java
	 - UtenteService.java

```java
package com.saos.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.saos.model.UtenteModel;
import com.saos.service.interfaces.UtenteService;
import com.saos.service.repository.UtenteRepository;

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
``` 
 - **Implementazioni dei servizi**: implementazioni dei servizi per l'accesso ai dati:
   	 - LibroExtendedServiceImpl.java
	 - LibroServiceImpl.java
	 - UtenteServiceImpl.java
	 - JPAUserDetailsServiceImpl.java
	 
Si richiama in particolare l'attenzione sull'ultima classe la cui implementazione è necessaria per consentire l'accesso ai dati degli utenti al framework Spring Security
```java
package com.saos.service.implementation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.saos.model.SecurityUser;
import com.saos.service.repository.UtenteExtendedRepository;

@Configuration
@Service
@Transactional
public class JPAUserDetailsServiceImpl implements UserDetailsService {

	private final UtenteExtendedRepository utenteExtendedRepository;

	public JPAUserDetailsServiceImpl(UtenteExtendedRepository utenteExtendedRepository) {
		this.utenteExtendedRepository = utenteExtendedRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return utenteExtendedRepository.findByUtenteUserName(username).map(SecurityUser::new)
				.orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
``` 
E' rilevante il controllo di accesso sui metodi in base al ruolo dell'utente, tale funzionalità è stata utilizzata per quei metodi che effettuano modifiche sul DB, in particolare nella classe: **LibroServiceImpl.java**:
```java
@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
public  void deleteLibro(int  libroId) {

	LibroModel libroModel = getLibroByLibroId(libroId);

	libroModel.setLibroAttivo(Boolean.FALSE);

	libroRepository.save(libroModel);

}
```
L' accesso al metodo è consentito ai soli utenti aventi ruoli: **USER** ed **ADMIN**.

 - **Implementazioni dei model**: implementazioni delle entità correlate alle tabelle sul DB:
   	 - LibroExtendedModel.java
	 - LibroModel.java
	 - UtenteExtendedModel.java
	 - UtenteModel.java
	 - SecurityUser.java
	 
Si richiama in particolare l'attenzione sull'ultima classe la cui implementazione è necessario per consentire la gestione dei dati degli utenti al framework Spring Security
```java
package com.saos.model;

import java.util.Arrays;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//Entità per 'SecurityUser', model per la  gestione dell'utente specificamente  utilizzata  da
//Spring Security. In essa  vengono  riversati i dati dell'entità UtenteExtendedModel

@SuppressWarnings("serial")
public  class SecurityUser implements UserDetails {

	private UtenteExtendedModel utenteExtendedModel;

	public SecurityUser(UtenteExtendedModel utenteExtendedModel) {
		this.utenteExtendedModel = utenteExtendedModel;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return Arrays.stream(utenteExtendedModel.getUtenteRuolo().split(",")).map(SimpleGrantedAuthority::new).toList();
	}

	@Override
	public String getPassword() {
		return  utenteExtendedModel.getUtentePassword();
	}

	@Override
	public String getUsername() {
	 	return  utenteExtendedModel.getUtenteUserName();
	}

	@Override
	public  boolean isAccountNonExpired() {
		return  true;
	}

	@Override
	public  boolean isAccountNonLocked() {
		return  true;
	}

	@Override
	public  boolean isCredentialsNonExpired() {
		return  true;
	}

	@Override
	public  boolean isEnabled() {
		return  true;
	}

}
``` 
#### Criteri di sicurezza
 - **Implementazioni dei criteri di sicurezza**: implementazione delle classi contenente i criteri di sicurezza che dovranno essere implementati da Spring Security:
   	 - SecurityConfig.java
   	 - CustomAuthenticationFailureHandler.java
```java
package com.saos.security;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.saos.service.implementation.JPAUserDetailsServiceImpl;

//Classe per la configurazione di criteri di sicurezza
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final JPAUserDetailsServiceImpl jpaUserDetailsServiceImpl;

	public SecurityConfig(JPAUserDetailsServiceImpl jpaUserDetailsServiceImpl) {
		this.jpaUserDetailsServiceImpl = jpaUserDetailsServiceImpl;
	}

	@Bean
	SecurityFilterChain configureSecurity(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers(AntPathRequestMatcher.antMatcher("/Homepage")).authenticated() //Consente l'accesso alla HomePage a tutti gli utenti autorizzati
				.requestMatchers(HttpMethod.POST, "/new_libro").hasAnyAuthority("USER", "ADMIN") //Consente la creazione di un nuovo libro solo agli utenti con ruolo USER ed ADMIN
				.requestMatchers(HttpMethod.POST, "/delete/libro/**").hasAnyAuthority("USER", "ADMIN") //Consente l'eliminazione di un nuovo libro solo agli utenti con ruolo USER ed ADMIN
				.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll() //Consente a tutti l'accesso alla console H2
				.anyRequest() //Qualunqe altra richiesta 
				.denyAll()) //è bloccata per tutti
				.userDetailsService(jpaUserDetailsServiceImpl) //Acquisisce da JPA le sorgenti dati per la verifica degli utenti
				.csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))) //Abilita la verifica su CSRF con una eccezione per la console H2
				.formLogin((form) -> form.loginPage("/login") //Abilita il login con una pagina custom
						.defaultSuccessUrl("/Homepage", true) //Pagina da raggiungere in caso di successo nell'autenticazione
					    .failureForwardUrl("/login") //Pagina da raggiungere in caso di fallimento nell'autenticazione
						.failureHandler(authenticationFailureHandler()) //Classe per la gestione degli errori di autenticazione
						.permitAll()) //Tutti hanno accesso al Login
				.headers(headers -> headers.disable()) //Headers disabilitati per l'accesso alla console H2
				.logout((logout) -> logout.logoutSuccessUrl("/login") //In caso di logout invia sempre alla pagina di login
						.deleteCookies("JSESSIONID") //Elimina il cookie di sessione Java
						.invalidateHttpSession(true) //Elimina la sessione http
						.permitAll()); //Il logout è consentito a tutti

		return http.build();

	}
	
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

}

``` 
#### Logica di business
**Classi controller** per la gestione delle varie chiamate derivanti dalle operazioni eseguite dagli utenti:
 - HomePageController.java
 - LoginController.java

Rilevante è la prima classe:
```java
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
public  class HomePageController {

private  final LibroService libroService;
private  final LibroExtendedService libroExtendedService;
private  final UtenteService utenteService;

	public HomePageController(LibroService libroService, LibroExtendedService libroExtendedService, 
	UtenteService utenteService) {

			this.libroService = libroService;
			this.libroExtendedService = libroExtendedService;
			this.utenteService = utenteService;
		}

		//Controller HomePage.html
		@GetMapping("/Homepage")
		public String HomePage(Model model, Authentication authentication) {

			//Acquisisce  anagrafica dell'utente, attraverso  lo user name presente  nel  contesto
			UtenteModel utenteModel = utenteService.findByUtenteUserName(authentication.getName());

			//Verifica  che l'utente abbia  ruolo USER dal  contesto
			Boolean user = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"));

			//Importa l'elenco del  libri
			List<LibroExtendedModel> libroExtendedModelArray  = new ArrayList<LibroExtendedModel>();
			libroExtendedModelArray = libroExtendedService.getAllLibroExtended();

			//Se l'utente ha ruolo USER pone a NULL gli ID Libro, in modo  che non siano  visualizzati
			//su  pagina i tasti  di  cancellazione per i libri  di  cui l'utente con  ruolo USER non è owner
			//L'utente con  ruolo ADMIN può  cancellare  tutti i libri

			if (user) {
				String userName = authentication.getName();
				for (LibroExtendedModel libroExtendedModel : libroExtendedModelArray) {
					if (!libroExtendedModel.getLibroUtenteUserName().equals(userName)) {
						
						libroExtendedModel.setLibroId(null);
					}
				}
		}

		//Carica l'elenco dei  libri  opportunamente  modificato  su  pagina
		model.addAttribute("libri", libroExtendedModelArray);

		//Carica  nome e cognome dell'utente su  pagina
		model.addAttribute("user", utenteModel.getUtenteNome() + " " + utenteModel.getUtenteCognome());

		//Verifica  che l'utente abbia  ruolo VIEWER dal  contesto
		Boolean viewer = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("VIEWER"));

		//Se l'utente ha ruolo VIEWER non permette l'inserimento e la  cancellazione
		model.addAttribute("notviewer", !viewer);

		return  "HomePage";
	}

	//Controller per inserimento  nuovo  libro
	@PostMapping("/new_libro")
	public String newLibro(@ModelAttribute LibroModel newLibro, Authentication authentication) {
	
		//Acquisisce ID utente, attraverso  lo user name presente  nel  contesto
		UtenteModel utenteModel = utenteService.findByUtenteUserName(authentication.getName());

		//Inserisce l' ID utente  nel  modello  del  nuovo  libro
		newLibro.setLibroIdUtente(utenteModel.getUtenteId());

		//Crea  il  nuovo  libro
		libroService.createLibro(newLibro);

		return  "redirect:/Homepage";

	}

	//Controller per cancellazione  logica  libro  esistente
	@PostMapping("/delete/libro/{LibroId}")
	public String deleteLibro(@PathVariable  int  LibroId, Authentication authentication) {

		//Verifica  che l'utente abbia  ruolo ADMIN dal  contesto
		Boolean admin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));

		if (admin) {

			//Se l'utente ha ruolo ADMIN può  cancellare  qualunque  libro
			libroService.deleteLibro(LibroId);

		} else {

		//In caso  contrario  potrà  cancellare  solamente i libri  di  cui è owner
		LibroExtendedModel libroExtendedModel = libroExtendedService.getLibroExtendedByLibroId(LibroId);

		if (libroExtendedModel.getLibroUtenteUserName().equals(authentication.getName())) {

			libroService.deleteLibro(LibroId);
		}

	}

		//NOTA BENE
		//L'utente VIEWER non potrà  comunque  accedere a questa  funzionalità  perchè l'accesso
		//è inibito  sia  su SecurityFilterChain che a livello  di  metodo

		return  "redirect:/Homepage";
	}
}

```
#### Template
I template sono implementati con HTML e Mustache. **Moustache** è descritto come un sistema privo di logica perché privo di istruzioni esplicite sul flusso di controllo, tuttavia esso è di rapido apprendimento e semplice implementazione.
Degno di rilievo nei template, è il codice che consente l'inserimento automatico in ogni form che effettui una chiamata all'applicativo, di token generati casualmente e noti all'applicativo stesso, che consentono di evitare **CSRF** (Cross Site Request Forgery), ovvero il verificarsi di chiamate 'maliziose' da parte di siti terzi non autorizzati.
```html
<form action="/login" method="post">
	<input type="text" name="username" required>
	<input type="password" name="password" placeholder="Password" required>
	<input type="submit" value="Login">
	<input type="hidden" name="{{_csrf.parameterName}}" value="{{_csrf.token}}">
</form>
```

#### Gestione delle chiamate
Possiamo distinguere due tipologie principali di chiamate:
 - Autenticazione
 - Logica di business

L'autenticazione è gestita direttamente da Spring Security e richiede l'implementazione di entità e metodi ben definiti, tutta la configurazione della gestione dell'autenticazione viene effettuata nella classe **SecurityConfig.Java**.

Tutta la logica di business è gestita nella classe **HomePageController.java**.
Tuttavia ogni chiamata effettuata a metodi presenti in quest'ultima classe è mediata da Spring Security mediante le configurazioni presenti nella classe **SecurityConfig.Java**, in dettaglio:

 - L'accesso alla pagina: HomePage.html per i soli utenti autorizzati
 - L'accesso alle funzionalità di creazione ed eliminazione di un commento per alcuni ruoli specifici

Inoltre sono presenti nella logica di business ulteriori controlli per prevenire BFLA (Broken Function Level Authorization).

### Open point
Il presente applicativo si pone come scopo l'esplorazione delle principali funzionalità di Spring Security, **non** sono pertanto state implementate ulteriori misure di sicurezza necessarie in ambiente di produzione:

 1. Abilitazione SSL.
 2. Sanitizzazione dell'input, sia per quanto concerne i testi inseriti, sia per quanto concerne i parametri ricevuti, da effettuarsi ad esempio con Apache Tika.
 3. Verifica di user name e password con Regex.
 4. Gestione delle password con Salt su un DB separato, BCrypt inserisce al contrario il Salt nella stesso campo della password.
 5. Informazioni da fornire all'utente in caso di errore di autenticazione.
 6. Eventuale implementazione di cookie per l'accesso senza autenticazione, funzionalità, tuttavia, che non è necessariamente implementata ovunque.

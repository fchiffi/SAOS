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

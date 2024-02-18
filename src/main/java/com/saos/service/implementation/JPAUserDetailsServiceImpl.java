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

//Classe implementata per consentire a Spring Security l'accesso ai dati utente
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

package com.saos.model;

import java.util.Arrays;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//Entità per 'SecurityUser', model per la gestione dell'utente specificamente utilizzata da
//Spring Security. In essa vengono riversati i dati dell'entità UtenteExtendedModel
@SuppressWarnings("serial")
public class SecurityUser implements UserDetails {

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

		return utenteExtendedModel.getUtentePassword();
	}

	@Override
	public String getUsername() {

		return utenteExtendedModel.getUtenteUserName();
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {

		return true;
	}

}

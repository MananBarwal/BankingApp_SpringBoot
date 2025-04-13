package com.project.bankingApp.Security.UserDetails;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.bankingApp.entity.Account;

public class CustomUserDetails implements UserDetails {
	
	private Account account;
											// Constructor injection
	public CustomUserDetails(Account a) {
		this.account=a;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority (account.getRole().name()));
	}

	@Override
	public String getPassword() {
		return account.getPassword();
	}

	@Override
	public String getUsername() {
		return Long.toString(account.getAccNumber());
	}

}

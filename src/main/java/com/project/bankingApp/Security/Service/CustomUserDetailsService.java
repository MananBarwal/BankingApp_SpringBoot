package com.project.bankingApp.Security.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.bankingApp.Security.UserDetails.CustomUserDetails;
import com.project.bankingApp.entity.Account;
import com.project.bankingApp.repository.AccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	AccountRepository AccountRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		long l = Long.parseLong(username);
		Account a= AccountRepo.findById(l).orElseThrow(() -> new UsernameNotFoundException("DNE"));
		return new CustomUserDetails(a);
	}

}

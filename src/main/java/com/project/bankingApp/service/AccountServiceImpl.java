package com.project.bankingApp.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.bankingApp.Security.UserDetails.CustomUserDetails;
import com.project.bankingApp.dto.AccountDto;
import com.project.bankingApp.dto.BalanceDto;
import com.project.bankingApp.dto.updateDto;
import com.project.bankingApp.entity.Account;
import com.project.bankingApp.entity.EnumforRoles;
import com.project.bankingApp.mapper.AccountMapper;
import com.project.bankingApp.repository.AccountRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountRepository AccountRepo;

	@Autowired
	AccountMapper accountMapper;

	@Autowired
	PasswordEncoder passwordEncoder;
	 
	
	@Override
	@Transactional
	public AccountDto saveAccount(AccountDto a) {
		if (AccountRepo.aadharUsed(a.getAadharNumber()) || AccountRepo.phoneUsed(a.getPhoneNumber())) {
			throw new RuntimeException("USER CAN ONLY HAVE 1 ACCOUNT");
		}
		Account account = accountMapper.dtoToAccount(a); // Convert DTO -> Entity
		account.setPassword(passwordEncoder.encode(a.getPassword())); // Salting + Hashing password
		account.setRole(EnumforRoles.USER);
		account.setActive(true);
		account.setBalance(0);
		
		return accountMapper.accountToDto(AccountRepo.save(account)); // Convert back Entity -> DTO and return
	}

	@Override // Admin
	public Optional<AccountDto> getAccountbyId(long id) {
		return AccountRepo.findById(id).map(accountMapper::accountToDto);
	}

	@Override
	@Transactional
	public AccountDto deactivateAccount(long id) {
		Account account = AccountRepo.findById(id).orElseThrow(() -> new RuntimeException("No such account found"));
		account.setActive(false);
		return accountMapper.accountToDto(AccountRepo.save(account));
	}

	@Override // Admin
	public List<AccountDto> getAllAccounts() {
	    List<Account> accounts = AccountRepo.findAll();
	    return accounts.stream().map(accountMapper::accountToDto).toList();
	}
	
	@Override //Admin
	public Optional<List<AccountDto>> getAccountsbyEmail(String email) {
		Optional<List<Account>> accounts = AccountRepo.findByEmail(email);
		List<AccountDto> l = new ArrayList<AccountDto>();
		if (accounts.isEmpty()){
			return Optional.empty();
		}
		else {
			for (Account a : accounts.get()) {
				l.add(accountMapper.accountToDto(a));
			}
			}
		return Optional.of(l);
	}

	@Override
	@Transactional
	public AccountDto updateAccount( updateDto updatedData) {
		
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		if (a == null || !a.isAuthenticated()) { throw new RuntimeException ("Unauthenticated User");}
		
		CustomUserDetails UserDetails = (CustomUserDetails) a.getPrincipal();
		long accNumber = Long.parseLong(UserDetails.getUsername());
		
		Account existingAccount = AccountRepo.findById(accNumber).orElseThrow(() -> new RuntimeException("Account not found"));
		accountMapper.updateAccountFromDto(updatedData, existingAccount);
		Account savedAccount = AccountRepo.save(existingAccount);
		return accountMapper.accountToDto(savedAccount);
	}
	
	@Override
	public AccountDto editRole(long id, EnumforRoles Role)  //ADMIN
	{ 
		Account a=AccountRepo.findById(id).orElseThrow(() -> new RuntimeException ("Account not found"));
		a.setRole(Role);
		return accountMapper.accountToDto(AccountRepo.save(a));
	}

	@Override
	public BalanceDto checkBalance() {
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		if (a == null || !a.isAuthenticated()) { throw new RuntimeException ("Unauthenticated User");}
		CustomUserDetails UserDetails = (CustomUserDetails) a.getPrincipal();
		long accNumber = Long.parseLong(UserDetails.getUsername());
		Account account= AccountRepo.findById(accNumber).orElseThrow(() -> new RuntimeException ("Account not found"));
		BalanceDto b=accountMapper.accountToBalanceDto(account);
		
		return b;
	}

	
}

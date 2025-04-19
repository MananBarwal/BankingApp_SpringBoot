package com.project.bankingApp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bankingApp.dto.AccountDto;
import com.project.bankingApp.dto.BalanceDto;
import com.project.bankingApp.dto.EditRoledto;
import com.project.bankingApp.dto.updateDto;
import com.project.bankingApp.entity.EnumforRoles;
import com.project.bankingApp.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
	
	private final AccountService a; //constructor dependency injection. 
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/getAccount")
	public ResponseEntity<AccountDto> getAccount(@RequestBody long accNumber){
		return a.getAccountbyId(accNumber).map(accountDto -> ResponseEntity.ok(accountDto)).
				orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping ("/getAllAccounts")
	public ResponseEntity<List<AccountDto>> getAllAccounts(){
		List<AccountDto> accounts=a.getAllAccounts();
		if (accounts.isEmpty()) {
			return ResponseEntity.noContent().build();
		}	 
		return ResponseEntity.ok(accounts);	
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/getAccountbyEmail")
	public ResponseEntity<List<AccountDto>> getAccount(@Valid @RequestBody String email){
		return a.getAccountsbyEmail(email)
				.map(accounts -> ResponseEntity.ok(accounts))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping("/deactivateAccount")
	public ResponseEntity<AccountDto> deactivateAccount(@Valid @RequestBody long accNumber) {
		return ResponseEntity.ok(a.deactivateAccount(accNumber));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping("/editRoles")
	public ResponseEntity<AccountDto> editRoles(@Valid @RequestBody EditRoledto edr) {
	    return ResponseEntity.ok(a.editRole(edr.getId(), edr.getRole()));
	}

	@PutMapping("/updateAccount")
	public ResponseEntity<AccountDto> updateAccount(@Valid @RequestBody updateDto updateDto){
		AccountDto p= a.updateAccount(updateDto);
		System.out.println("Update Account API is called!");
		return ResponseEntity.status(HttpStatus.CREATED).body(p);
	}
	
	@GetMapping("/checkBalance")
	public ResponseEntity<BalanceDto> checkBalance(){
		BalanceDto b=a.checkBalance();
	return ResponseEntity.ok().body(b);
	}
}



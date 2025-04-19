package com.project.bankingApp.service;
import java.util.*;

import org.springframework.stereotype.Service;

import com.project.bankingApp.dto.AccountDto;
import com.project.bankingApp.dto.BalanceDto;
import com.project.bankingApp.dto.TransactionDto;
import com.project.bankingApp.dto.updateDto;
import com.project.bankingApp.entity.EnumforRoles;

@Service
public interface AccountService  {
	public AccountDto saveAccount (AccountDto a);
	Optional<AccountDto>  getAccountbyId(long id);
	AccountDto updateAccount(updateDto b);
	Optional<List<AccountDto>> getAccountsbyEmail(String email);
	public AccountDto deactivateAccount(long id);
	List<AccountDto> getAllAccounts();
	AccountDto editRole (long id, EnumforRoles role);
	BalanceDto checkBalance();
	
	
	
	
	
	
}

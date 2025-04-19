package com.project.bankingApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bankingApp.dto.DWDto;
import com.project.bankingApp.dto.TransactionDto;
import com.project.bankingApp.dto.WithdrawDto;
import com.project.bankingApp.dto.transferDto;
import com.project.bankingApp.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

	@Autowired
	TransactionService t;
	
	@PostMapping("/deposit")
	public ResponseEntity<TransactionDto> deposit(@RequestBody DWDto d){
		TransactionDto td=t.deposit(d);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(td);
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<TransactionDto> withdraw(@RequestBody WithdrawDto d){
		TransactionDto td=t.withdraw(d);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(td);
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<TransactionDto> transfer(@RequestBody transferDto transferDto) {
        TransactionDto td = t.transfer(
                transferDto.getSenderId(),
                transferDto.getReceiverId(),
                transferDto.getAmount()
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(td);
    }
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/viewalltransactions")
	public ResponseEntity<List<TransactionDto>> getAllTransactions(){
		List<TransactionDto> allTransactions= t.getAllTransactions();
		return ResponseEntity.ok(allTransactions);
	}

	@GetMapping("/alltransactionsofUser")
	public ResponseEntity<List<TransactionDto>> getAllTransactionsUser(){
		List<TransactionDto> allTransactions= t.TransactionHistory();
		return ResponseEntity.ok(allTransactions);
	} 
	
	
	
}

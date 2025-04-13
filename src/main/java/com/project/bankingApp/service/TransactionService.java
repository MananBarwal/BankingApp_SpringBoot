package com.project.bankingApp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.bankingApp.dto.DWDto;
import com.project.bankingApp.dto.TransactionDto;

@Service
public interface TransactionService {

	public TransactionDto deposit(DWDto d);
	public TransactionDto withdraw(DWDto d);
	public TransactionDto transfer(long senderAccNumber, long receiverAccNumber, double amount);
	public List<TransactionDto> TransactionHistory ();
	List<TransactionDto> getAllTransactions();
}

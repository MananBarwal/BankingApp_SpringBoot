package com.project.bankingApp.service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.project.bankingApp.Security.UserDetails.CustomUserDetails;
import com.project.bankingApp.Security.jwt.JwtUtil;
import com.project.bankingApp.dto.DWDto;
import com.project.bankingApp.dto.TransactionDto;
import com.project.bankingApp.entity.Account;
import com.project.bankingApp.entity.EnumforTransaction;
import com.project.bankingApp.entity.Transaction;
import com.project.bankingApp.mapper.AccountMapper;
import com.project.bankingApp.mapper.TransactionMapper;
import com.project.bankingApp.repository.AccountRepository;
import com.project.bankingApp.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	AccountRepository AccountRepo;
	
	@Autowired
    AccountMapper accountMapper;
	
	@Autowired
	TransactionRepository TransactionRepo;
	
	@Autowired
	TransactionMapper tmapper;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Override
	@Transactional
	public TransactionDto deposit(DWDto dw) {
		long accNumber=dw.getId();
		double amount =dw.getAmount();
		Account a= AccountRepo.findById(accNumber).orElseThrow(() -> new RuntimeException ("Not found"));
		double currentBalance=a.getBalance();
		double newBalance=currentBalance+amount;
		a.setBalance(newBalance);
		AccountRepo.save(a);
		Transaction depositTransaction=new Transaction();
		depositTransaction.setAccount(a);
		depositTransaction.setTransactionType(EnumforTransaction.DEPOSIT);
		depositTransaction.setPaymentFrom(dw.getName());
		depositTransaction.setPaymentTo("N/A");
		depositTransaction.setAmount(amount);
		depositTransaction.setTransactionDate(LocalDateTime.now());
		return tmapper.transactionToDto(TransactionRepo.save(depositTransaction));
	}

	@Override
	@Transactional
	public TransactionDto withdraw(DWDto dw) {
		long accNumber=dw.getId();
		double amount=dw.getAmount();
		if (amount <= 0) {
			throw new RuntimeException("Amount should be greater than zero");
		}
		Account a= AccountRepo.findById(accNumber).orElseThrow(() -> new RuntimeException ("Not found"));
		double currentBalance=a.getBalance();
		if (amount>currentBalance){
			throw new RuntimeException ("Insufficient funds");
		}
		else{
			double newBalance = currentBalance - amount;
			a.setBalance(newBalance);
			AccountRepo.save(a);
			Transaction withdrawTransaction = new Transaction();
			withdrawTransaction.setAccount(a);
			withdrawTransaction.setTransactionType(EnumforTransaction.WITHDRAW);
			withdrawTransaction.setPaymentTo(dw.getName());
			withdrawTransaction.setPaymentFrom("N/A");
			withdrawTransaction.setAmount(amount);
			withdrawTransaction.setSenderAccNumber(accNumber);
			withdrawTransaction.setTransactionDate(LocalDateTime.now());
			Transaction savedTransaction = TransactionRepo.save(withdrawTransaction);
			return tmapper.transactionToDto(savedTransaction);
	}}

	@Override
	@Transactional
	public TransactionDto transfer(long senderAccNumber, long receiverAccNumber, double amount) {
		Account a = AccountRepo.findById(senderAccNumber).orElseThrow(() -> new RuntimeException ("Not found"));
		Account b = AccountRepo.findById(receiverAccNumber).orElseThrow(() -> new RuntimeException ("Not found"));
		double currentBalance=a.getBalance();
		if (amount>currentBalance){
			throw new RuntimeException ("Insufficient funds");
		}
		else{
			double newBalance = currentBalance - amount;
			double newBalance2 = b.getBalance() + amount;
			b.setBalance(newBalance2);
			a.setBalance(newBalance);
			AccountRepo.save(a);
			AccountRepo.save(b);
			
			Transaction senderTransaction = new Transaction();
			senderTransaction.setAccount(a);
			senderTransaction.setReceiverAccNumber(receiverAccNumber);
			senderTransaction.setTransactionType(EnumforTransaction.TRANSFER_SENT);
			senderTransaction.setAmount(amount);
			senderTransaction.setTransactionDate(LocalDateTime.now());
			senderTransaction.setPaymentFrom("N/A");
			senderTransaction.setPaymentTo("N/A");
			senderTransaction.setSenderAccNumber(senderAccNumber);

			Transaction receiverTransaction = new Transaction();
			receiverTransaction.setAccount(b);
			receiverTransaction.setSenderAccNumber(senderAccNumber);
			receiverTransaction.setTransactionType(EnumforTransaction.TRANSFER_RECEIVED);
			receiverTransaction.setAmount(amount);
			receiverTransaction.setTransactionDate(LocalDateTime.now());
			receiverTransaction.setPaymentFrom("N/A");
			receiverTransaction.setPaymentTo("N/A");
			receiverTransaction.setReceiverAccNumber(receiverAccNumber);

			TransactionRepo.save(senderTransaction);
			TransactionRepo.save(receiverTransaction);
		return tmapper.transactionToDto(senderTransaction);
		}
	}

	@Override
	public List<TransactionDto> getAllTransactions() {
	    List<Transaction> transactions = TransactionRepo.findAll();
	    return transactions.stream().map(tmapper::transactionToDto).toList();
	}
	
	@Override
	public List<TransactionDto> TransactionHistory() {
		
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		if (a == null || !a.isAuthenticated()) { throw new RuntimeException ("Unauthenticated User");}
		CustomUserDetails UserDetails = (CustomUserDetails) a.getPrincipal();
		long accNumber = Long.parseLong( UserDetails.getUsername());
		List<Transaction> transactions = TransactionRepo.getTransactionsbyDate(accNumber);
		/*
		 * List<TransactionDto> dtoList=new ArrayList<TransactionDto>(); for
		 * (Transaction t : transactions) { dtoList.add(tmapper.transactionToDto(t)); }
		 * return dtoList;
		 */
		return transactions.stream().map(tmapper::transactionToDto).toList();
	}

}


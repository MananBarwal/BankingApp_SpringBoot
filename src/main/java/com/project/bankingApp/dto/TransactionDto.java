package com.project.bankingApp.dto;

import java.time.LocalDateTime;

import com.project.bankingApp.entity.Account;
import com.project.bankingApp.entity.EnumforTransaction;

import lombok.Data;

@Data
public class TransactionDto {
	    private long Transaction_id;
	    private long accNumber;
	    private Account account;
	    private EnumforTransaction transactionType; // "DEPOSIT", "WITHDRAW", "TRANSFER"
	    private double amount;
	    private String PaymentFrom;
	    private String PaymentTo;
	    private LocalDateTime transactionDate;
	    private Long receiverAccNumber; // For transfers
	    private Long senderAccNumber; //for transfers

	}


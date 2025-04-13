package com.project.bankingApp.entity;

import java.time.LocalDateTime;
import com.project.bankingApp.entity.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Transaction_id;
    @ManyToOne
    @JoinColumn(name = "accNumber", nullable = false)
    private Account account;
    @Enumerated(EnumType.STRING)
    private EnumforTransaction transactionType; // "DEPOSIT", "WITHDRAW", "TRANSFER"
    private double amount;
    private String PaymentFrom;
    private String PaymentTo;
    private LocalDateTime transactionDate;
    private Long receiverAccNumber; // For transfers
    private Long senderAccNumber; // For transfers

}


package com.project.bankingApp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "Accounts")
@Entity
@Getter
@Setter
public class Account{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long accNumber;
	@Column(name="Account_holder_name")
	private String accHolderName;
	private double balance;
	private long phoneNumber;
	private String email;
	private String password;
	@Enumerated(EnumType.STRING) // Maps Enum to a String column
	private EnumforRoles role;
	private boolean isActive;
	@Column(unique=true)
	private long aadharNumber;
}

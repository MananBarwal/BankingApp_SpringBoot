package com.project.bankingApp.dto;

import com.project.bankingApp.entity.EnumforRoles;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data // Creates Getter, Setter and constructor

public class AccountDto {
	
	@Positive(message = "Account number must be a positive number")
	private long accNumber;
	
	@NotBlank(message = "Account holder name is mandatory")
	private String accHolderName;

	private double balance;

	@Digits(integer = 10, fraction = 0, message = "Phone number must be a 10 digit number")
	@Min(value = 6000000000L, message = "Phone number must be valid & 10 digits minimum")
	@Max(value = 9999999999L, message = "Phone number must be at most 10 digits")
	@NotBlank(message = "phoneNumber is necessary")
	private long phoneNumber;

	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email format is invalid")
	private String email;

	@NotBlank(message = "Password cannot be blank")
	@Size(min = 4, message = "Password must have 4 characters")
	private String password;

	@NotBlank(message = "Role can't be empty")
	private EnumforRoles role;

	private boolean isActive;

	@Positive(message = "Aadhar number must be positive")
	@NotBlank(message="Aadhar number is mandatory")
	private long aadharNumber;

	
}
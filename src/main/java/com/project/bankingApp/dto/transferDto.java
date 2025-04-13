package com.project.bankingApp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class transferDto {
	private long senderId;
	private long receiverId;
	private double amount;

}

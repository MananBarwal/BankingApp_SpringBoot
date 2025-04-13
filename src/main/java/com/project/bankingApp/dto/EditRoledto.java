package com.project.bankingApp.dto;

import com.project.bankingApp.entity.EnumforRoles;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
@Data
public class EditRoledto {
		@Positive
	    private long id;
		@NotNull
	    private EnumforRoles role;

	}


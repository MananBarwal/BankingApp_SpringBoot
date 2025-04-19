package com.project.bankingApp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.bankingApp.dto.DWDto;
import com.project.bankingApp.dto.TransactionDto;
import com.project.bankingApp.entity.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
	@Mapping(source = "account.accNumber", target = "accNumber")
	TransactionDto transactionToDto(Transaction Transaction);
	@Mapping(source = "accNumber", target = "account.accNumber")
    Transaction dtoToTransaction(TransactionDto Transaction);

    TransactionDto DWDto_to_TransactionDto (DWDto dw);
    
}

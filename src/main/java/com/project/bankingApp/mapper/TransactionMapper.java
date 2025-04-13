package com.project.bankingApp.mapper;

import org.mapstruct.Mapper;

import com.project.bankingApp.dto.DWDto;
import com.project.bankingApp.dto.TransactionDto;
import com.project.bankingApp.entity.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
	
	TransactionDto transactionToDto(Transaction Transaction);
    Transaction dtoToTransaction(TransactionDto Transaction);

    TransactionDto DWDto_to_TransactionDto (DWDto dw);
    
}

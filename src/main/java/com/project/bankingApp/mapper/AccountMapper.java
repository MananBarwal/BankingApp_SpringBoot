package com.project.bankingApp.mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.project.bankingApp.dto.AccountDto;
import com.project.bankingApp.dto.updateDto;
import com.project.bankingApp.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
	
    AccountDto accountToDto(Account account);
    Account dtoToAccount(AccountDto accountDto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccountFromDto(updateDto dto, @MappingTarget Account entity);
}


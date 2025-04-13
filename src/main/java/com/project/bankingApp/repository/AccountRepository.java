package com.project.bankingApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bankingApp.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	
	 Optional<List<Account>> findByEmail(String email);
	 
	 @Query("Select exists (Select 1 from Account a where a.aadharNumber= :aadharNum)")
	 boolean aadharUsed(@Param("aadharNum") long aadharNum);
	 
	 @Query("Select exists (Select 1 from Account a where a.phoneNumber= :phoneNumber)")
	 boolean phoneUsed(@Param("aadharNum") long phoneNumber);

}

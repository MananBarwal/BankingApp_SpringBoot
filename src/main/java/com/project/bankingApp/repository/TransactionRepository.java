package com.project.bankingApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bankingApp.entity.Transaction;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	@Query("Select t from Transaction t where t.account.accNumber = :accNumber ORDER BY t.transactionDate DESC")
	public List<Transaction> getTransactionsbyDate(@Param("accNumber") long accNumber);
	

}

package com.myfinbank.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.myfinbank.entity.Transaction;
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	
	// Retrieves all transactions linked to a specific account ID
	List<Transaction>findByAccountAccountId(Long accountId);

}

package com.myfinbank.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myfinbank.entity.RecurringDeposit;
import java.util.List;
public interface RecurringDepositRepository extends JpaRepository<RecurringDeposit, Long> {
	
	// Retrieves all recurring deposits linked to a specific account ID
	List<RecurringDeposit>findByAccountAccountId(Long accountId);

}

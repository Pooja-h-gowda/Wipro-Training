package com.myfinbank.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myfinbank.entity.FixedDeposit;
import java.util.List;
public interface FixedDepositRepository extends JpaRepository<FixedDeposit, Long> {
	
	// Retrieves all fixed deposits linked to a specific account ID
	List<FixedDeposit>findByAccountAccountId(Long accountId);

}

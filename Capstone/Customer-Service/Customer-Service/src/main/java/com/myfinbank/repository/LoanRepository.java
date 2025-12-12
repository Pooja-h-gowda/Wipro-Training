package com.myfinbank.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myfinbank.entity.Loan;
import java.util.List;
public interface LoanRepository extends JpaRepository<Loan, Long> {
	
	// Retrieves all loan records with a specific customer ID
	List<Loan> findByCustomerId(Long customerId);

}

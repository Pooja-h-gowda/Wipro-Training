package com.myfinbank.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myfinbank.entity.Account;
import java.util.List;
import java.util.Optional;
public interface AccountRepository extends JpaRepository<Account, Long>{
	
	// Fetches the account details using customer ID
	Optional<Account> findByCustomerId(Long customerId);

}

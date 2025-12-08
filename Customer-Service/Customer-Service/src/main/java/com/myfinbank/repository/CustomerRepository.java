package com.myfinbank.repository;
import com.myfinbank.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	 // Fetches a customer record using the provided email ID
	Customer findByEmail(String email);
}

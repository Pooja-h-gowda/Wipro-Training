package com.myfinbank.security;

import com.myfinbank.entity.Customer;
import com.myfinbank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    // Injects CustomerRepository to fetch customer details from the database
    @Autowired
    private CustomerRepository customerRepository;

    // Loads customer authentication details for Spring Security using email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Customer customer = customerRepository.findByEmail(email);

        if (customer == null) {
            throw new UsernameNotFoundException("Customer not found");
        }

        return User.withUsername(customer.getEmail())
                .password(customer.getPassword())
                .roles("USER")
                .build();
    }

    // Retrieves full customer entity using email ID
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}

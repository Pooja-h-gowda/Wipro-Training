package com.myfinbank.service;

import com.myfinbank.dto.CustomerDTO;
import com.myfinbank.entity.Account;
import com.myfinbank.entity.Chat;
import com.myfinbank.entity.Customer;
import com.myfinbank.entity.FixedDeposit;
import com.myfinbank.entity.RecurringDeposit;
import com.myfinbank.entity.Transaction;

import java.util.List;

public interface CustomerService {

    // Registers a new customer in the system
    Customer register(Customer customer);

    // Authenticates customer using email and password
    Customer login(String email, String password);

    // Logs out the customer using customer ID
    String logout(Long id);

    // Fetches customer details using customer ID
    Customer findById(Long id);

    // Retrieves the list of all customers
    List<Customer> getAllCustomers();

    // Retrieves customer details using customer ID
    Customer getCustomerById(Long id);

    // Updates customer profile details
    Customer updateCustomer(Long id, Customer customer);   

    // Activates a customer account
    void activateCustomer(Long id);

    // Deactivates a customer account
    void deactivateCustomer(Long id);   

    // Finds customer details using email ID
    Customer findByEmail(String email);

    // Converts Customer entity to CustomerDTO
    CustomerDTO convertToDto(Customer customer);  

    // Deposits the specified amount into customer account
    void deposit(Long customerId, Double amount);

    // Withdraws the specified amount from customer account
    void withdraw(Long customerId, Double amount);

    // Retrieves all transaction records of the customer
    List<Transaction> getTransactions(Long customerId);

    // Returns the current balance of the customer account
    Double getBalance(Long customerId);  

    // Retrieves account details of the customer
    Account getAccountDetails(Long customerId);

    // Creates a new fixed deposit for the customer
    void createFD(Long customerId, Double amount, Double interestRate, Integer months);

    // Retrieves all fixed deposits of the customer
    List<FixedDeposit> getFDs(Long customerId);

    // Creates a new recurring deposit for the customer
    void createRD(Long customerId, Double monthlyAmount, Double interestRate, Integer months);

    // Retrieves all recurring deposits of the customer
    List<RecurringDeposit> getRDs(Long customerId);

    // Retrieves all chat messages for a specific customer
    List<Chat> getChatByCustomer(Long customerId);

    // Sends a chat message from customer to admin
    Chat sendMessageFromCustomer(Long customerId, String message);

    // Sends a chat message from admin to a specific customer
    Chat sendMessageFromAdmin(Long customerId, Long adminId, String message);

}

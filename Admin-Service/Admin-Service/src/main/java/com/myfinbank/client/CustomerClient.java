package com.myfinbank.client;

import com.myfinbank.dto.CustomerDTO;
import com.myfinbank.dto.LoanResponseDto;
import com.myfinbank.entity.Chat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "customer-service")
public interface CustomerClient {

    // Fetches the list of all customers from the customer service
    @GetMapping("/api/customers")
    List<CustomerDTO> getAllCustomers();

    // Deactivates a specific customer account using customer ID
    @PutMapping("/api/customers/deactivate/{id}")
    void deactivateCustomer(@PathVariable Long id);

    // Activates a specific customer account using customer ID
    @PutMapping("/api/customers/activate/{id}")
    void activateCustomer(@PathVariable Long id);

    // Retrieves a specific customer by customer ID
    @GetMapping("/api/customers/{id}")
    CustomerDTO getCustomer(@PathVariable Long id);

    // Updates customer details using customer ID
    @PutMapping("/api/customers/{id}")
    CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customer);

    // Fetches all loan details from the customer service
    @GetMapping("/customer/loans/all")  
    List<LoanResponseDto> getAllLoans();

    // Approves a specific loan using loan ID
    @PutMapping("/api/admin/loans/approve/{loanId}")
    LoanResponseDto approveLoan(@PathVariable Long loanId);

    // Denies a specific loan using loan ID
    @PutMapping("/api/admin/loans/deny/{loanId}")
    LoanResponseDto denyLoan(@PathVariable Long loanId);

    // Retrieves all chat messages of a specific customer
    @GetMapping("/api/customers/chat/{customerId}")
    List<Chat> getChatByCustomer(@PathVariable Long customerId);

    // Sends a chat message from customer to admin
    @PostMapping("/api/customers/chat/send/{customerId}")
    Chat sendCustomerMessage(@PathVariable Long customerId, @RequestParam String message);

    // Sends a chat message from admin to a specific customer
    @PostMapping("/api/customers/chat/send/admin")
    Chat sendAdminMessage(@RequestParam Long customerId, @RequestParam Long adminId, @RequestParam String message);

}

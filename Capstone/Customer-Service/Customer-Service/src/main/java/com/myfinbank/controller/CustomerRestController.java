package com.myfinbank.controller;

import com.myfinbank.entity.Chat;
import com.myfinbank.entity.Customer;
import com.myfinbank.dto.CustomerDTO;
import com.myfinbank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    // Registers a new customer and returns the customer details as DTO
    @PostMapping("/register")
    public CustomerDTO register(@RequestBody Customer customer) {
        Customer saved = customerService.register(customer);
        return customerService.convertToDto(saved);
    }

    // Authenticates customer login and returns customer details as DTO
    @PostMapping("/login")
    public CustomerDTO login(@RequestParam String email, @RequestParam String password) {
        Customer logged = customerService.login(email, password);
        return customerService.convertToDto(logged);
    }

    // Fetches a specific customer using customer ID
    @GetMapping("/{customerId}")
    public CustomerDTO getCustomer(@PathVariable Long customerId) {
        Customer customer = customerService.findById(customerId);
        return customerService.convertToDto(customer);
    }

    // Fetches and returns all customers as a list of DTOs
    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers()
                .stream()
                .map(customerService::convertToDto)
                .collect(Collectors.toList());
    }

    // Updates customer details and returns the updated customer as DTO
    @PutMapping("/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody Customer customer) {
        Customer updated = customerService.updateCustomer(customerId, customer);
        return customerService.convertToDto(updated);
    }

    // Activates a specific customer account using customer ID
    @PutMapping("/activate/{id}")
    public String activateCustomer(@PathVariable Long id) {
        customerService.activateCustomer(id);
        return "Customer activated successfully!";
    }

    // Deactivates a specific customer account using customer ID
    @PutMapping("/deactivate/{id}")
    public String deactivateCustomer(@PathVariable Long id) {
        customerService.deactivateCustomer(id);
        return "Customer deactivated successfully!";
    }

    // Sends a chat message from customer to admin
    @PostMapping("/chat/send/{customerId}")
    public Chat sendCustomerMessage(@PathVariable Long customerId,
                                    @RequestParam String message) {
        return customerService.sendMessageFromCustomer(customerId, message);
    }

    // Sends a chat message from admin to a specific customer
    @PostMapping("/chat/send/admin")
    public Chat sendAdminMessage(@RequestParam Long customerId,
                                 @RequestParam Long adminId,
                                 @RequestParam String message) {
        return customerService.sendMessageFromAdmin(customerId, adminId, message);
    }

    // Retrieves full chat history for a specific customer
    @GetMapping("/chat/{customerId}")
    public List<Chat> getChat(@PathVariable Long customerId) {
        return customerService.getChatByCustomer(customerId);
    }

}

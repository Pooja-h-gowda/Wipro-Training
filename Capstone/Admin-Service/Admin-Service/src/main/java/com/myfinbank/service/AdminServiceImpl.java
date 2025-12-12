package com.myfinbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import com.myfinbank.client.CustomerClient;
import com.myfinbank.entity.Admin;
import com.myfinbank.entity.Chat;
import com.myfinbank.exception.ResourceNotFoundException;
import com.myfinbank.repository.AdminChatRepository;
import com.myfinbank.repository.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {

	// Injects the AdminRepository to perform database operations related to Admin
	@Autowired
	private AdminRepository adminRepository;

	// Injects the CustomerClient to communicate with the Customer microservice
	@Autowired
	private CustomerClient customerClient;

	// Injects the AdminChatRepository to handle chat-related database operations
	@Autowired
	private AdminChatRepository adminChatRepository;


    // Registers a new admin after performing validation checks
    @Override
    public Admin register(Admin admin) {

        // Validate name
        if (admin.getName() == null || admin.getName().trim().length() < 3) {
            throw new ResourceNotFoundException("Name must be at least 3 characters!");
        }

        // Validate email format
        if (admin.getEmail() == null || !admin.getEmail().contains("@")) {
            throw new ResourceNotFoundException("Invalid email format!");
        }

        // Validate password
        if (admin.getPassword() == null || admin.getPassword().length() < 4) {
            throw new ResourceNotFoundException("Password must be at least 4 characters!");
        }

        // Check duplicate email
        if (adminRepository.findByEmail(admin.getEmail()) != null) {
            throw new ResourceNotFoundException("Email already registered!");
        }

        return adminRepository.save(admin);
    }

    // Authenticates admin credentials during login
    @Override
    public Admin login(String email, String password) {

        Admin admin = adminRepository.findByEmail(email);

        if (admin == null) {
            throw new ResourceNotFoundException("Admin not found!");
        }

        if (!admin.getPassword().equals(password)) {
            throw new ResourceNotFoundException("Invalid password!");
        }

        return admin;
    }

    // Sends a chat message from admin to a specific customer
    @Override
    public Chat sendMessage(Long adminId, Long customerId, String message) {
        Chat chat = new Chat();
        chat.setAdminId(adminId);
        chat.setCustomerId(customerId);
        chat.setSender("ADMIN");
        chat.setMessage(message);
        chat.setTimestamp(LocalDateTime.now());

        return adminChatRepository.save(chat);
    }

    // Retrieves all chat messages for a specific customer
    @Override
    public List<Chat> getChatByCustomer(Long customerId) {
        return adminChatRepository.findByCustomerId(customerId);
    }

    // Retrieves the list of all registered admins
    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // Activates a customer account through customer service
    @Override
    public String activateCustomer(Long customerId) {
        customerClient.activateCustomer(customerId);
        return "Customer activated successfully!";
    }

    // Deactivates a customer account through customer service
    @Override
    public String deactivateCustomer(Long customerId) {
        customerClient.deactivateCustomer(customerId);
        return "Customer deactivated successfully!";
    }
    
}

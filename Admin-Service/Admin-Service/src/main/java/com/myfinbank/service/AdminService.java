package com.myfinbank.service;

import com.myfinbank.entity.Admin;
import com.myfinbank.entity.Chat;

import java.util.List;

public interface AdminService {

    // Registers a new admin into the system
    Admin register(Admin admin);

    // Authenticates admin using email and password
    Admin login(String email, String password);

    // Retrieves the list of all admins
    List<Admin> getAllAdmins();

    // Activates a specific customer account using customer ID
    String activateCustomer(Long customerId);

    // Deactivates a specific customer account using customer ID
    String deactivateCustomer(Long customerId);

    // Sends a chat message from admin to a customer
    Chat sendMessage(Long adminId, Long customerId, String message);

    // Retrieves all chat messages for a specific customer
    List<Chat> getChatByCustomer(Long customerId);

}

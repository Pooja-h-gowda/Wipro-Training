package com.myfinbank.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import com.myfinbank.entity.Admin;
import com.myfinbank.entity.Chat;
import com.myfinbank.service.AdminService;
import com.myfinbank.client.CustomerClient;
import com.myfinbank.dto.CustomerDTO;
import com.myfinbank.dto.LoanResponseDto;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final CustomerClient customerClient;

    public AdminController(AdminService adminService, CustomerClient customerClient) {
        this.adminService = adminService;
        this.customerClient = customerClient;
    }

    // Displays the admin registration page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // Handles admin registration process
    @PostMapping("/register")
    public String register(Admin admin, Model model) {
        try {
            adminService.register(admin);
            model.addAttribute("message", "Admin registered successfully!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "register";
    }

    // Displays the admin login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Authenticates admin credentials and starts session
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        try {
            Admin admin = adminService.login(email, password);
            session.setAttribute("loggedAdmin", admin);
            model.addAttribute("admin", admin);
            return "dashboard";

        } catch (Exception e) {
            model.addAttribute("error", "Invalid email or password!");
            return "login";
        }
    }

    // Displays the admin dashboard after successful login
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("admin", admin);
        return "dashboard";
    }

    // Displays list of all customers to the admin
    @GetMapping("/customers")
    public String viewCustomers(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        List<CustomerDTO> customers = customerClient.getAllCustomers();
        model.addAttribute("customers", customers);
        model.addAttribute("admin", admin);
        return "customers";
    }

    // Activates a deactivated customer account
    @GetMapping("/activate/{id}")
    public String activate(@PathVariable Long id, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        customerClient.activateCustomer(id);
        return "redirect:/admin/customers";
    }

    // Deactivates an active customer account
    @GetMapping("/deactivate/{id}")
    public String deactivate(@PathVariable Long id, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        customerClient.deactivateCustomer(id);
        return "redirect:/admin/customers";
    }

    // Displays list of all loan applications to the admin
    @GetMapping("/loans")
    public String viewLoans(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        List<LoanResponseDto> loans = customerClient.getAllLoans();
        model.addAttribute("loans", loans);
        model.addAttribute("admin", admin);
        return "loans";
    }

    // Approves a specific loan after validation
    @GetMapping("/loans/approve/{loanId}")
    public String approveLoanUI(@PathVariable Long loanId,
                                HttpSession session,
                                Model model) {

        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            customerClient.approveLoan(loanId);

            model.addAttribute("success", "Loan approved successfully!");

        } catch (Exception e) {

            String message = e.getMessage();

            if (message != null && message.contains("Loan cannot be approved")) {
                model.addAttribute("error",
                        "Loan cannot be approved — customer balance is too low.");
            } else {
                model.addAttribute("error", "Something went wrong while approving the loan.");
            }
        }

        List<LoanResponseDto> loans = customerClient.getAllLoans();
        model.addAttribute("loans", loans);
        model.addAttribute("admin", admin);

        return "loans";
    }

    // Denies a specific loan request
    @GetMapping("/loans/deny/{loanId}")
    public String denyLoanUI(@PathVariable Long loanId,
                             HttpSession session,
                             Model model) {

        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            customerClient.denyLoan(loanId);
            model.addAttribute("success", "Loan denied successfully!");

        } catch (Exception e) {

            String message = e.getMessage();

            if (message != null && message.contains("Loan not found")) {
                model.addAttribute("error", "Cannot deny loan — loan does not exist.");
            } else {
                model.addAttribute("error", "Failed to deny loan. Please try again.");
            }
        }

        List<LoanResponseDto> loans = customerClient.getAllLoans();
        model.addAttribute("loans", loans);
        model.addAttribute("admin", admin);

        return "loans";
    }

    // Sends a chat message from admin to a customer
    @PostMapping("/chat/send")
    public String sendToCustomer(@RequestParam Long customerId,
                                 @RequestParam String message,
                                 HttpSession session) {

        Admin admin = (Admin) session.getAttribute("loggedAdmin");

        // Admin sends → call customer-service
        customerClient.sendAdminMessage(customerId, admin.getId(), message);

        return "redirect:/admin/chat/" + customerId;
    }

    // Opens chat interface between admin and selected customer
    @GetMapping("/chat/{customerId}")
    public String openChat(@PathVariable Long customerId, Model model, HttpSession session) {

        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        // Get chat messages
        List<Chat> messages = customerClient.getChatByCustomer(customerId);

        // Fetch customer info for display
        CustomerDTO customer = customerClient.getCustomer(customerId);

        model.addAttribute("messages", messages);
        model.addAttribute("customer", customer);
        model.addAttribute("customerId", customerId);
        model.addAttribute("admin", admin);

        return "admin-chat";
    }

    // Displays list of customers available for admin chat
    @GetMapping("/chat/customers")
    public String chatCustomerList(HttpSession session, Model model) {

        Admin admin = (Admin) session.getAttribute("loggedAdmin");
        if (admin == null) return "redirect:/admin/login";

        List<CustomerDTO> customers = customerClient.getAllCustomers();
        model.addAttribute("customers", customers);

        return "admin-chat-customers";
    }

    // Logs out the admin and clears the session
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}

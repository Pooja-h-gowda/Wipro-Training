package com.myfinbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.myfinbank.dto.LoanRequestDto;
import com.myfinbank.dto.LoanResponseDto;
import com.myfinbank.entity.Account;
import com.myfinbank.entity.Chat;
import com.myfinbank.entity.Customer;
import com.myfinbank.repository.ChatRepository;
import com.myfinbank.service.CustomerService;
import com.myfinbank.service.LoanService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    private LoanService loanService;
    
    @Autowired
    private ChatRepository chatRepository;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Displays the customer registration page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";  
    }

    // Registers a new customer and redirects to login page
    @PostMapping("/register")
    public String register(Customer customer, Model model) {

        customerService.register(customer);
        model.addAttribute("message", "Registration successful! Please login.");
        return "login";
    }

    // Displays the customer login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Displays the customer dashboard with account, balance, and chat details
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Customer customer = (Customer) session.getAttribute("loggedCustomer");
        if (customer == null) {
            return "redirect:/customer/login";
        }

        Account account = customerService.getAccountDetails(customer.getId());
        Double balance = customerService.getBalance(customer.getId());

        model.addAttribute("customer", customer);
        model.addAttribute("account", account);
        model.addAttribute("balance", balance);

        model.addAttribute("messages", customerService.getChatByCustomer(customer.getId()));
        int unread = chatRepository.countByCustomer_IdAndSenderAndReadStatus(
                customer.getId(), "ADMIN", false);

        model.addAttribute("unreadMessages", unread);

        return "dashboard";
    }

    // Displays the loan application page
    @GetMapping("/loan/apply")
    public String showApplyLoanPage(HttpSession session) {

        if (session.getAttribute("loggedCustomer") == null) {
            return "redirect:/customer/login";
        }

        return "loan-apply";
    }

    // Submits a new loan application for the customer
    @PostMapping("/loan/submit")
    public String submitLoan(@RequestParam Double amount,
                             @RequestParam Double interestRate,
                             @RequestParam Integer months,
                             HttpSession session) {

        Customer customer = (Customer) session.getAttribute("loggedCustomer");

        if (customer == null) {
            return "redirect:/customer/login";
        }

        LoanRequestDto request = new LoanRequestDto();
        request.setCustomerId(customer.getId());
        request.setAmount(amount);
        request.setInterestRate(interestRate);
        request.setMonths(months);

        loanService.applyLoan(request);

        return "redirect:/customer/loan/myloans";
    }

    // Displays all loans applied by the customer
    @GetMapping("/loan/myloans")
    public String viewMyLoans(Model model, HttpSession session) {

        Customer customer = (Customer) session.getAttribute("loggedCustomer");

        if (customer == null) {
            return "redirect:/customer/login";
        }

        List<LoanResponseDto> loans = loanService.getLoansByCustomer(customer.getId());
        model.addAttribute("loans", loans);

        return "my-loans";
    }

    // Displays the EMI payment page for a selected loan
    @GetMapping("/loan/pay")
    public String showPayEmiPage(@RequestParam Long loanId,
                                 HttpSession session,
                                 Model model) {

        Customer customer = (Customer) session.getAttribute("loggedCustomer");

        if (customer == null) {
            return "redirect:/customer/login";
        }

        LoanResponseDto loan = loanService.getLoanById(loanId);
        model.addAttribute("loan", loan);

        return "pay-emi"; 
    }

    // Processes EMI payment for a selected loan
    @PostMapping("/loan/pay")
    public String payEmi(@RequestParam Long loanId,
                         HttpSession session,
                         Model model) {

        Customer customer = (Customer) session.getAttribute("loggedCustomer");

        if (customer == null) {
            return "redirect:/customer/login";
        }

        try {
            loanService.payEmi(loanId, customer.getId());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            LoanResponseDto loan = loanService.getLoanById(loanId);
            model.addAttribute("loan", loan);
            return "pay-emi";
        }

        return "redirect:/customer/loan/myloans";
    }

    // Displays the deposit page
    @GetMapping("/account/deposit")
    public String showDepositPage() {
        return "deposit";
    }

    // Performs deposit transaction for the customer
    @PostMapping("/account/deposit")
    public String deposit(@RequestParam Double amount, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("loggedCustomer");
        customerService.deposit(customer.getId(), amount);
        return "redirect:/customer/account/transactions";
    }

    // Displays the withdraw page
    @GetMapping("/account/withdraw")
    public String showWithdrawPage() {
        return "withdraw";
    }

    // Performs withdrawal transaction for the customer
    @PostMapping("/account/withdraw")
    public String withdraw(@RequestParam Double amount, HttpSession session, Model model) {

        Customer customer = (Customer) session.getAttribute("loggedCustomer");

        try {
            customerService.withdraw(customer.getId(), amount);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "withdraw";
        }

        return "redirect:/customer/account/transactions";
    }

    // Displays all transaction history of the customer
    @GetMapping("/account/transactions")
    public String viewTransactions(HttpSession session, Model model) {

        Customer customer = (Customer) session.getAttribute("loggedCustomer");

        model.addAttribute("transactions", customerService.getTransactions(customer.getId()));

        return "transactions";
    }

    // Displays the fixed deposit application page
    @GetMapping("/fd/apply")
    public String showFdApplyPage(HttpSession session) {

        if (session.getAttribute("loggedCustomer") == null) {
            return "redirect:/customer/login";
        }

        return "fd-apply"; 
    }

    // Submits a new fixed deposit request
    @PostMapping("/fd/submit")
    public String submitFD(@RequestParam Double amount,
                           @RequestParam Double interestRate,
                           @RequestParam Integer months,
                           HttpSession session,
                           Model model) {

        Customer customer = (Customer) session.getAttribute("loggedCustomer");

        if (customer == null) {
            return "redirect:/customer/login";
        }

        try {
            customerService.createFD(customer.getId(), amount, interestRate, months);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "fd-apply";
        }

        return "redirect:/customer/fd/list";
    }

    // Displays all fixed deposits of the customer
    @GetMapping("/fd/list")
    public String viewFDList(HttpSession session, Model model) {

        Customer customer = (Customer) session.getAttribute("loggedCustomer");

        if (customer == null) {
            return "redirect:/customer/login";
        }

        model.addAttribute("fds", customerService.getFDs(customer.getId()));
        return "fd-list"; 
    }

    // Displays the recurring deposit application page
    @GetMapping("/rd/apply")
    public String showRdApplyPage(HttpSession session) {

        if (session.getAttribute("loggedCustomer") == null) {
            return "redirect:/customer/login";
        }

        return "rd-apply"; 
    }

    // Submits a new recurring deposit request
    @PostMapping("/rd/submit")
    public String submitRD(@RequestParam Double monthlyAmount,
                           @RequestParam Double interestRate,
                           @RequestParam Integer months,
                           HttpSession session,
                           Model model) {

        Customer customer = (Customer) session.getAttribute("loggedCustomer");

        if (customer == null) {
            return "redirect:/customer/login";
        }

        try {
            customerService.createRD(customer.getId(), monthlyAmount, interestRate, months);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "rd-apply";
        }

        return "redirect:/customer/rd/list";
    }

    // Displays all recurring deposits of the customer
    @GetMapping("/rd/list")
    public String viewRDList(HttpSession session, Model model) {

        Customer customer = (Customer) session.getAttribute("loggedCustomer");

        if (customer == null) {
            return "redirect:/customer/login";
        }

        model.addAttribute("rds", customerService.getRDs(customer.getId()));
        return "rd-list"; 
    }

    // Displays the customer chat page and marks admin messages as read
    @GetMapping("/chat")
    public String showChatPage(HttpSession session, Model model) {

        Customer customer = (Customer) session.getAttribute("loggedCustomer");
        if (customer == null) return "redirect:/customer/login";

        // get all chat messages for this customer
        List<Chat> chats = customerService.getChatByCustomer(customer.getId());

        //  mark all ADMIN messages as read
        for (Chat chat : chats) {
            if ("ADMIN".equals(chat.getSender()) && !chat.isReadStatus()) {
                chat.setReadStatus(true);
            }
        }

        // save updated readStatus
        chatRepository.saveAll(chats);

        model.addAttribute("messages", chats);
        return "customer-chat";
    }

    // Sends a chat message from customer to admin
    @PostMapping("/chat/send")
    public String sendMessage(@RequestParam String message, HttpSession session) {

        Customer customer = (Customer) session.getAttribute("loggedCustomer");
        if (customer == null) return "redirect:/customer/login";

        customerService.sendMessageFromCustomer(customer.getId(), message);

        return "redirect:/customer/chat";
    }

    // Logs out the customer and clears the session
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/customer/login";
    }
}

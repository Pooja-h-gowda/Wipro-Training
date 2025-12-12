package com.myfinbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.myfinbank.dto.CustomerDTO;
import com.myfinbank.entity.Account;
import com.myfinbank.entity.Chat;
import com.myfinbank.entity.Customer;
import com.myfinbank.entity.FixedDeposit;
import com.myfinbank.entity.RecurringDeposit;
import com.myfinbank.entity.Transaction;
import com.myfinbank.exception.ResourceNotFoundException;
import com.myfinbank.repository.AccountRepository;
import com.myfinbank.repository.ChatRepository;
import com.myfinbank.repository.CustomerRepository;
import com.myfinbank.repository.FixedDepositRepository;
import com.myfinbank.repository.RecurringDepositRepository;
import com.myfinbank.repository.TransactionRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.myfinbank.dto.MailDetail;

@Service
public class CustomerServiceImpl implements CustomerService {

    // Injects CustomerRepository to perform customer-related database operations
    @Autowired
    private CustomerRepository customerRepository;

    // Injects AccountRepository to manage account data
    @Autowired
    private AccountRepository accountRepository;

    // Injects TransactionRepository to manage transaction history
    @Autowired
    private TransactionRepository transactionRepository;

    // Injects FixedDepositRepository to manage fixed deposit data
    @Autowired
    private FixedDepositRepository fixedDepositRepository;

    // Injects RecurringDepositRepository to manage recurring deposit data
    @Autowired
    private RecurringDepositRepository recurringDepositRepository;
    
    // Injects ChatRepository to manage chat messages
    @Autowired
    private ChatRepository chatRepository;
    
    // Injects PasswordEncoder to securely encode and verify passwords
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Injects RestTemplate to communicate with external microservices
    @Autowired
    private RestTemplate restTemplate;

    // Generates a unique account number for a customer
    private String generateAccountNumber(Long customerId) {
        return "ACC" + (100000 + customerId);
    }

    // Registers a new customer and creates a linked savings account
    @Override
    public Customer register(Customer customer) {

        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new ResourceNotFoundException("Name cannot be empty!");
        }

        if (customer.getEmail() == null || !customer.getEmail().contains("@")) {
            throw new ResourceNotFoundException("Invalid email format!");
        }

        if (customer.getPhone() == null || !customer.getPhone().matches("\\d{10}")) {
            throw new ResourceNotFoundException("Phone number must be 10 digits!");
        }

        if (customer.getPassword() == null || customer.getPassword().length() < 4) {
            throw new ResourceNotFoundException("Password must be at least 4 characters!");
        }

        if (customerRepository.findByEmail(customer.getEmail()) != null) {
            throw new ResourceNotFoundException("Email already registered!");
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setStatus("active");
        Customer savedCustomer = customerRepository.save(customer);

        Account account = new Account();
        account.setAccountType("savings");
        account.setBalance(0.0);
        account.setCustomer(savedCustomer);
        account.setAccountNumber(generateAccountNumber(savedCustomer.getId()));

        accountRepository.save(account);

        return savedCustomer;
    }

    // Authenticates customer login using encrypted password
    @Override
    public Customer login(String email, String rawPassword) {
        Customer customer = customerRepository.findByEmail(email);

        if (customer == null) {
            return null;
        }

        if (!passwordEncoder.matches(rawPassword, customer.getPassword())) {
            return null;
        }

        return customer;
    }

    // Handles customer logout operation
    @Override
    public String logout(Long customerId) {
        return "Customer logged out successfully!";
    }

    // Fetches customer details by customer ID
    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found!"));
    }

    // Fetches customer details by customer ID
    @Override
    public Customer getCustomerById(Long id) {
        return findById(id);
    }

    // Retrieves all customers from the database
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Updates customer profile details
    @Override
    public Customer updateCustomer(Long id, Customer updatedCustomer) {

        Customer existing = findById(id);

        if (updatedCustomer.getName() != null)
            existing.setName(updatedCustomer.getName());
        if (updatedCustomer.getPhone() != null)
            existing.setPhone(updatedCustomer.getPhone());
        if (updatedCustomer.getPassword() != null)
            existing.setPassword(updatedCustomer.getPassword());

        return customerRepository.save(existing);
    }

    // Activates a customer account
    @Override
    public void activateCustomer(Long id) {
        Customer customer = findById(id);
        customer.setStatus("active");
        customerRepository.save(customer);
    }

    // Deactivates a customer account
    @Override
    public void deactivateCustomer(Long id) {
        Customer customer = findById(id);
        customer.setStatus("inactive");
        customerRepository.save(customer);
    }

    // Finds a customer using email ID
    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    // Converts Customer entity to CustomerDTO
    @Override
    public CustomerDTO convertToDto(Customer customer) {
        CustomerDTO dto = new CustomerDTO();

        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setStatus(customer.getStatus());

        return dto;
    }

    // Deposits amount into customer account and creates transaction record
    @Override
    public void deposit(Long customerId, Double amount) {

        Account account = accountRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction t = new Transaction();
        t.setAccount(account);
        t.setAmount(amount);
        t.setType("DEPOSIT");
        t.setDateTime(LocalDateTime.now());

        transactionRepository.save(t);
    }

    // Withdraws amount from customer account and checks for zero balance
    @Override
    public void withdraw(Long customerId, Double amount) {

        Account account = accountRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (account.getBalance() < amount) {
            throw new ResourceNotFoundException("Insufficient balance!");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction t = new Transaction();
        t.setAccount(account);
        t.setAmount(amount);
        t.setType("WITHDRAW");
        t.setDateTime(LocalDateTime.now());
        transactionRepository.save(t);

        if (account.getBalance() == 0) {
            notifyAdminZeroBalance(account);
        }
    }

    // Retrieves transaction history of the customer
    @Override
    public List<Transaction> getTransactions(Long customerId) {

        Account account = accountRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return transactionRepository.findByAccountAccountId(account.getAccountId());
    }

    // Retrieves current account balance of the customer
    @Override
    public Double getBalance(Long customerId) {

        Account account = accountRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return account.getBalance();
    }

    // Retrieves account details of the customer
    @Override
    public Account getAccountDetails(Long customerId) {
        return accountRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    // Creates a fixed deposit for the customer
    public void createFD(Long customerId, Double amount, Double interestRate, Integer months) {

        Account account = accountRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (account.getBalance() < amount) {
            throw new ResourceNotFoundException("Insufficient balance for FD");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        FixedDeposit fd = new FixedDeposit();
        fd.setAccount(account);
        fd.setDepositAmount(amount);
        fd.setInterestRate(interestRate);
        fd.setDurationMonths(months);
        fd.setStartDate(LocalDate.now());
        fd.setEndDate(LocalDate.now().plusMonths(months));
        fd.setStatus("ACTIVE");

        fixedDepositRepository.save(fd);

        Transaction t = new Transaction();
        t.setAccount(account);
        t.setAmount(amount);
        t.setType("FD CREATED");
        t.setDateTime(LocalDateTime.now());
        transactionRepository.save(t);
    }

    // Retrieves all fixed deposits of the customer
    public List<FixedDeposit> getFDs(Long customerId) {

        Account account = accountRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return fixedDepositRepository.findByAccountAccountId(account.getAccountId());
    }

    // Creates a recurring deposit for the customer
    public void createRD(Long customerId, Double monthlyAmount, Double interestRate, Integer months) {

        Account account = accountRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (account.getBalance() < monthlyAmount) {
            throw new ResourceNotFoundException("Insufficient balance for RD");
        }

        account.setBalance(account.getBalance() - monthlyAmount);
        accountRepository.save(account);

        RecurringDeposit rd = new RecurringDeposit();
        rd.setAccount(account);
        rd.setMonthlyAmount(monthlyAmount);
        rd.setInterestRate(interestRate);
        rd.setDurationMonths(months);
        rd.setStartDate(LocalDate.now());
        rd.setEndDate(LocalDate.now().plusMonths(months));
        rd.setStatus("ACTIVE");

        recurringDepositRepository.save(rd);

        Transaction t = new Transaction();
        t.setAccount(account);
        t.setAmount(monthlyAmount);
        t.setType("RD FIRST INSTALLMENT");
        t.setDateTime(LocalDateTime.now());
        transactionRepository.save(t);
    }

    // Retrieves all recurring deposits of the customer
    public List<RecurringDeposit> getRDs(Long customerId) {

        Account account = accountRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return recurringDepositRepository.findByAccountAccountId(account.getAccountId());
    }

    // Sends a chat message from customer to admin
    @Override
    public Chat sendMessageFromCustomer(Long customerId, String message) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Chat chat = new Chat();
        chat.setCustomer(customer);
        chat.setSender("CUSTOMER");
        chat.setAdminId(null); 
        chat.setMessage(message);
        chat.setTimestamp(LocalDateTime.now());
        chat.setReadStatus(false);

        return chatRepository.save(chat);
    }

    // Retrieves chat messages for a specific customer
    @Override
    public List<Chat> getChatByCustomer(Long customerId) {

        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return chatRepository.findByCustomer_Id(customerId);
    }

    // Sends a chat message from admin to customer
    @Override
    public Chat sendMessageFromAdmin(Long customerId, Long adminId, String message) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Chat chat = new Chat();
        chat.setCustomer(customer);
        chat.setSender("ADMIN");
        chat.setAdminId(adminId);
        chat.setMessage(message);
        chat.setTimestamp(LocalDateTime.now());
        chat.setReadStatus(false);

        return chatRepository.save(chat);
    }

    // Sends email notification to admin when customer balance becomes zero
    private void notifyAdminZeroBalance(Account account) {

        MailDetail mail = new MailDetail();
        mail.setRecipient("poojahgowda17@gmail.com"); 
        mail.setSubject("Zero Balance Alert");
        mail.setMsgBody(
                "Customer with Account Number: " + account.getAccountNumber() +
                " has reached ZERO balance.\nCurrent Balance: ₹0"
        );

        String url = "http://EMAIL-SERVICE/api/zero-balance-alert";

        try {
            restTemplate.postForObject(url, mail, String.class);
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }

}

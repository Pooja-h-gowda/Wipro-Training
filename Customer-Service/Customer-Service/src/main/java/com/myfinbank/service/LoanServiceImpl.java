package com.myfinbank.service;

import com.myfinbank.dto.LoanRequestDto;
import com.myfinbank.dto.LoanResponseDto;
import com.myfinbank.entity.Account;
import com.myfinbank.entity.Customer;
import com.myfinbank.entity.Loan;
import com.myfinbank.entity.Transaction;
import com.myfinbank.exception.ResourceNotFoundException;
import com.myfinbank.repository.AccountRepository;
import com.myfinbank.repository.CustomerRepository;
import com.myfinbank.repository.LoanRepository;
import com.myfinbank.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    // Injects LoanRepository to perform loan-related database operations
    @Autowired
    private LoanRepository loanRepository;

    // Injects CustomerRepository to fetch customer details
    @Autowired
    private CustomerRepository customerRepository;

    // Injects AccountRepository to access customer account details
    @Autowired
    private AccountRepository accountRepository;

    // Injects TransactionRepository to store EMI and loan transactions
    @Autowired
    private TransactionRepository transactionRepository;

    // Applies for a new loan for the customer
    @Override
    public LoanResponseDto applyLoan(LoanRequestDto request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setAmount(request.getAmount());
        loan.setInterestRate(request.getInterestRate());
        loan.setMonths(request.getMonths());
        loan.setStatus("PENDING");

        Loan savedLoan = loanRepository.save(loan);
        return convertToDto(savedLoan);
    }

    // Approves a pending loan if minimum balance condition is satisfied
    @Override
    public LoanResponseDto approveLoan(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if (!loan.getStatus().equals("PENDING"))
            throw new ResourceNotFoundException("Only PENDING loans can be approved");

        Customer customer = loan.getCustomer();

        if (customer.getAccounts().isEmpty())
            throw new ResourceNotFoundException("Customer has no accounts");

        Account account = customer.getAccounts().get(0);

        double minimumRequiredBalance = 1000.0;

        if (account.getBalance() < minimumRequiredBalance) {
            throw new ResourceNotFoundException(
                    "Loan cannot be approved! Customer balance is too low. Minimum required: " 
                    + minimumRequiredBalance
            );
        }

        double emi = calculateEmi(
                loan.getAmount(),
                loan.getInterestRate(),
                loan.getMonths()
        );

        loan.setEmiAmount(emi);
        loan.setRemainingAmount(loan.getAmount());
        loan.setStatus("APPROVED");

        account.setBalance(account.getBalance() + loan.getAmount());
        accountRepository.save(account);

        Loan updatedLoan = loanRepository.save(loan);
        return convertToDto(updatedLoan);
    }

    // Denies a loan request and updates its status to REJECTED
    @Override
    public LoanResponseDto denyLoan(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        loan.setStatus("REJECTED");
        return convertToDto(loanRepository.save(loan));
    }

    // Calculates EMI based on loan amount, interest rate, and tenure
    @Override
    public double calculateEmi(double amount, double interestRate, int months) {

        double monthlyRate = interestRate / (12 * 100);

        return (amount * monthlyRate * Math.pow(1 + monthlyRate, months)) /
                (Math.pow(1 + monthlyRate, months) - 1);
    }

    // Processes EMI payment for an approved loan
    @Override
    public LoanResponseDto payEmi(Long loanId, Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if (!loan.getStatus().equals("APPROVED"))
            throw new ResourceNotFoundException("Loan is not active");

        if (customer.getAccounts().isEmpty())
            throw new ResourceNotFoundException("Customer has no accounts");

        Account account = customer.getAccounts().get(0);

        double emi = loan.getEmiAmount();

        if (account.getBalance() < emi)
            throw new ResourceNotFoundException("Insufficient balance for EMI");

        account.setBalance(account.getBalance() - emi);
        accountRepository.save(account);

        loan.setRemainingAmount(loan.getRemainingAmount() - emi);

        if (loan.getRemainingAmount() <= 0) {
            loan.setRemainingAmount(0.0);
            loan.setStatus("CLOSED");
        }

        Loan updated = loanRepository.save(loan);

        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(emi);
        tx.setType("LOAN EMI PAYMENT");
        tx.setDateTime(LocalDateTime.now());
        transactionRepository.save(tx);

        return convertToDto(updated);
    }

    // Retrieves all loan records in the system
    @Override
    public List<LoanResponseDto> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Retrieves all loan records of a specific customer
    @Override
    public List<LoanResponseDto> getLoansByCustomer(Long customerId) {
        return loanRepository.findByCustomerId(customerId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Retrieves a specific loan using loan ID
    @Override
    public LoanResponseDto getLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
        return convertToDto(loan);
    }

    // Converts Loan entity into LoanResponseDto
    private LoanResponseDto convertToDto(Loan loan) {

        LoanResponseDto dto = new LoanResponseDto();

        dto.setLoanId(loan.getLoanId());
        dto.setCustomerId(loan.getCustomer().getId());
        dto.setAmount(loan.getAmount());
        dto.setInterestRate(loan.getInterestRate());
        dto.setMonths(loan.getMonths());
        dto.setStatus(loan.getStatus());
        dto.setEmiAmount(loan.getEmiAmount());
        dto.setRemainingAmount(loan.getRemainingAmount());

        return dto;
    }
}

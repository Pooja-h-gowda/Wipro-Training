package com.myfinbank.service;

import com.myfinbank.dto.LoanRequestDto;
import com.myfinbank.dto.LoanResponseDto;
import java.util.List;

public interface LoanService {

    // Applies for a new loan using the given loan request details
    LoanResponseDto applyLoan(LoanRequestDto request);

    // Approves a specific loan using the loan ID
    LoanResponseDto approveLoan(Long loanId);

    // Denies a specific loan using the loan ID
    LoanResponseDto denyLoan(Long loanId);

    // Retrieves all loans associated with a specific customer
    List<LoanResponseDto> getLoansByCustomer(Long customerId);

    // Retrieves a specific loan using the loan ID
    LoanResponseDto getLoanById(Long loanId);

    // Retrieves all loan records in the system
    List<LoanResponseDto> getAllLoans();

    // Calculates the EMI based on amount, interest rate, and number of months
    double calculateEmi(double amount, double interestRate, int months);

    // Processes EMI payment for a specific loan and customer
    LoanResponseDto payEmi(Long loanId, Long customerId);
}

package com.myfinbank.controller;

import com.myfinbank.dto.LoanResponseDto;
import com.myfinbank.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/loans")   
public class AdminLoanController {

    // Injects the LoanService to handle loan approval and denial operations
    @Autowired
    private LoanService loanService;

    // Approves a specific loan using the loan ID
    @PutMapping("/approve/{loanId}")
    public LoanResponseDto approveLoan(@PathVariable Long loanId) {
        return loanService.approveLoan(loanId);
    }

    // Denies a specific loan using the loan ID
    @PutMapping("/deny/{loanId}")
    public LoanResponseDto denyLoan(@PathVariable Long loanId) {
        return loanService.denyLoan(loanId);
    }
}

package com.myfinbank.controller;

import com.myfinbank.dto.LoanRequestDto;
import com.myfinbank.dto.LoanResponseDto;
import com.myfinbank.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    // Submits a new loan application 
    @PostMapping("/apply")
    public LoanResponseDto applyLoan(@RequestBody LoanRequestDto request) {
        return loanService.applyLoan(request);
    }

    // Retrieves the list of all loan records
    @GetMapping("/all")
    public List<LoanResponseDto> getAllLoans() {
        return loanService.getAllLoans();
    }

    // Retrieves all loan details for a specific customer
    @GetMapping("/{customerId}")
    public List<LoanResponseDto> getCustomerLoans(@PathVariable Long customerId) {
        return loanService.getLoansByCustomer(customerId);
    }

}

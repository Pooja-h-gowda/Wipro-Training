package com.myfinbank.controller;

import com.myfinbank.client.CustomerClient;
import com.myfinbank.dto.CustomerDTO;
import com.myfinbank.dto.LoanResponseDto;
import com.myfinbank.entity.Admin;
import com.myfinbank.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminRestController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CustomerClient customerClient;

    // Fetches and returns the list of all loan applications from the customer service
    @GetMapping("/loans/all")
    public List<LoanResponseDto> getAllLoans() {
        return customerClient.getAllLoans(); // Calls /customer/loans/all
    }

    // Approves a specific loan using its loan ID
    @PutMapping("/loans/approve/{loanId}")
    public LoanResponseDto approveLoan(@PathVariable Long loanId) {
        return customerClient.approveLoan(loanId);
    }

    // Denies a specific loan using its loan ID
    @PutMapping("/loans/deny/{loanId}")
    public LoanResponseDto denyLoan(@PathVariable Long loanId) {
        return customerClient.denyLoan(loanId);
    }
}

package com.myfinbank.dto;

import lombok.Data;

@Data
public class LoanResponseDto {
    private Long loanId;
    private Long customerId;
    private Double amount;
    private Double interestRate;
    private Integer months;
    private String status;
}

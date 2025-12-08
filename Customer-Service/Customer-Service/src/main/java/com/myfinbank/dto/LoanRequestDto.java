package com.myfinbank.dto;

import lombok.Data;

@Data
public class LoanRequestDto {
    private Long customerId;
    private Double amount;
    private Double interestRate;
    private Integer months;
}

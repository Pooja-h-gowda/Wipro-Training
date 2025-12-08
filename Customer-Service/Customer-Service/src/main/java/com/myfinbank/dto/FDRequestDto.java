package com.myfinbank.dto;

import lombok.Data;

@Data
public class FDRequestDto {
    private Long customerId;
    private Double amount;
    private Double interestRate;
    private Integer months;
}

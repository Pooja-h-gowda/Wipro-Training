package com.myfinbank.dto;

import lombok.Data;

@Data
public class RDRequestDto {
    private Long customerId;
    private Double monthlyAmount;
    private Double interestRate;
    private Integer months;
}

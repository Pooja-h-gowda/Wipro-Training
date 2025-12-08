package com.myfinbank.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String status;
}

package com.example.CustomerService.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerResponseDto {
    private UUID id;
    private String name;
    private String address;
    private String phoneNumber;
}

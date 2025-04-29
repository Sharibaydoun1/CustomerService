package com.example.CustomerService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileVerificationResponseDto {
    private boolean valid;
    private String countryCode;
    private String countryName;
    private String operatorName;

}

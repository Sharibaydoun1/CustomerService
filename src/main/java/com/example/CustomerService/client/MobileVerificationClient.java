package com.example.CustomerService.client;

import com.example.CustomerService.dto.MobileVerificationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mobile-verification-service", url = "http://localhost:8083/api")
public interface MobileVerificationClient {

    @GetMapping("/verify/{phoneNumber}")
    ResponseEntity<MobileVerificationResponseDto> verifyMobileNumber(@PathVariable("phoneNumber") String phoneNumber);
}



package com.example.CustomerService.controller;

import com.example.CustomerService.dto.CustomerRequestDto;
import com.example.CustomerService.dto.CustomerResponseDto;
import com.example.CustomerService.model.Customer;
import com.example.CustomerService.service.Customerservice;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("user/customer")
@RequiredArgsConstructor

public class CustomerController {
    private final Customerservice customerservice;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<CustomerResponseDto> addCustomer(@RequestBody CustomerRequestDto customerRequestDto) {
       CustomerResponseDto customerResponseDto= modelMapper.
               map(customerservice.addCustomer(customerRequestDto), CustomerResponseDto.class);
       return ResponseEntity.ok(customerResponseDto);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@RequestBody CustomerRequestDto customerRequestDto,@PathVariable UUID id) {
        CustomerResponseDto customerResponseDto= modelMapper.
                map(customerservice.updateCustomer(id,customerRequestDto), CustomerResponseDto.class);
        return ResponseEntity.ok(customerResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<Customer> customers = customerservice.getAllCustomers();
        List<CustomerResponseDto> response = new ArrayList<>();
        customers.forEach(customer -> response.add(modelMapper.map(customer, CustomerResponseDto.class)));
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable UUID id) {
        customerservice.deleteCustomer(id);
        return ResponseEntity.ok("Customer deleted");
    }
}

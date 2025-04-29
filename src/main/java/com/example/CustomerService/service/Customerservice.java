package com.example.CustomerService.service;

import com.example.CustomerService.dto.CustomerRequestDto;
import com.example.CustomerService.model.Customer;

import java.util.List;
import java.util.UUID;

public interface Customerservice {
Customer addCustomer(CustomerRequestDto customerRequestDto);
Customer updateCustomer(UUID id, CustomerRequestDto customerRequestDto);
void deleteCustomer(UUID id);
List<Customer> getAllCustomers();
}

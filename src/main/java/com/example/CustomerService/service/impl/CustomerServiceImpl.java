package com.example.CustomerService.service.impl;

import com.example.CustomerService.client.MobileVerificationClient;
import com.example.CustomerService.dto.CustomerRequestDto;
import com.example.CustomerService.dto.MobileVerificationResponseDto;
import com.example.CustomerService.model.Customer;
import com.example.CustomerService.repository.CustomerRepository;
import com.example.CustomerService.service.Customerservice;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements Customerservice {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final MobileVerificationClient mobileVerificationClient;
    @Override
    public Customer addCustomer(CustomerRequestDto customerRequestDto) {
        ResponseEntity<MobileVerificationResponseDto> response = mobileVerificationClient.verifyMobileNumber(customerRequestDto.getPhoneNumber());
        if (response == null || !response.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            throw new IllegalArgumentException("Invalid mobile number");
        }
        return customerRepository.save(modelMapper.map(customerRequestDto, Customer.class));
    }

    @Override
    public Customer updateCustomer(UUID id, CustomerRequestDto customerRequestDto) {
       Customer customer= customerRepository.findById(id)
               .orElseThrow(()-> new RuntimeException("Customer not found"));
       if (customerRequestDto.getName() != null){
           customer.setName(customerRequestDto.getName());
       }
          if(customerRequestDto.getAddress() != null){
               customer.setAddress(customerRequestDto.getAddress());
           }
           if(customerRequestDto.getPhoneNumber() != null){
               ResponseEntity<MobileVerificationResponseDto> response = mobileVerificationClient.verifyMobileNumber(customerRequestDto.getPhoneNumber());
               if (response == null || !response.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
                   throw new IllegalArgumentException("Invalid mobile number");
               }

               customer.setPhoneNumber(customerRequestDto.getPhoneNumber());
           }
           return customerRepository.save(customer);
       }



    @Override
    public void deleteCustomer(UUID id) {
        Customer customer=customerRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Customer not found"));
        customerRepository.delete(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
       return customerRepository.findAll();
    }
}

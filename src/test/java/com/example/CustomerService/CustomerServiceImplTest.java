package com.example.CustomerService;

import com.example.CustomerService.client.MobileVerificationClient;
import com.example.CustomerService.dto.CustomerRequestDto;
import com.example.CustomerService.dto.MobileVerificationResponseDto;
import com.example.CustomerService.model.Customer;
import com.example.CustomerService.repository.CustomerRepository;
import com.example.CustomerService.service.impl.CustomerServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerServiceImplTest {

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private MobileVerificationClient mobileVerificationClient;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private CustomerServiceImpl customerService;

    private CustomerRequestDto requestDto;
    private Customer customer;

    @BeforeEach
    void setUp() {
        requestDto = new CustomerRequestDto();
        requestDto.setName("Shari");
        requestDto.setAddress("Beirut");
        requestDto.setPhoneNumber("70123456");

        customer = new Customer(UUID.randomUUID(), "Shari", "Beirut", "70123456");
    }

    @Test
    void addCustomer_ValidPhone_ReturnsCustomer() {
        MobileVerificationResponseDto verificationResponse =
                new MobileVerificationResponseDto(true, "961", "Lebanon", "Alfa");
        ResponseEntity<MobileVerificationResponseDto> responseEntity = ResponseEntity.ok(verificationResponse);

        when(mobileVerificationClient.verifyMobileNumber("70123456")).thenReturn(responseEntity);
        when(modelMapper.map(requestDto, Customer.class)).thenReturn(customer);
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = customerService.addCustomer(requestDto);

        assertNotNull(result);
        assertEquals("Shari", result.getName());
        verify(customerRepository, times(1)).save(any());
    }

    @Test
    void addCustomer_InvalidPhone_ThrowsException() {
        ResponseEntity<MobileVerificationResponseDto> responseEntity = ResponseEntity.badRequest().build();

        when(mobileVerificationClient.verifyMobileNumber("70123456")).thenReturn(responseEntity);

        assertThrows(IllegalArgumentException.class, () -> customerService.addCustomer(requestDto));
    }

    @Test
    void updateCustomer_ValidData_UpdatesAndReturnsCustomer() {
        UUID id = customer.getId();
        CustomerRequestDto updateDto = new CustomerRequestDto();
        updateDto.setName("Updated Name");
        updateDto.setAddress("Updated Address");
        updateDto.setPhoneNumber("78965412");

        MobileVerificationResponseDto verificationResponse = new MobileVerificationResponseDto(true, "961", "Lebanon", "Touch");
        ResponseEntity<MobileVerificationResponseDto> responseEntity = ResponseEntity.ok(verificationResponse);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(mobileVerificationClient.verifyMobileNumber("78965412")).thenReturn(responseEntity);
        when(customerRepository.save(any())).thenReturn(customer);

        Customer updated = customerService.updateCustomer(id, updateDto);

        assertEquals("Updated Name", updated.getName());
        assertEquals("Updated Address", updated.getAddress());
        assertEquals("78965412", updated.getPhoneNumber());
    }

    @Test
    void deleteCustomer_ValidId_DeletesCustomer() {
        UUID id = customer.getId();
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);

        assertDoesNotThrow(() -> customerService.deleteCustomer(id));
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void getAllCustomers_ReturnsCustomerList() {
        List<Customer> customerList = List.of(
                customer,
                new Customer(UUID.randomUUID(), "Ali", "Tripoli", "70119933")
        );

        when(customerRepository.findAll()).thenReturn(customerList);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals("Shari", result.get(0).getName());
        assertEquals("Ali", result.get(1).getName());
    }
}

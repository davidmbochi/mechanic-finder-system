package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.exception.CustomerWithTheProvidedEmailExists;
import com.mechanicfinder.mechanicfindersystem.model.Customer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CustomerService {
    Customer registerCustomer(Customer customer, MultipartFile profileImage) throws IOException, CustomerWithTheProvidedEmailExists;
    Customer findCustomerByEmail(String email);
    Customer saveCustomer(Customer customer);
    Customer findCustomerById(Long id);
    Customer findCustomerByPhoneNumber(String phoneNumber);
    void deleteCustomer(Customer customer);
}

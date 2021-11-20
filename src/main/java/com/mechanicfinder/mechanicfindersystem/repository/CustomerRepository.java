package com.mechanicfinder.mechanicfindersystem.repository;

import com.mechanicfinder.mechanicfindersystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findCustomerByEmail(String email);
    Customer findCustomerById(Long id);
    Customer findCustomerByFirstName(String firstName);
}

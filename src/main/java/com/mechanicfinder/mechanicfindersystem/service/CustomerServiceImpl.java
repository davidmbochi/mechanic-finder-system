package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.exception.CustomerWithTheProvidedEmailExists;
import com.mechanicfinder.mechanicfindersystem.model.AppUser;
import com.mechanicfinder.mechanicfindersystem.model.Customer;
import com.mechanicfinder.mechanicfindersystem.model.Role;
import com.mechanicfinder.mechanicfindersystem.repository.CustomerRepository;
import com.mechanicfinder.mechanicfindersystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final AppUserService appUserService;
    private final RoleRepository roleRepository;

    @Override
    public Customer registerCustomer(Customer customer, MultipartFile profileImage) throws IOException, CustomerWithTheProvidedEmailExists {
        String imageName = StringUtils.cleanPath(profileImage.getOriginalFilename());

        customer.setProfileImage(imageName);

        Customer customerExists = customerRepository.findCustomerByEmail(customer.getEmail());

        if (customerExists == null){
            AppUser appUser = new AppUser(customer.getUsername(),
                    passwordEncoder.encode(customer.getPassword()));

            Role role_customer = roleRepository.findRoleByRoleName("ROLE_CUSTOMER");
            appUser.getRoles().add(role_customer);

            appUserService.createCredentials(appUser);

            customer.setAppUser(appUser);

            Customer registeredCustomer = customerRepository.save(customer);

            uploadImageOrDocument(registeredCustomer, profileImage, imageName);

            return registeredCustomer;
        }else {
            throw new CustomerWithTheProvidedEmailExists("Customer with the email exists");
        }
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer findCustomerById(Long id) {
        return customerRepository.findCustomerById(id);
    }

    @Override
    public Customer findCustomerByPhoneNumber(String phoneNumber) {
        return customerRepository.findCustomerByPhoneNumber(phoneNumber);
    }

    @Override
    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }

    private void uploadImageOrDocument(Customer registeredCustomer,
                                       MultipartFile multipartFile,
                                       String fileName) throws IOException {
        String uploadDirectory =
                StringUtils.
                cleanPath("./customer-images/")+ registeredCustomer.getId();
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        InputStream inputStream = multipartFile.getInputStream();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }
}

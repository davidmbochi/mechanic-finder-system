package com.mechanicfinder.mechanicfindersystem.controller;

import com.mechanicfinder.mechanicfindersystem.exception.*;
import com.mechanicfinder.mechanicfindersystem.model.*;
import com.mechanicfinder.mechanicfindersystem.service.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestMapping("/api/customer")
@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final TaskService taskService;
    private final MechanicService mechanicService;
    private final AppointmentService appointmentService;
    private final AppUserService appUserService;
    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @GetMapping("/register/{id}/{taskName}")
    public String registerCustomer(@PathVariable("id") Long id,
                                   @PathVariable("taskName") String taskName,
                                   Model model){
        Mechanic mechanicById = mechanicService.findMechanicById(id);
        Task taskByTaskName = taskService.findTaskByTaskName(taskName);
        if (! isAuthenticated()){
            model.addAttribute("mechanic",mechanicById);
            model.addAttribute("task",taskByTaskName);
            model.addAttribute("customer",new Customer());
            return "customer-views/customer-reg-form";
        }else {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        AppUser appUserByUserName = appUserService.findAppUserByUserName(name);
        Customer customer = appUserByUserName.getCustomer();
            return "redirect:/api/customer/"+mechanicById.getId()+"/"+taskByTaskName.getTaskName()+"/"+customer.getId();
        }

    }

    @GetMapping("/{id}/{taskName}/{customerId}")
    public String addAppointment(@PathVariable("id") Long id,
                                 @PathVariable("taskName") String taskName,
                                 @PathVariable("customerId") Long customerId,
                                 Model model) throws MultipleAppointmentException {
        model.addAttribute("mechanic",mechanicService.findMechanicById(id));
        model.addAttribute("task",taskService.findTaskByTaskName(taskName));
        model.addAttribute("customer",customerService.findCustomerById(customerId));
        model.addAttribute("appointment",new Appointment());

        return "appointment-views/make-appointment";
    }

    @GetMapping("/customer-signup")
    public String customerSignUpForm(Model model){
        model.addAttribute("customer", new Customer());
        return "customer-views/signup";
    }

    @PostMapping("/process-signup")
    public String processCustomerSignUpForm(@Valid @ModelAttribute("customer") Customer customer,
                                            BindingResult bindingResult,
                                            Model model,
                                            @RequestParam("image") MultipartFile multipartFile) throws CustomerWithTheProvidedEmailExists, IOException {
        if (bindingResult.hasErrors() || multipartFile.isEmpty() || !customer.getPhoneNumber().startsWith("07")){
            return "customer-views/signup";
        }else {
            Customer byEmail = customerService.findCustomerByEmail(customer.getEmail());
            Customer byPhoneNumber = customerService.findCustomerByPhoneNumber(customer.getPhoneNumber());
            Mechanic mechanic = mechanicService.findMechanicByEmail(customer.getEmail());
            Mechanic phoneNumber = mechanicService.findMechanicByPhoneNumber(customer.getPhoneNumber());
            mechanicService.findMechanicByPhoneNumber(customer.getPhoneNumber());
            if ((byEmail != null) || (byPhoneNumber != null) || (mechanic != null) || (phoneNumber != null)){
                throw new CustomerWithTheProvidedEmailExists("Check your email and phone number");
            }else {
                Customer customer1 = customerService.registerCustomer(customer, multipartFile);
                return "redirect:/api/customer/"+customer1.getId();
            }
        }

    }

    @PostMapping("/process-customer-reg-form/{id}/{taskName}")
    public String processCustomerRegForm(@Valid @ModelAttribute("customer") Customer customer,
                                         BindingResult bindingResult,
                                         Model model,
                                         @PathVariable("id") Long id,
                                         @PathVariable("taskName") String taskName,
                                         @RequestParam("image")MultipartFile multipartFile) throws CustomerWithTheProvidedEmailExists, IOException, MechanicWithThatEmailExists, MultipleAppointmentException {
        if (bindingResult.hasErrors() || multipartFile.isEmpty() || !customer.getPhoneNumber().startsWith("07")){
            model.addAttribute("mechanic",mechanicService.findMechanicById(id));
            model.addAttribute("task",taskService.findTaskByTaskName(taskName));
            return "customer-views/customer-reg-form";
        }else {

            Customer customerByEmail = customerService.findCustomerByEmail(customer.getEmail());
            Customer byPhoneNumber = customerService.findCustomerByPhoneNumber(customer.getPhoneNumber());
            Mechanic byEmail = mechanicService.findMechanicByEmail(customer.getEmail());
            Mechanic phoneNumber = mechanicService.findMechanicByPhoneNumber(customer.getPhoneNumber());

            if ((customerByEmail != null) || (byPhoneNumber != null) || (byEmail != null) || (phoneNumber != null)){
                throw new CustomerWithTheProvidedEmailExists("Check your email and phone number");
            }else {

                Customer customer1 = customerService.registerCustomer(customer, multipartFile);

                Mechanic mechanicById = mechanicService.findMechanicById(id);
                Task taskByTaskName = taskService.findTaskByTaskName(taskName);

                return "redirect:/api/customer/"+mechanicById.getId()+"/"+taskByTaskName.getTaskName()+"/"+customer1.getId();

            }

        }
    }

    @GetMapping("/{id}")
    public String findTaskById(@PathVariable("id") Long id, Model model){
        model.addAttribute("customer", customerService.findCustomerById(id));

        return "customer-views/customer-viewport";
    }

    private boolean isAuthenticated(){
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal() instanceof UserDetails;
    }

    @GetMapping("/withdraw/{id}/{customerId}")
    public String withdrawAppointment(@PathVariable("id") Long id,
                                      @PathVariable("customerId") Long customerId){
        Customer customer = customerService.findCustomerById(customerId);
        Appointment appointment = appointmentService.findAppointmentById(id);
        appointmentService.deleteAppointment(appointment);
        return "redirect:/api/customer/"+customer.getId();
    }

    @GetMapping("/edit-customer/{id}")
    public String editCustomer(@PathVariable("id") Long id,
                               Model model){
        model.addAttribute("customer",customerService.findCustomerById(id));
        model.addAttribute("appUser", customerService.findCustomerById(id).getAppUser());
        return "customer-views/edit-customer";
    }

    @PostMapping("/process-update-customer/{id}")
    public String updateCustomer(@Valid @ModelAttribute("customer") Customer customer,
                                 BindingResult bindingResult,
                                 @PathVariable("id") Long id,
                                 Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("customer",customerService.findCustomerById(customer.getId()));
            model.addAttribute("appUser",customerService.findCustomerById(customer.getId()).getAppUser());
            return "customer-views/edit-customer";
        }else {
            Customer saveCustomer = customerService.saveCustomer(customer);
            AppUser user = appUserService.findAppUserById(id);
            saveCustomer.setAppUser(user);
            Customer saveCustomer1 = customerService.saveCustomer(saveCustomer);
            return "redirect:/api/customer/"+saveCustomer1.getId();
        }

    }

    @GetMapping("/delete-customer/{id}")
    public String deleteCustomer(@PathVariable("id") Long id) throws DeleteCustomerException {
        Customer customer = customerService.findCustomerById(id);
        List<Appointment> approved = customer.getAppointments()
                .stream()
                .filter(appointment -> appointment
                        .getAppointmentStatus()
                        .equals("APPROVED"))
                .collect(Collectors.toList());
        if (! approved.isEmpty()){
            throw new DeleteCustomerException("You have an active appointment");
        }else {
            customerService.deleteCustomer(customer);
            return "redirect:/logout";
        }
    }
}

package com.mechanicfinder.mechanicfindersystem.controller;

import com.mechanicfinder.mechanicfindersystem.exception.CustomerWithTheProvidedEmailExists;
import com.mechanicfinder.mechanicfindersystem.exception.MechanicWithThatEmailExists;
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
import java.time.LocalDateTime;

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
        if (! isAuthenticated()){
            model.addAttribute("mechanic",mechanicService.findMechanicById(id));
            model.addAttribute("task",taskService.findTaskByTaskName(taskName));
            model.addAttribute("customer",new Customer());
            return "customer-views/customer-reg-form";
        }else {
            Mechanic mechanicById = mechanicService.findMechanicById(id);
            Task taskByTaskName = taskService.findTaskByTaskName(taskName);
            return "redirect:/api/customer/"+mechanicById.getId()+"/"+taskByTaskName.getTaskName();
        }

    }

    @GetMapping("/{id}/{taskName}")
    public String addAppointment(@PathVariable("id") Long id, @PathVariable("taskName") String taskName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        AppUser appUserByUserName = appUserService.findAppUserByUserName(name);
        Customer customer = appUserByUserName.getCustomer();
        Mechanic mechanicById = mechanicService.findMechanicById(id);
        Task taskByTaskName = taskService.findTaskByTaskName(taskName);

        Appointment appointment = new Appointment();

        appointment.setCustomer(customer);
        appointment.setMechanic(mechanicById);
        appointment.setAppointmentStatus(AppointmentStatus.PENDING);
        appointment.setTask(taskByTaskName);

        addTaskStartAndEndTime(taskByTaskName, appointment);

        appointmentService.bookAppointment(appointment);

        return "redirect:/api/customer/"+customer.getId();
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
        if (bindingResult.hasErrors()){
            return "customer-views/signup";
        }else {
            Customer customer1 = customerService.registerCustomer(customer, multipartFile);
            return "redirect:/api/customer/"+customer1.getId();
        }

    }

    @PostMapping("/process-customer-reg-form/{id}/{taskName}")
    public String processCustomerRegForm(@Valid @ModelAttribute("customer") Customer customer,
                                         BindingResult bindingResult,
                                         Model model,
                                         @PathVariable("id") Long id,
                                         @PathVariable("taskName") String taskName,
                                         @RequestParam("image")MultipartFile multipartFile) throws CustomerWithTheProvidedEmailExists, IOException, MechanicWithThatEmailExists {
        if (bindingResult.hasErrors()){
            return "customer-views/customer-reg-form";
        }else {

            Customer customerByEmail = customerService.findCustomerByEmail(customer.getEmail());

            if (customerByEmail != null){
                throw new CustomerWithTheProvidedEmailExists("choose another email id");
            }else {
                Customer customer1 = customerService.registerCustomer(customer, multipartFile);

                Mechanic mechanicById = mechanicService.findMechanicById(id);

                Task taskByTaskName = taskService.findTaskByTaskName(taskName);

                Appointment appointment = new Appointment();

                appointment.setCustomer(customer1);
                appointment.setMechanic(mechanicById);
                appointment.setAppointmentStatus(AppointmentStatus.PENDING);
                appointment.setTask(taskByTaskName);


                addTaskStartAndEndTime(taskByTaskName, appointment);

                appointmentService.bookAppointment(appointment);

                return "redirect:/api/customer/"+customer1.getId();

            }

        }
    }

    @GetMapping("/{id}")
    public String findTaskById(@PathVariable("id") Long id, Model model){
        model.addAttribute("customer", customerService.findCustomerById(id));

        return "customer-views/customer-viewport";
    }

    private void addTaskStartAndEndTime(Task taskByTaskName, Appointment appointment) {
        Appointment appointment1 = appointmentService
                .findAppointmentByAppointmentDateAndStartTime(
                        appointment.getAppointmentDate(),LocalDateTime.now());
        if (appointment1 == null){
            appointment.setStartTime(LocalDateTime.now());
        }else {
            logger.info("The start time is invalid");
        }

        LocalDateTime endTime = appointment.getStartTime()
                .plusHours(taskByTaskName.getDuration());

        Appointment appointment2 = appointmentService
                .findAppointmentByAppointmentDateAndEndTime(
                        appointment.getAppointmentDate(), endTime
                );

        if (appointment2 == null){
            appointment.setEndTime(endTime);
        }else {
            logger.info("The end time is invalid");
        }
    }

    private boolean isAuthenticated(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails;
    }
}

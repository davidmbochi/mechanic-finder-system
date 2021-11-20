package com.mechanicfinder.mechanicfindersystem.controller;

import com.mechanicfinder.mechanicfindersystem.exception.CustomerWithTheProvidedEmailExists;
import com.mechanicfinder.mechanicfindersystem.model.Appointment;
import com.mechanicfinder.mechanicfindersystem.model.Customer;
import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import com.mechanicfinder.mechanicfindersystem.model.Task;
import com.mechanicfinder.mechanicfindersystem.service.AppointmentService;
import com.mechanicfinder.mechanicfindersystem.service.CustomerService;
import com.mechanicfinder.mechanicfindersystem.service.MechanicService;
import com.mechanicfinder.mechanicfindersystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @GetMapping("/register/{id}/{taskName}")
    public String registerCustomer(@PathVariable("id") Long id,
                                   @PathVariable("taskName") String taskName,
                                   Model model){
        Mechanic mechanicById = mechanicService.findMechanicById(id);
        Task taskByTaskName = taskService.findTaskByTaskName(taskName);
        model.addAttribute("mechanic",mechanicById.getId());
        model.addAttribute("task",taskByTaskName.getTaskName());
        model.addAttribute("customer",new Customer());
        return "customer-views/customer-reg-form";
    }

    @PostMapping("/process-customer-reg-form/{id}/{taskName}")
    public String processCustomerRegForm(@Valid @ModelAttribute("customer") Customer customer,
                                         BindingResult bindingResult,
                                         Model model,
                                         @PathVariable("id") Long id,
                                         @PathVariable("taskName") String taskName,
                                         @RequestParam("image")MultipartFile multipartFile) throws CustomerWithTheProvidedEmailExists, IOException {
        if (bindingResult.hasErrors()){
            return "customer-views/customer-reg-form";
        }else {
            Customer customer1 = customerService.registerCustomer(customer, multipartFile);

            Mechanic mechanicById = mechanicService.findMechanicById(id);

            Task taskByTaskName = taskService.findTaskByTaskName(taskName);

            Appointment appointment = new Appointment();

            appointment.setCustomer(customer1);
            appointment.setMechanic(mechanicById);
            appointment.setTask(taskByTaskName);


            addTaskStartAndEndTime(taskByTaskName, appointment);

            appointmentService.bookAppointment(appointment);

            return "redirect:/api/customer/"+customer1.getId();
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
}

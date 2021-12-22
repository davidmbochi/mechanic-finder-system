package com.mechanicfinder.mechanicfindersystem.controller;

import com.mechanicfinder.mechanicfindersystem.exception.MultipleAppointmentException;
import com.mechanicfinder.mechanicfindersystem.model.*;
import com.mechanicfinder.mechanicfindersystem.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppUserService appUserService;
    private final MechanicService mechanicService;
    private final TaskService taskService;
    private final AppointmentService appointmentService;
    private final CustomerService customerService;

    @PostMapping("/process-appointment/{id}/{taskName}/{customerId}")
    public String processAppointment(@Valid @ModelAttribute("appointment")Appointment appointment,
                                     BindingResult bindingResult,
                                     @PathVariable("id") Long id, @PathVariable("taskName") String taskName,
                                     @PathVariable("customerId") Long customerId,
                                     Model model) throws MultipleAppointmentException {
        if (bindingResult.hasErrors() ||
               !appointment.getEndTime().isAfter(appointment.getStartTime()) ||
                Duration.between(appointment.getStartTime(),appointment.getEndTime()).toHours() < 1 ||
                Duration.between(appointment.getStartTime(),appointment.getEndTime()).toHours() > 8){
            model.addAttribute("mechanic",mechanicService.findMechanicById(id));
            model.addAttribute("task",taskService.findTaskByTaskName(taskName));
            model.addAttribute("customer",customerService.findCustomerById(customerId));
            return "appointment-views/make-appointment";
        }else {

            appointment.setAppointmentDate(LocalDate.now());

            Appointment appointmentExists = appointmentService.findAppointmentByDateAndStartTimeAndEndTime(appointment.getAppointmentDate(),
                    appointment.getStartTime(), appointment.getEndTime());
            if (appointmentExists != null){
                return "appointment-views/make-appointment";
            }

            Customer customer = customerService.findCustomerById(customerId);
            Mechanic mechanicById = mechanicService.findMechanicById(id);
            Task taskByTaskName = taskService.findTaskByTaskName(taskName);



            appointment.setCustomer(customer);
            appointment.setMechanic(mechanicById);
            appointment.setAppointmentStatus(AppointmentStatus.PENDING);
            appointment.setTask(taskByTaskName);

            if (appointmentService.findAppointmentByCustomerAndTask(customer,taskByTaskName) == null){
                appointmentService.bookAppointment(appointment);
            }else {
                throw new MultipleAppointmentException("You have already booked for this service!");
            }

            return "redirect:/api/customer/"+customer.getId();
        }
    }

}

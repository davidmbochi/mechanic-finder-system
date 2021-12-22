package com.mechanicfinder.mechanicfindersystem.controller;

import com.mechanicfinder.mechanicfindersystem.exception.MultipleAppointmentException;
import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import org.bouncycastle.math.raw.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionController {

    @GetMapping("/file-size-limit")
    public String fileSizeLimit(Model model){
        model.addAttribute("message","The file is too large");
        return "exception-views/file-size-limit";
    }

    @GetMapping("/user-exists")
    public String mechanicExists(Model model){
        model.addAttribute("message", "Check your email and phone number, a user seems to exist ! ");
        return "exception-views/user-exists";
    }

    @GetMapping("/multiple-appointment-error")
    public String multipleAppointments(Model model){
        model.addAttribute("message","You have already booked for the service !");
        return "exception-views/multiple-appointments";
    }

    @GetMapping("/delete-task")
    public String deleteTask(Model model){
        model.addAttribute("message","Check pending and approved applications before deleting !");
        return "exception-views/delete-task";
    }

    @GetMapping("/multiple-tasks")
    public String multipleTasks(Model model){
        model.addAttribute("message","This Service is already present !");
        return "exception-views/multiple-tasks";
    }

    @GetMapping("/delete-mechanic-exception")
    public String deleteMechanic(Model model){
        model.addAttribute("message","Check approved appointments before deleting account !");
        return "exception-views/delete-mechanic-exception";
    }

    @GetMapping("/delete-customer-exception")
    public String deleteCustomer(Model model){
        model.addAttribute("message","You have an active appointment");
        return "exception-views/delete-customer-exception";
    }
}

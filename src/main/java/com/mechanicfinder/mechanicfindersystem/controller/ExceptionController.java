package com.mechanicfinder.mechanicfindersystem.controller;

import com.mechanicfinder.mechanicfindersystem.exception.MultipleAppointmentException;
import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
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

    @GetMapping("/mechanic-exists")
    public String mechanicExists(Model model){
        model.addAttribute("mechanic", new Mechanic());
        return "mechanic-views/mechanic-reg-form";
    }

    @GetMapping("/multiple-appointment-error")
    public String multipleAppointments(Model model){
        model.addAttribute("message","You have already booked for the service");
        return "exception-views/multiple-appointments";
    }
}

package com.mechanicfinder.mechanicfindersystem.controller;

import com.mechanicfinder.mechanicfindersystem.exception.MechanicWithThatEmailExists;
import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import com.mechanicfinder.mechanicfindersystem.service.MechanicService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequestMapping("/api/mechanic")
@Controller
@AllArgsConstructor
public class MechanicController {
    private final Logger logger = LoggerFactory.getLogger(MechanicController.class);
    private final MechanicService mechanicService;

    @GetMapping("/all")
    public List<Mechanic> findAllMechanic(){
        return mechanicService.findAllMechanics();
    }

    @GetMapping("/register")
    public String mechanicRegForm(Model model){
        model.addAttribute("mechanic",new Mechanic());
        return "mechanic-views/mechanic-reg-form";
    }

    @PostMapping("/process-mechanic-reg-form")
    public String processMechanicRegForm(@Valid @ModelAttribute("mechanic") Mechanic mechanic,
                                 BindingResult bindingResult,
                                 @RequestParam("image") MultipartFile profileImage,
                                 @RequestParam("pdf") MultipartFile qualification,
                                 Model model) throws IOException, MechanicWithThatEmailExists {

        if (bindingResult.hasErrors()){
            logger.error(String.valueOf(bindingResult.getFieldErrors()));
            return "mechanic-views/mechanic-reg-form";
        }else {
            Mechanic registeredMechanic =
                    mechanicService.registerMechanic(mechanic, profileImage, qualification);

            return "redirect:/api/mechanic/"+registeredMechanic.getId();
        }

    }

    @GetMapping("/{id}")
    public String mechanicProfile(@PathVariable("id") Long id, Model model){
        model.addAttribute("mechanic",
                mechanicService.findMechanicById(id));
        return "mechanic-views/mechanic-viewport";
    }
}

package com.mechanicfinder.mechanicfindersystem.controller;

import com.mechanicfinder.mechanicfindersystem.model.ApplicationStatus;
import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import com.mechanicfinder.mechanicfindersystem.service.MechanicService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final MechanicService mechanicService;

    @GetMapping("/admin-view")
    public String adminView(Model model){
        List<Mechanic> allMechanics = mechanicService.findAllMechanics();
        for (Mechanic allMechanic : allMechanics) {
           logger.info(String.valueOf(allMechanic.getApplicationStatus()));
        }
        model.addAttribute("mechanics",mechanicService.findAllMechanics());
        return "/admin-views/admin-view";
    }

    @GetMapping("/approve/{id}")
    public String approveMechanicApplication(@PathVariable("id") Long id){
        Mechanic mechanicById = mechanicService.findMechanicById(id);
        mechanicById.setApplicationStatus(ApplicationStatus.MEMBER);
        mechanicService.approveMechanic(mechanicById);
        return "redirect:/api/admin/admin-view";
    }

    @GetMapping("/decline/{id}")
    public String declineMechanicApplication(@PathVariable("id") Long id){
        Mechanic mechanicById = mechanicService.findMechanicById(id);
        mechanicById.setApplicationStatus(ApplicationStatus.DECLINED);
        mechanicService.declineMechanic(mechanicById);
        return "redirect:/api/admin/admin-view";
    }
}

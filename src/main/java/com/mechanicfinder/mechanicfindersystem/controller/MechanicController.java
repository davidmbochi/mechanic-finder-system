package com.mechanicfinder.mechanicfindersystem.controller;

import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import com.mechanicfinder.mechanicfindersystem.service.MechanicService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/mechanic")
@RestController
@AllArgsConstructor
public class MechanicController {
    private final MechanicService mechanicService;

    @GetMapping("/all")
    public List<Mechanic> findAllMechanic(){
        return mechanicService.findAllMechanics();
    }
}

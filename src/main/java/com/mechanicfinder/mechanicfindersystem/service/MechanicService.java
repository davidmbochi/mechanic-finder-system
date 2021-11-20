package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.exception.MechanicWithThatEmailExists;
import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MechanicService {
    List<Mechanic> findAllMechanics();
    Mechanic registerMechanic(Mechanic mechanic, MultipartFile profileImage, MultipartFile qualification) throws IOException, MechanicWithThatEmailExists;
    Mechanic findMechanicByEmail(String email);
    Mechanic findMechanicById(Long id);
    Mechanic findMechanicByFirstName(String firstName);
    Mechanic saveMechanic(Mechanic mechanic);
}

package com.mechanicfinder.mechanicfindersystem.repository;

import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
    Mechanic findMechanicByFirstName(String firstName);
    Mechanic findMechanicByEmail(String email);
    Mechanic findMechanicById(Long id);
}

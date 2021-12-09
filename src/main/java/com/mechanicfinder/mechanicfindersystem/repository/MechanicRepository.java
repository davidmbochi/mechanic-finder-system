package com.mechanicfinder.mechanicfindersystem.repository;

import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
    Mechanic findMechanicByFirstName(String firstName);
    Mechanic findMechanicByEmail(String email);
    Mechanic findMechanicById(Long id);
    List<Mechanic>findAllByCreatedAt(LocalDate localDate);
}

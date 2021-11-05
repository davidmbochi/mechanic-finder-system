package com.mechanicfinder.mechanicfindersystem.repository;

import com.mechanicfinder.mechanicfindersystem.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}

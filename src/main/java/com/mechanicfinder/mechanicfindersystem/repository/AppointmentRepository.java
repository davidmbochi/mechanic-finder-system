package com.mechanicfinder.mechanicfindersystem.repository;

import com.mechanicfinder.mechanicfindersystem.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Appointment findAppointmentByStartTime(LocalDateTime localDateTime);
    Appointment findAppointmentByAppointmentDateAndStartTime(LocalDate date, LocalDateTime startTime);
    Appointment findAppointmentByAppointmentDateAndEndTime(LocalDate date, LocalDateTime endTime);
}

package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.model.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    Appointment bookAppointment(Appointment appointment);
    Appointment findAppointmentByStartTime(LocalDateTime localDateTime);
    List<Appointment> findAll();
    Appointment findAppointmentByAppointmentDateAndStartTime(LocalDate date, LocalDateTime startTime);
    Appointment findAppointmentByAppointmentDateAndEndTime(LocalDate date, LocalDateTime endTime);
    Appointment findAppointmentById(Long id);
    Appointment updateAppointmentStatus(Appointment appointment);
}

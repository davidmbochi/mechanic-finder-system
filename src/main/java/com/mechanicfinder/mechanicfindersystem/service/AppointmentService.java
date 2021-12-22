package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.model.Appointment;
import com.mechanicfinder.mechanicfindersystem.model.Customer;
import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import com.mechanicfinder.mechanicfindersystem.model.Task;

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
    Appointment findAppointmentByCustomerAndTask(Customer customer, Task task);
    Appointment findAppointmentByDateAndStartTimeAndEndTime(LocalDate date,
                                                            LocalDateTime startTime,
                                                            LocalDateTime endTime);
    boolean deleteAppointment(Appointment appointment);
}

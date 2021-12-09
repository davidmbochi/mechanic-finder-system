package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.model.Appointment;
import com.mechanicfinder.mechanicfindersystem.model.Customer;
import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import com.mechanicfinder.mechanicfindersystem.model.Task;
import com.mechanicfinder.mechanicfindersystem.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepository appointmentRepository;
    @Override
    public Appointment bookAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment findAppointmentByStartTime(LocalDateTime localDateTime) {
        return appointmentRepository.findAppointmentByStartTime(localDateTime);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment findAppointmentByAppointmentDateAndStartTime(LocalDate date, LocalDateTime startTime) {
        return appointmentRepository
                .findAppointmentByAppointmentDateAndStartTime(date,startTime);
    }

    @Override
    public Appointment findAppointmentByAppointmentDateAndEndTime(LocalDate date, LocalDateTime endTime) {
        return appointmentRepository
                .findAppointmentByAppointmentDateAndEndTime(date,endTime);
    }

    @Override
    public Appointment findAppointmentById(Long id) {
        return appointmentRepository.findAppointmentById(id);
    }

    @Override
    public Appointment updateAppointmentStatus(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment findAppointmentByCustomerAndTask(Customer customer, Task task) {
        return appointmentRepository.findAppointmentByCustomerAndTask(customer,task);
    }

    @Override
    public boolean deleteAppointment(Appointment appointment) {
        appointmentRepository.delete(appointment);
        return true;
    }
}

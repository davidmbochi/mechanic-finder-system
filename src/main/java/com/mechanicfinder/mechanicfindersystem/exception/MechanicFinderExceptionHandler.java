package com.mechanicfinder.mechanicfindersystem.exception;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.bouncycastle.math.raw.Mod;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MechanicFinderExceptionHandler {

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public String fileTooLargeException(){
        return "redirect:/file-size-limit";
    }

    @ExceptionHandler(UserException.class)
    public String mechanicExists(){
        return "redirect:/user-exists";
    }

    @ExceptionHandler(MultipleAppointmentException.class)
    public String multipleAppointments(){
        return "redirect:/multiple-appointment-error";
    }

    @ExceptionHandler(DeleteTaskException.class)
    public String deleteTask(){
        return "redirect:/delete-task";
    }

    @ExceptionHandler(MultipleTasksException.class)
    public String multipleTasks(){
        return "redirect:/multiple-tasks";
    }

    @ExceptionHandler(DeleteMechanicException.class)
    public String deleteMechanic(){
        return "redirect:/delete-mechanic-exception";
    }

    @ExceptionHandler(DeleteCustomerException.class)
    public String deleteCustomer(){
        return "redirect:/delete-customer-exception";
    }
}

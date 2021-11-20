package com.mechanicfinder.mechanicfindersystem.exception;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MechanicFinderExceptionHandler {

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public String fileTooLargeException(){
        return "redirect:/file-size-limit";
    }

    @ExceptionHandler(MechanicWithThatEmailExists.class)
    public String mechanicExists(){
        return "redirect:/mechanic-exists";
    }
}

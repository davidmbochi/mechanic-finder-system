package com.mechanicfinder.mechanicfindersystem.exception;

public class CustomerWithTheProvidedEmailExists extends UserException {
    public CustomerWithTheProvidedEmailExists(String message) {
        super(message);
    }
}

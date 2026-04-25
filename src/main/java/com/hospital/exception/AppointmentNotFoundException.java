package com.hospital.exception;

public class AppointmentNotFoundException extends ResourceNotFoundException {
    public AppointmentNotFoundException(Object id) {
        super("Appointment", id);
    }
}

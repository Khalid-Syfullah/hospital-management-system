package com.hospital.exception;

public class DoctorNotFoundException extends ResourceNotFoundException {
    public DoctorNotFoundException(Object id) {
        super("Doctor", id);
    }
}

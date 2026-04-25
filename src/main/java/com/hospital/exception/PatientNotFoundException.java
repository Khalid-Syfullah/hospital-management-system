package com.hospital.exception;

public class PatientNotFoundException extends ResourceNotFoundException {
    public PatientNotFoundException(Object id) { super("Patient", id); }
}

package com.hospital.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String medicationName, int requested, int available) {
        super("Insufficient stock for " + medicationName + ". Requested: " + requested + ", Available: " + available);
    }
}

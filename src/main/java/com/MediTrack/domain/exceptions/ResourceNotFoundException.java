package com.MediTrack.domain.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String field, String value) {
        super(resource + " no encontrado con " + field + ": " + value);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

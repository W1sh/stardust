package com.w1sh.aperture.core.exception;

public class ComponentCreationException extends RuntimeException {

    public ComponentCreationException(String message) {
        super(message);
    }

    public ComponentCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}

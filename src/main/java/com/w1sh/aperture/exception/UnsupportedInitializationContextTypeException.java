package com.w1sh.aperture.exception;

public class UnsupportedInitializationContextTypeException extends RuntimeException {
    public UnsupportedInitializationContextTypeException() {
        super("Unsupported InitializationContext type");
    }
}

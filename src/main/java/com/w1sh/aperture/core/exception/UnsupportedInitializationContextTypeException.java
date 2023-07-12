package com.w1sh.aperture.core.exception;

public class UnsupportedInitializationContextTypeException extends RuntimeException {
    public UnsupportedInitializationContextTypeException() {
        super("Unsupported InitializationContext type");
    }
}

package com.w1sh.aperture.exception;

public class ProviderInitializationException extends RuntimeException {

    public ProviderInitializationException(String message) {
        super(message);
    }

    public ProviderInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ProviderInitializationException required(String name) {
        return new ProviderInitializationException(String.format("No candidate found for required parameter %s", name));
    }
}

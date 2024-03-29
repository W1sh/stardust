package com.w1sh.stardust.exception;

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

    public static ProviderInitializationException invalidPropertyType() {
        return new ProviderInitializationException("Cannot resolve property. Field annotated as property must be of type String.");
    }
}

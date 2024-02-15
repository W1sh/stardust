package com.w1sh.stardust.exception;

public class ProviderRegistrationException extends RuntimeException {

    public ProviderRegistrationException(String message) {
        super(message);
    }

    public static ProviderRegistrationException malformedProbe(Class<?> clazz) {
        return new ProviderRegistrationException(String.format("Failed to register probe of type %s" +
                " as no @Probe annotation is present in the class", clazz.getSimpleName()));
    }

    public static ProviderRegistrationException notAllowedName(String name) {
        return new ProviderRegistrationException(String.format("Failed to register provider with name %s as another " +
                "provider with same name is already present and overriding is not allowed", name));
    }

    public static ProviderRegistrationException multipleConstructors(Class<?> clazz) {
        return new ProviderRegistrationException(String.format("%s has multiple constructors annotated with @Inject",
                clazz.getSimpleName()));
    }

    public static ProviderRegistrationException noConstructor(Class<?> clazz) {
        return new ProviderRegistrationException(String.format("%s doesn't have a constructor annotated with @Inject" +
                " or a no-arg constructor", clazz.getSimpleName()));
    }
}

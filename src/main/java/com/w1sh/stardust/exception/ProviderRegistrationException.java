package com.w1sh.stardust.exception;

public class ProviderRegistrationException extends RuntimeException {

    public ProviderRegistrationException(String message) {
        super(message);
    }

    public ProviderRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ProviderRegistrationException notAllowedClass(Class<?> clazz) {
        return new ProviderRegistrationException(String.format("Failed to register provider for class %s as another " +
                "provider for same class is already present and overriding is not allowed", clazz.getSimpleName()));
    }

    public static ProviderRegistrationException notAllowedName(String name) {
        return new ProviderRegistrationException(String.format("Failed to register provider with name %s as another " +
                "provider with same name is already present and overriding is not allowed", name));
    }

    public static ProviderRegistrationException multipleConstructors(Class<?> clazz) {
        return new ProviderRegistrationException(String.format("%s has multiple eligible constructors",
                clazz.getSimpleName()));
    }

    public static ProviderRegistrationException multipleInjectConstructors(Class<?> clazz) {
        return new ProviderRegistrationException(String.format("%s has multiple constructors annotated with @Inject",
                clazz.getSimpleName()));
    }

    public static ProviderRegistrationException noDeclaredConstructors(Class<?> clazz) {
        return new ProviderRegistrationException(String.format("%s doesn't have a any declared constructors and cannot" +
                " be initialized. This is likely an interface, a primitive type, an array class, or void.", clazz.getSimpleName()));
    }
}

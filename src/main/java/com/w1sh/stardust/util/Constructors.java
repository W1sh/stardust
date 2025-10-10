package com.w1sh.stardust.util;

import com.w1sh.stardust.annotation.Inject;
import com.w1sh.stardust.exception.ProviderRegistrationException;

import java.lang.reflect.Constructor;

public class Constructors {

    private Constructors() {}

    /**
     * Returns the constructor to be used for injecting, in case no constructor is defined with the @inject annotation,
     * the default constructor will be used. If multiple constructors are eligible to be injected, an exception will
     * be thrown.
     *
     * @param clazz The class to retrieve the constructor from.
     * @return The constructor to be injected.
     * @param <T> The type of the constructor.
     * @throws ProviderRegistrationException if the class has no constructors, or multiple eligible constructors.
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getInjectConstructor(Class<T> clazz) {
        if (clazz.getDeclaredConstructors().length == 0) {
            throw ProviderRegistrationException.noDeclaredConstructors(clazz);
        }

        Constructor<T> injectConstructor = null;
        for (Constructor<?> declaredConstructor : clazz.getDeclaredConstructors()) {
            if (declaredConstructor.isAnnotationPresent(Inject.class)) {
                if (injectConstructor != null) {
                    throw ProviderRegistrationException.multipleInjectConstructors(clazz);
                }
                injectConstructor = (Constructor<T>) declaredConstructor;
            }
        }

        if (injectConstructor == null) {
            if (clazz.getDeclaredConstructors().length > 1) {
                throw ProviderRegistrationException.multipleConstructors(clazz);
            } else {
                return (Constructor<T>) clazz.getDeclaredConstructors()[0];
            }
        }
        return injectConstructor;
    }
}

package com.w1sh.aperture.core.util;

import com.w1sh.aperture.core.annotation.Inject;
import com.w1sh.aperture.core.exception.ProviderRegistrationException;

import java.lang.reflect.Constructor;

public class Constructors {

    private Constructors() {}

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> findInjectAnnotatedConstructor(Class<T> clazz) {
        Constructor<T> injectConstructor = null;
        for (Constructor<?> declaredConstructor : clazz.getDeclaredConstructors()) {
            if (declaredConstructor.isAnnotationPresent(Inject.class)) {
                if (injectConstructor != null) {
                    throw ProviderRegistrationException.multipleConstructors(clazz);
                }
                injectConstructor = (Constructor<T>) declaredConstructor;
            }
        }

        if (injectConstructor == null) {
            try {
                return clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                throw ProviderRegistrationException.noConstructor(clazz);
            }
        }
        return injectConstructor;
    }
}

package com.w1sh.stardust.util;

import com.w1sh.stardust.annotation.Inject;
import com.w1sh.stardust.exception.ProviderRegistrationException;

import java.lang.reflect.Constructor;

public class Constructors {

    private Constructors() {}

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getInjectConstructor(Class<T> clazz) {
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

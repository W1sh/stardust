package com.w1sh.aperture.core;

import com.w1sh.aperture.core.annotation.Inject;
import com.w1sh.aperture.core.builder.Options;
import com.w1sh.aperture.core.exception.ComponentCreationException;
import com.w1sh.aperture.core.exception.ProviderRegistrationException;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ConstructorInitializationContext<T> extends InitializationContext<T> {

    private final Constructor<T> constructor;
    private final Set<Class<?>> initializationChain;
    private final boolean isProvider;

    public ConstructorInitializationContext(Class<T> clazz, Options options) {
        super(clazz, options);
        this.constructor = findInjectAnnotatedConstructor();
        this.initializationChain = new HashSet<>();
        this.isProvider = false;
        this.initializationChain.add(clazz);
    }

    public String getName() {
        return (getOptions().name() != null && !getOptions().name().isBlank()) ? getOptions().name() : null;
    }

    public String chainAsString() {
        return initializationChain.stream()
                .map(chainClazz -> chainClazz.getSimpleName() + " -> ")
                .collect(Collectors.joining("", "", getClazz().getSimpleName()));
    }

    private Constructor<T> findInjectAnnotatedConstructor() {
        Constructor<T> injectConstructor = null;
        for (Constructor<?> declaredConstructor : getClazz().getDeclaredConstructors()) {
            if (declaredConstructor.isAnnotationPresent(Inject.class)) {
                if (injectConstructor != null) {
                    throw ProviderRegistrationException.multipleConstructors(getClazz());
                }
                injectConstructor = (Constructor<T>) declaredConstructor;
            }
        }

        if (injectConstructor == null) {
            try {
                return getClazz().getConstructor();
            } catch (NoSuchMethodException e) {
                throw ProviderRegistrationException.noConstructor(getClazz());
            }
        }
        return injectConstructor;
    }

    public Constructor<T> getConstructor() {
        return constructor;
    }

    public Set<Class<?>> getChain() {
        return initializationChain;
    }

    public boolean isProvider() {
        return isProvider;
    }
}

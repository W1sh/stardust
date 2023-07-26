package com.w1sh.aperture;

import com.w1sh.aperture.binding.*;
import com.w1sh.aperture.exception.ComponentCreationException;
import com.w1sh.aperture.exception.ProviderInitializationException;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class ParameterResolver {

    private final ProviderContainer container;

    public ParameterResolver(ProviderContainer container) {
        this.container = container;
    }

    public Object resolve(ResolvableParameter<?> parameter) {
        Objects.requireNonNull(parameter);
        if (Map.class.isAssignableFrom(parameter.getActualType())) {
            throw new UnsupportedOperationException();
        } else if (Collection.class.isAssignableFrom(parameter.getActualType())) {
            throw new UnsupportedOperationException();
        } else if (parameter.getActualType().isArray()) {
            throw new UnsupportedOperationException();
        } else if (Binding.class.isAssignableFrom(parameter.getActualType())) {
            return resolveParameterizedType(parameter);
        } else {
            return resolveObject(parameter);
        }
    }

    private Object resolveObject(ResolvableParameter<?> parameter) {
        final String qualifier = parameter.getQualifier();
        final ObjectProvider<?> provider = qualifier != null ? container.provider(qualifier) : container.provider(parameter.getActualType());

        if (provider == null) {
            if (Boolean.TRUE.equals(parameter.isRequired())) {
                throw ProviderInitializationException.required(parameter.getActualType().getSimpleName());
            }
        } else {
            return provider.singletonInstance();
        }
        return null;
    }

    private Object resolveParameterizedType(ResolvableParameter<?> parameter) {
        final ParameterizedType type = parameter.getParameterizedType();
        final Class<?> parameterizedClazz = (Class<?>) type.getActualTypeArguments()[0];
        final String qualifier = parameter.getQualifier();
        final ObjectProvider<?> provider = qualifier != null ? container.provider(qualifier) : container.provider(parameterizedClazz);
        if (type.getRawType().equals(Lazy.class)) {
            return LazyBinding.of(provider);
        }
        if (type.getRawType().equals(Provider.class)) {
            return ProviderBinding.of(provider);
        }
        throw new ComponentCreationException(String.format("Unknown binding expectedType %s", type.getRawType()));
    }
}

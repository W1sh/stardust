package com.w1sh.stardust;

import com.w1sh.stardust.annotation.Introspect;
import com.w1sh.stardust.binding.*;
import com.w1sh.stardust.exception.ComponentCreationException;
import com.w1sh.stardust.exception.ProviderInitializationException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ParameterResolver {

    private static final Map<Type, Function<ObjectProvider<?>, ? extends Binding<?>>> bindingResolvers = new ConcurrentHashMap<>(8);
    private final ProviderContainer container;

    public ParameterResolver(ProviderContainer container) {
        this.container = container;
        this.addBindingResolver(Lazy.class, LazyBinding::of);
        this.addBindingResolver(Provider.class, ProviderBinding::of);
    }

    public <T extends Binding<?>> void addBindingResolver(Class<T> bindingClass, Function<ObjectProvider<?>, T> bindingResolver) {
        bindingResolvers.put(bindingClass, bindingResolver);
    }

    public Object resolve(ResolvableParameter<?> parameter) {
        Objects.requireNonNull(parameter);
        if (Collection.class.isAssignableFrom(parameter.getActualType()) && parameter.isAnnotationPresent(Introspect.class)) {
            return resolveCollection(parameter);
        } else if (parameter.getActualType().isArray()) {
            return resolveArrayType(parameter);
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
            return null;
        } else {
            return provider.singletonInstance();
        }
    }

    private Object resolveParameterizedType(ResolvableParameter<?> parameter) {
        final ParameterizedType type = parameter.getParameterizedType();
        final Class<?> parameterizedClazz = (Class<?>) type.getActualTypeArguments()[0];
        final String qualifier = parameter.getQualifier();
        final ObjectProvider<?> provider = qualifier != null ? container.provider(qualifier) : container.provider(parameterizedClazz);
        if (provider == null && Boolean.TRUE.equals(parameter.isRequired())) {
            throw ProviderInitializationException.required(parameter.getActualType().getSimpleName());
        }
        if (bindingResolvers.containsKey(type.getRawType())) {
            return bindingResolvers.get(type.getRawType()).apply(provider);
        } else {
            throw new ComponentCreationException(String.format("No known resolver for binding %s", type.getRawType()));
        }
    }

    private Object[] resolveArrayType(ResolvableParameter<?> parameter) {
        final var arrayType = parameter.getActualType().componentType();
        List<?> instances = container.instances(arrayType);
        return instances.toArray();
    }

    private Collection<Object> resolveCollection(ResolvableParameter<?> parameter) {
        if (!List.class.isAssignableFrom(parameter.getActualType()) && !Set.class.isAssignableFrom(parameter.getActualType())) {
            throw new UnsupportedOperationException("Collection type not supported, use either List or Set");
        }
        final ParameterizedType type = parameter.getParameterizedType();
        final Class<?> parameterizedClazz = (Class<?>) type.getActualTypeArguments()[0];
        List<?> instances = container.instances(parameterizedClazz);
        return List.class.isAssignableFrom(parameter.getActualType()) ? List.copyOf(instances) : Set.copyOf(instances);
    }
}

package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.core.annotation.Nullable;
import com.w1sh.wave.core.binding.Lazy;
import com.w1sh.wave.core.binding.LazyBinding;
import com.w1sh.wave.core.binding.Provider;
import com.w1sh.wave.core.binding.ProviderBinding;
import com.w1sh.wave.core.exception.ComponentCreationException;
import com.w1sh.wave.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class WaveContext {

    private static final Logger logger = LoggerFactory.getLogger(WaveContext.class);

    private final Map<Class<?>, ObjectProvider<?>> providers = new ConcurrentHashMap<>(256);

    public <T> ObjectProvider<T> getProvider(Class<T> clazz, boolean nullable) {
        return null;
    }

    protected Constructor<?> findInjectAnnotatedConstructor(Class<?> aClass) {
        Constructor<?> injectConstructor = null;
        for (Constructor<?> constructor : aClass.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                if (injectConstructor != null) throw new ComponentCreationException(String.format(
                        "%s has multiple constructors annotated with @Inject", aClass.getName()));
                injectConstructor = constructor;
            }
        }

        if (injectConstructor == null) {
            try {
                return aClass.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new ComponentCreationException(String.format(
                        "%s doesn't have a constructor annotated with @Inject or a no-arg constructor", aClass.getName()));
            }
        }
        return injectConstructor;
    }

    /**
     * Invokes all the methods annotated with {@link javax.annotation.PostConstruct} annotation for the given instance.
     *
     * @param instance   The object instance to invoke the methods on.
     */
    protected void processPostConstructorMethods(Object instance) {
        final Deque<Method> postConstructMethods = new LinkedList<>();
        for (Class<?> clazz = instance.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(PostConstruct.class)) {
                    postConstructMethods.addFirst(method);
                }
            }
        }

        for (Method m : postConstructMethods) {
            try {
                m.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ComponentCreationException(String.format(
                        "Can't invoke @PostConstruct annotated method %s:%s", m.getClass(), m.getName()), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> ObjectProvider<T> createObjectProvider(Class<T> clazz) {
        final Supplier<T> supplier = () -> {
            final var instance = createInstance(clazz);
            processPostConstructorMethods(instance);
            return (T) instance;
        };
        return new SimpleObjectProvider<>(supplier);
    }

    @SuppressWarnings("unchecked")
    private <T> Object createInstance(Class<T> clazz) {
        final Constructor<T> constructor = (Constructor<T>) findInjectAnnotatedConstructor(clazz);
        if (constructor.getParameterTypes().length == 0) {
            logger.debug("Creating new instance of class {}", clazz.getName());
            return ReflectionUtils.newInstance(constructor, new Object[]{});
        }

        final Object[] params = new Object[constructor.getParameterTypes().length];
        for (int i = 0; i < constructor.getParameterTypes().length; i++) {
            final Type paramType = constructor.getGenericParameterTypes()[i];

            if (isWrappedInBinding(paramType)) {
                params[i] = createBinding((ParameterizedType) paramType);
            } else {
                final Annotation[] metadata = constructor.getParameters()[i].getDeclaredAnnotations();
                final boolean nullable = Arrays.stream(metadata)
                        .anyMatch(annotation -> annotation.annotationType().equals(Nullable.class));
                params[i] = getProvider((Class<?>) paramType, nullable).singletonInstance();
            }
        }
        return ReflectionUtils.newInstance(constructor, params);
    }

    private boolean isWrappedInBinding(Type type) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = ((ParameterizedType) type);
            return parameterizedType.getRawType().equals(Lazy.class) || parameterizedType.getRawType().equals(Provider.class);
        }
        return false;
    }

    private Object createBinding(ParameterizedType type) {
        final Class<?> parameterizedClazz = (Class<?>) type.getActualTypeArguments()[0];
        if (type.getRawType().equals(Lazy.class)) {
            return new LazyBinding<>(providers.get(parameterizedClazz));
        }
        if (type.getRawType().equals(Provider.class)) {
            return new ProviderBinding<>(providers.get(parameterizedClazz));
        }
        throw new ComponentCreationException(String.format("Unknown provider type %s", type.getRawType()));
    }
}

package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.core.annotation.Nullable;
import com.w1sh.wave.core.annotation.Primary;
import com.w1sh.wave.core.annotation.Qualifier;
import com.w1sh.wave.core.binding.Lazy;
import com.w1sh.wave.core.binding.LazyBinding;
import com.w1sh.wave.core.binding.Provider;
import com.w1sh.wave.core.binding.ProviderBinding;
import com.w1sh.wave.core.builder.ContextBuilder;
import com.w1sh.wave.core.builder.ContextGroup;
import com.w1sh.wave.core.exception.ComponentCreationException;
import com.w1sh.wave.core.exception.UnsatisfiedComponentException;
import com.w1sh.wave.util.Annotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class WaveContext {

    private static final Logger logger = LoggerFactory.getLogger(WaveContext.class);

    private final Map<Key<?>, ObjectProvider<?>> providers = new ConcurrentHashMap<>(256);
    private final Map<Key<?>, Object> singletons = new ConcurrentHashMap<>(256);

    public WaveContext context(ContextGroup contextGroup) {
        ContextBuilder.setStaticContext(this);
        contextGroup.apply();
        ContextBuilder.clearStaticContext();
        return this;
    }

    public void register(Class<?> clazz) {
        final ObjectProvider<?> objectProvider = createObjectProvider(clazz);
        final boolean primary = Annotations.isAnnotationPresent(clazz, Primary.class);
        final String name = clazz.getPackageName() + "." + clazz.getSimpleName();
        providers.put(Key.of(clazz, name, primary), objectProvider);
    }

    public void register(String name, Class<?> clazz) {
        final ObjectProvider<?> objectProvider = createObjectProvider(clazz);
        final boolean primary = Annotations.isAnnotationPresent(clazz, Primary.class);
        providers.put(Key.of(clazz, name, primary), objectProvider);
    }

    public void register(Class<?> clazz, Object instance) {
        register(clazz, false, instance);
    }

    public void register(Class<?> clazz, boolean primary, Object instance) {
        final String name = clazz.getPackageName() + "." + clazz.getSimpleName();
        singletons.put(Key.of(clazz, name, primary), instance);
    }

    public void register(String name, Object instance) {
        register(name, false, instance);
    }

    public void register(String name, boolean primary, Object instance) {
        singletons.put(Key.of(instance.getClass(), name, primary), instance);
    }

    @SuppressWarnings("unchecked")
    public <T> ObjectProvider<T> getProvider(Class<T> clazz, boolean nullable) {
        ObjectProvider<T> primaryCandidate = null;
        final List<ObjectProvider<T>> candidates = new ArrayList<>();
        for (Entry<Key<?>, ObjectProvider<?>> scopeClazz : providers.entrySet()) {
            if (clazz.isAssignableFrom(scopeClazz.getKey().getType())) {
                if (scopeClazz.getKey().isPrimary()) {
                    if (primaryCandidate != null) throw new UnsatisfiedComponentException(
                            "Multiple primary injection candidates found for class " + clazz);
                    primaryCandidate = (ObjectProvider<T>) scopeClazz.getValue();
                }
                candidates.add((ObjectProvider<T>) scopeClazz.getValue());
            }
        }

        if (primaryCandidate != null) {
            return primaryCandidate;
        }

        if (candidates.isEmpty()) {
            if (nullable) return null;
            logger.error("No injection candidate found for class {}", clazz);
            throw new UnsatisfiedComponentException("No injection candidate found for class " + clazz);
        } else if (candidates.size() > 1) {
            logger.error("Multiple injection candidates found for class {}", clazz);
            throw new UnsatisfiedComponentException("Multiple injection candidates found for class " + clazz);
        }
        return candidates.get(0);
    }

    @SuppressWarnings("unchecked")
    public <T> ObjectProvider<T> getProvider(String name, boolean nullable) {
        final List<ObjectProvider<T>> candidates = new ArrayList<>();
        for (Entry<Key<?>, ObjectProvider<?>> scopeClazz : providers.entrySet()) {
            if (scopeClazz.getKey().getName().equalsIgnoreCase(name)) {
                candidates.add((ObjectProvider<T>) scopeClazz.getValue());
            }
        }

        if (candidates.isEmpty()) {
            if (nullable) return null;
            logger.error("No injection candidate found for qualifier {}", name);
            throw new UnsatisfiedComponentException("No injection candidate found for qualifier " + name);
        } else if (candidates.size() > 1) {
            logger.error("Multiple injection candidates found for qualifier {}", name);
            throw new UnsatisfiedComponentException("Multiple injection candidates found for qualifier " + name);
        }
        return candidates.get(0);
    }

    @SuppressWarnings("unchecked")
    protected <T> Constructor<T> findInjectAnnotatedConstructor(Class<T> aClass) {
        Constructor<T> injectConstructor = null;
        for (Constructor<?> constructor : aClass.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                if (injectConstructor != null) throw new ComponentCreationException(String.format(
                        "%s has multiple constructors annotated with @Inject", aClass.getName()));
                injectConstructor = (Constructor<T>) constructor;
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
     * @param instance The object instance to invoke the methods on.
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
    protected <T> ObjectProvider<T> createObjectProvider(Class<T> clazz) {
        return new SimpleObjectProvider<>(() -> {
            final var instance = createInstance(clazz);
            processPostConstructorMethods(instance);
            return (T) instance;
        });
    }

    private <T> Object createInstance(Class<T> clazz) {
        final Constructor<T> constructor = findInjectAnnotatedConstructor(clazz);
        if (constructor.getParameterTypes().length == 0) {
            logger.debug("Creating new instance of class {}", clazz.getName());
            return newInstance(constructor, new Object[]{});
        }

        final Object[] params = new Object[constructor.getParameterTypes().length];
        for (int i = 0; i < constructor.getParameterTypes().length; i++) {
            final Type paramType = constructor.getGenericParameterTypes()[i];
            final Parameter parameter = constructor.getParameters()[i];

            if (isWrappedInBinding(paramType)) {
                params[i] = createBinding((ParameterizedType) paramType);
            } else {
                final boolean nullable = parameter.isAnnotationPresent(Nullable.class);
                if (parameter.isAnnotationPresent(Qualifier.class)) {
                    final String name = parameter.getAnnotation(Qualifier.class).name();
                    params[i] = getProvider(name, nullable);
                } else {
                    params[i] = getProvider((Class<?>) paramType, nullable).singletonInstance();
                }
            }
        }
        return newInstance(constructor, params);
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
        final ObjectProvider<?> provider = getProvider(parameterizedClazz, false);
        if (type.getRawType().equals(Lazy.class)) {
            return new LazyBinding<>(provider);
        }
        if (type.getRawType().equals(Provider.class)) {
            return new ProviderBinding<>(provider);
        }
        throw new ComponentCreationException(String.format("Unknown binding type %s", type.getRawType()));
    }

    private <T> T newInstance(Constructor<T> constructor, Object[] params) {
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ComponentCreationException("Unable to create an instance of the class", e);
        }
    }
}

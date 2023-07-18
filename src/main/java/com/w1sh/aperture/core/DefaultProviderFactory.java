package com.w1sh.aperture.core;

import com.w1sh.aperture.core.annotation.Qualifier;
import com.w1sh.aperture.core.annotation.Required;
import com.w1sh.aperture.core.binding.*;
import com.w1sh.aperture.core.exception.CircularDependencyException;
import com.w1sh.aperture.core.exception.ComponentCreationException;
import com.w1sh.aperture.core.exception.ProviderInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Set;

public class DefaultProviderFactory implements ProviderFactory {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProviderFactory.class);

    private final ProviderRegistry registry;
    private final PostConstructorProcessor postConstructorProcessor;

    public DefaultProviderFactory(ProviderRegistry registry) {
        this.registry = registry;
        this.postConstructorProcessor = new JakartaPostConstructProcessor();
    }

    @Override
    public <T> ObjectProvider<T> newProvider(Definition<T> definition) {
        if (Scope.SINGLETON.equals(definition.getMetadata().scope())) {
            if (definition instanceof ClassDefinition<T> classDefinition) {
                T instance = createInstance(classDefinition);
                postConstructorProcessor.process(instance);
                return new SingletonObjectProvider<>(instance);
            } else if (definition instanceof ModuleMethodDefinition<T> methodDefinition) {
                return new SingletonObjectProvider<>(methodDefinition.getSupplier().get());
            }
        } else {
            if (definition instanceof ClassDefinition<T> classDefinition) {
                return new PrototypeObjectProvider<>(() -> {
                    T instance = createInstance(classDefinition);
                    postConstructorProcessor.process(instance);
                    return instance;
                });
            } else if (definition instanceof ModuleMethodDefinition<T> methodDefinition) {
                return new PrototypeObjectProvider<>(methodDefinition.getSupplier());
            }
        }
        throw new ProviderInitializationException(String.format("Definition type %s not supported by default provider factory",
                definition.getClass().getSimpleName()));
    }

    private <T> T createInstance(ClassDefinition<T> context) {
        final Constructor<T> constructor = context.getConstructor();

        if (constructor.getParameterTypes().length == 0) {
            return newInstance(constructor, new Object[]{});
        }

        if (containsCircularDependency(constructor, context.getChain())) {
            throw new CircularDependencyException(context);
        }

        final Object[] params = new Object[constructor.getParameterTypes().length];
        for (int i = 0; i < constructor.getParameterTypes().length; i++) {
            final Type paramType = constructor.getGenericParameterTypes()[i];
            final Class<?> actualParameterType = getActualParameterType(paramType);
            final String qualifier = getParameterQualifier(constructor.getParameters()[i]);
            final ObjectProvider<?> provider = qualifier != null ? registry.provider(qualifier) : registry.provider(actualParameterType);

            if (provider == null) {
                if (constructor.getParameters()[i].isAnnotationPresent(Required.class)) {
                    throw ProviderInitializationException.required(actualParameterType.getSimpleName());
                }
            } else {
                if (isWrappedInBinding(paramType)) {
                    params[i] = createBinding((ParameterizedType) paramType);
                } else {
                    params[i] = provider.singletonInstance();
                }
            }
        }
        return newInstance(constructor, params);
    }

    /**
     * private Object resolveParameter(Parameter parameter) {
     * <p>
     * }
     **/

    private <T> boolean containsCircularDependency(Constructor<T> constructor, Set<Class<?>> chain) {
        return Arrays.stream(constructor.getParameterTypes())
                .anyMatch(chain::contains);
    }

    private Class<?> getActualParameterType(Type paramType) {
        return isWrappedInBinding(paramType) ? (Class<?>) ((ParameterizedType) paramType).getActualTypeArguments()[0] : (Class<?>) paramType;
    }

    private String getParameterQualifier(Parameter parameter) {
        if (parameter.isAnnotationPresent(Qualifier.class) && !parameter.getAnnotation(Qualifier.class).name().isBlank()) {
            return parameter.getAnnotation(Qualifier.class).name();
        } else return null;
    }

    private boolean isWrappedInBinding(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            return parameterizedType.getRawType().equals(Lazy.class) || parameterizedType.getRawType().equals(Provider.class);
        }
        return false;
    }

    private Binding<?> createBinding(ParameterizedType type) {
        final Class<?> parameterizedClazz = (Class<?>) type.getActualTypeArguments()[0];
        final ObjectProvider<?> provider = registry.provider(parameterizedClazz);
        if (type.getRawType().equals(Lazy.class)) {
            return new LazyBinding<>(provider);
        }
        if (type.getRawType().equals(Provider.class)) {
            return new ProviderBinding<>(provider);
        }
        throw new ComponentCreationException(String.format("Unknown binding expectedType %s", type.getRawType()));
    }

    private <T> T newInstance(Constructor<T> constructor, Object[] params) {
        try {
            logger.debug("Creating new instance of class {}", constructor.getDeclaringClass());
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ProviderInitializationException("Unable to create an instance of the class", e);
        }
    }
}

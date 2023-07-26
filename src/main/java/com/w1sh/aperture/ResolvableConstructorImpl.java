package com.w1sh.aperture;

import com.w1sh.aperture.annotation.Primary;
import com.w1sh.aperture.annotation.Profile;
import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.exception.ProviderInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ResolvableConstructorImpl<S> implements ResolvableExecutable<S> {

    private static final Logger logger = LoggerFactory.getLogger(ResolvableConstructorImpl.class);

    private final Constructor<?> constructor;
    private final Set<ResolvableParameter<?>> parameters;

    public ResolvableConstructorImpl(Constructor<?> constructor) {
        this.constructor = constructor;
        Set<ResolvableParameter<?>> set = new HashSet<>();
        Parameter[] methodParameters = constructor.getParameters();
        for (int i = 0, methodParametersLength = methodParameters.length; i < methodParametersLength; i++) {
            Parameter parameter = methodParameters[i];
            ResolvableParameterImpl<?> resolvableParameterImpl = new ResolvableParameterImpl<>(parameter, i, constructor);
            set.add(resolvableParameterImpl);
        }
        this.parameters = set;
    }

    @Override
    public Class<S> getActualType() {
        return (Class<S>) constructor.getDeclaringClass();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return getActualType().getAnnotation(annotationType);
    }

    @Override
    public <T extends Annotation> Set<T> getAnnotations(Class<T> annotationType) {
        return Set.of(getActualType().getAnnotationsByType(annotationType));
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return getActualType().isAnnotationPresent(annotationType);
    }

    @Override
    public Integer getPriority() {
        Priority annotation = getAnnotation(Priority.class);
        return annotation != null ? annotation.value() : 0;
    }

    @Override
    public Set<String> getActiveProfiles() {
        Profile annotation = getAnnotation(Profile.class);
        return (annotation != null && annotation.value() != null) ? Set.of(annotation.value()) : Set.of();
    }

    @Override
    public Boolean isPrimary() {
        return isAnnotationPresent(Primary.class);
    }

    @Override
    public Scope getScope() {
        Provide annotation = getAnnotation(Provide.class);
        Objects.requireNonNull(annotation);
        return annotation.scope();
    }

    @Override
    public String getName() {
        Provide annotation = getAnnotation(Provide.class);
        Objects.requireNonNull(annotation);
        return !annotation.value().isBlank() ? annotation.value() : null;
    }

    @Override
    public Set<ResolvableParameter<?>> getParameters() {
        return parameters;
    }

    @Override
    public Object resolve(Object[] args) {
        try {
            logger.debug("Creating new instance of class {}", constructor.getDeclaringClass().getSimpleName());
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ProviderInitializationException("Unable to create an instance of the class", e);
        }
    }
}

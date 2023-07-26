package com.w1sh.aperture;

import com.w1sh.aperture.annotation.Primary;
import com.w1sh.aperture.annotation.Profile;
import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.exception.ProviderInitializationException;

import javax.annotation.Priority;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ResolvableMethodImpl<S> implements ResolvableExecutable<S> {

    private final Method method;
    private final Set<ResolvableParameter<?>> parameters;
    private final Object declaringClassInstance;

    public ResolvableMethodImpl(Method method, Object declaringClassInstance) {
        this.method = method;
        this.declaringClassInstance = declaringClassInstance;
        Set<ResolvableParameter<?>> set = new HashSet<>();
        Parameter[] methodParameters = method.getParameters();
        for (Parameter parameter : methodParameters) {
            ResolvableParameterImpl<?> resolvableParameterImpl = new ResolvableParameterImpl<>(parameter);
            set.add(resolvableParameterImpl);
        }
        this.parameters = set;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<S> getActualType() {
        return (Class<S>) method.getReturnType();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        Objects.requireNonNull(annotationType);
        return method.getAnnotation(annotationType);
    }

    @Override
    public <T extends Annotation> Set<T> getAnnotations(Class<T> annotationType) {
        Objects.requireNonNull(annotationType);
        return Set.of(method.getAnnotationsByType(annotationType));
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        Objects.requireNonNull(annotationType);
        return method.isAnnotationPresent(annotationType);
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
        return !annotation.value().isBlank() ? annotation.value() : method.getName();
    }

    @Override
    public Set<ResolvableParameter<?>> getParameters() {
        return parameters;
    }

    @Override
    public Object resolve(Object[] args) {
        try {
            return method.invoke(declaringClassInstance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ProviderInitializationException("Unable to create an instance of the class", e);
        }
    }
}

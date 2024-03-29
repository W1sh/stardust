package com.w1sh.stardust;

import com.w1sh.stardust.annotation.Primary;
import com.w1sh.stardust.annotation.Qualifier;
import com.w1sh.stardust.annotation.Required;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

public class ResolvableParameterImpl<S> implements ResolvableParameter<S> {

    private final Parameter parameter;

    public ResolvableParameterImpl(Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<S> getActualType() {
        return (Class<S>) parameter.getType();
    }

    @Override
    public ParameterizedType getParameterizedType() {
        return (ParameterizedType) parameter.getParameterizedType();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return parameter.getAnnotation(annotationType);
    }

    @Override
    public <T extends Annotation> Set<T> getAnnotations(Class<T> annotationType) {
        return Set.of(parameter.getAnnotationsByType(annotationType));
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return parameter.isAnnotationPresent(annotationType);
    }

    @Override
    public Boolean isPrimary() {
        return getAnnotation(Primary.class) != null;
    }

    @Override
    public String getQualifier() {
        Qualifier annotation = getAnnotation(Qualifier.class);
        return (annotation != null && !annotation.name().isBlank()) ? annotation.name() : null;
    }

    @Override
    public Boolean isRequired() {
        return getAnnotation(Required.class) != null;
    }


}

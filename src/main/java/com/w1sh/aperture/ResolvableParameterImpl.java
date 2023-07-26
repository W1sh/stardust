package com.w1sh.aperture;

import com.w1sh.aperture.annotation.Primary;
import com.w1sh.aperture.annotation.Qualifier;
import com.w1sh.aperture.annotation.Required;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

public class ResolvableParameterImpl<S> implements ResolvableParameter<S> {

    private final Parameter parameter;
    private final int index;
    private final Executable executable;

    public ResolvableParameterImpl(Parameter parameter, int index, Executable executable) {
        this.parameter = parameter;
        this.index = index;
        this.executable = executable;
    }

    @Override
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

package com.w1sh.aperture;

import java.lang.reflect.ParameterizedType;

public interface ResolvableParameter<S> extends Resolvable<S>, AnnotatedDependency {

    ParameterizedType getParameterizedType();
}

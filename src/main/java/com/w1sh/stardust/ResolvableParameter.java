package com.w1sh.stardust;

import java.lang.reflect.ParameterizedType;

public sealed interface ResolvableParameter<S> extends Resolvable<S>, AnnotatedDependency permits ResolvableParameterImpl {

    ParameterizedType getParameterizedType();
}

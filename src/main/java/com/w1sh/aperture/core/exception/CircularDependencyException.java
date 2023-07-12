package com.w1sh.aperture.core.exception;

import com.w1sh.aperture.core.ConstructorInitializationContext;

public class CircularDependencyException extends RuntimeException {

    public CircularDependencyException(ConstructorInitializationContext<?> context) {
        super(String.format("Can't create instance of class %s. Circular dependency: %s", context.getName(), context.chainAsString()));
    }
}

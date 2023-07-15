package com.w1sh.aperture.core.exception;

import com.w1sh.aperture.core.ClassDefinition;

public class CircularDependencyException extends RuntimeException {

    public CircularDependencyException(ClassDefinition<?> definition) {
        super(String.format("Can't create instance of class %s. Circular dependency: %s",
                definition.getMetadata().getClass(), definition.chainAsString()));
    }
}

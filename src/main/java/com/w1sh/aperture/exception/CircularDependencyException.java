package com.w1sh.aperture.exception;

import com.w1sh.aperture.ClassDefinition;

public class CircularDependencyException extends RuntimeException {

    public CircularDependencyException(ClassDefinition<?> definition) {
        super(String.format("Can't create instance of class %s. Circular dependency: %s",
                definition.getMetadata().getClass(), definition.chainAsString()));
    }
}

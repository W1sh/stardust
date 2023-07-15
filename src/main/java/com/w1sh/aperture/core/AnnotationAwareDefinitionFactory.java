package com.w1sh.aperture.core;

import com.w1sh.aperture.core.util.Constructors;

import java.lang.reflect.Constructor;

public interface AnnotationAwareDefinitionFactory extends DefinitionFactory {

    default <T> Constructor<T> findInjectAnnotatedConstructor(Class<T> clazz) {
        return Constructors.findInjectAnnotatedConstructor(clazz);
    }
}

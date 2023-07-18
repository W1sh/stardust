package com.w1sh.aperture.core;

import com.w1sh.aperture.core.util.Constructors;

import java.lang.reflect.Constructor;
import java.util.List;

public interface AnnotationAwareDefinitionFactory {

    <T> Definition<T> fromClass(Class<T> clazz);
    
    List<Definition<?>> fromModuleClass(Class<?> clazz);

    default <T> Constructor<T> findInjectAnnotatedConstructor(Class<T> clazz) {
        return Constructors.findInjectAnnotatedConstructor(clazz);
    }
}

package com.w1sh.aperture;

import com.w1sh.aperture.util.Constructors;

import java.lang.reflect.Constructor;
import java.util.List;

public interface AnnotationAwareDefinitionFactory {

    <T> Definition<T> fromClass(Class<T> clazz);
    
    List<Definition<?>> fromModuleClass(Class<?> clazz);

    default <T> Constructor<T> findInjectAnnotatedConstructor(Class<T> clazz) {
        return Constructors.findInjectAnnotatedConstructor(clazz);
    }
}

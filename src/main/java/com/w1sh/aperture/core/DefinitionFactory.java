package com.w1sh.aperture.core;

import com.w1sh.aperture.core.Definition;

public interface DefinitionFactory {

    <T> Definition<T> fromClass(Class<T> clazz);
}

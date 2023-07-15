package com.w1sh.aperture.core;

import java.util.List;

public interface ModuleAwareDefinitionFactory extends DefinitionFactory {

    <T> List<Definition<T>> fromModule(Class<?> module);
}

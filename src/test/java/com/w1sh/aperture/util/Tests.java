package com.w1sh.aperture.util;

import com.w1sh.aperture.core.ClassDefinition;
import com.w1sh.aperture.core.Metadata;
import com.w1sh.aperture.core.ProviderFactory;
import com.w1sh.aperture.core.DefaultProviderRegistry;
import com.w1sh.aperture.core.util.Constructors;

public class Tests {

    public static <T> void register(DefaultProviderRegistry registry, Class<T> clazz) {
        var factory = new ProviderFactory(registry);
        var provider = factory.create(definition(clazz));
        registry.register(provider, clazz, clazz.getSimpleName());
    }

    public static <T> ClassDefinition<T> definition(Class<T> clazz) {
        return ClassDefinition.withoutMetadata(clazz, Constructors.findInjectAnnotatedConstructor(clazz));
    }

    public static <T> ClassDefinition<T> definition(Class<T> clazz, Metadata metadata) {
        return ClassDefinition.withMetadata(clazz, Constructors.findInjectAnnotatedConstructor(clazz), metadata);
    }
}

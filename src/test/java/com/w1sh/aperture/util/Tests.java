package com.w1sh.aperture.util;

import com.w1sh.aperture.ClassDefinition;
import com.w1sh.aperture.Metadata;
import com.w1sh.aperture.DefaultProviderFactory;
import com.w1sh.aperture.DefaultProviderRegistry;

public class Tests {

    public static <T> void register(DefaultProviderRegistry registry, Class<T> clazz) {
        var factory = new DefaultProviderFactory(registry);
        var provider = factory.newProvider(definition(clazz));
        registry.register(provider, clazz, clazz.getSimpleName());
    }

    public static <T> ClassDefinition<T> definition(Class<T> clazz) {
        return ClassDefinition.withoutMetadata(clazz, Constructors.findInjectAnnotatedConstructor(clazz));
    }

    public static <T> ClassDefinition<T> definition(Class<T> clazz, Metadata metadata) {
        return ClassDefinition.withMetadata(clazz, Constructors.findInjectAnnotatedConstructor(clazz), metadata);
    }
}

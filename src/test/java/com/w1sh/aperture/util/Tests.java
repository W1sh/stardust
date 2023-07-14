package com.w1sh.aperture.util;

import com.w1sh.aperture.core.ConstructorInitializationContext;
import com.w1sh.aperture.core.ProviderFactory;
import com.w1sh.aperture.core.DefaultProviderRegistry;
import com.w1sh.aperture.core.builder.Options;

public class Tests {

    public static void register(DefaultProviderRegistry registry, Class<?> clazz) {
        var factory = new ProviderFactory(registry);
        var provider = factory.create(new ConstructorInitializationContext<>(clazz, Options.empty()));
        registry.register(provider, clazz, clazz.getSimpleName());
    }
}

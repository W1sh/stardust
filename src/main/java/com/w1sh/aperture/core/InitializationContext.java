package com.w1sh.aperture.core;

import com.w1sh.aperture.core.builder.Options;

public abstract class InitializationContext<T> {

    private final Class<T> clazz;
    private final Options options;

    protected InitializationContext(Class<T> clazz, Options options) {
        this.clazz = clazz;
        this.options = options;
    }

    public String getName() {
        return (options.name() != null && !options.name().isBlank()) ? options.name() : null;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public Options getOptions() {
        return options;
    }

}

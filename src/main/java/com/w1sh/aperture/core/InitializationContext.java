package com.w1sh.aperture.core;

import com.w1sh.aperture.core.builder.Options;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InitializationContext<?> that = (InitializationContext<?>) o;
        return Objects.equals(clazz, that.clazz) && Objects.equals(options, that.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, options);
    }
}

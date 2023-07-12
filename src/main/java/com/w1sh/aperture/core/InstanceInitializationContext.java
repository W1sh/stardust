package com.w1sh.aperture.core;

import com.w1sh.aperture.core.builder.Options;

public class InstanceInitializationContext<T> extends InitializationContext<T> {

    private final T instance;

    @SuppressWarnings("unchecked")
    protected InstanceInitializationContext(T instance, Options options) {
        super((Class<T>) instance.getClass(), options);
        this.instance = instance;
    }

    public T getInstance() {
        return instance;
    }
}

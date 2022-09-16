package com.w1sh.wave.core;

import java.util.List;

public final class DefinedObjectProvider<T> implements ObjectProvider<T> {

    private final T instance;

    public DefinedObjectProvider(T instance) {
        this.instance = instance;
    }

    @Override
    public T singletonInstance() {
        return instance;
    }

    @Override
    public T newInstance() {
        throw new UnsupportedOperationException("Defined object providers can not provide new instances, only a singleton instance.");
    }

    @Override
    public List<T> instances() {
        return List.of(instance);
    }
}

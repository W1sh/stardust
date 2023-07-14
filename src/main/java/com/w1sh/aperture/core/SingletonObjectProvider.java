package com.w1sh.aperture.core;

import java.util.List;

public final class SingletonObjectProvider<T> implements ObjectProvider<T> {

    private final T instance;

    public SingletonObjectProvider(T instance) {
        this.instance = instance;
    }

    @Override
    public T singletonInstance() {
        return instance;
    }

    @Override
    public T newInstance() {
        throw new UnsupportedOperationException("Singleton object providers can not provide new instances, only a singleton instance.");
    }

    @Override
    public List<T> instances() {
        return List.of(instance);
    }
}

package com.w1sh.wave.core;

import java.util.List;

public class DefinedObjectProvider<T> implements ObjectProvider<T> {

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
        return null;
    }

    @Override
    public List<T> instances() {
        return List.of(instance);
    }
}

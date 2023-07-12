package com.w1sh.aperture.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class PrototypeObjectProvider<T> implements ObjectProvider<T> {

    private final Supplier<T> supplier;
    private final List<T> instances;

    public PrototypeObjectProvider(Supplier<T> supplier) {
        this.supplier = supplier;
        this.instances = new ArrayList<>();
    }

    @Override
    public T singletonInstance() {
        if (instances.isEmpty()) {
            return newInstance();
        } else return instances.get(0);
    }

    @Override
    public T newInstance() {
        final T instance = Objects.requireNonNull(supplier.get());
        instances.add(instance);
        return instance;
    }

    @Override
    public List<T> instances() {
        return instances;
    }
}

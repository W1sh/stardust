package com.w1sh.aperture.binding;

import com.w1sh.aperture.ObjectProvider;

import static java.util.Objects.requireNonNull;

public class LazyBinding<T> implements Lazy<T> {

    private final ObjectProvider<T> provider;
    private volatile T delegate;

    public LazyBinding(ObjectProvider<T> provider) {
        this.provider = provider;
    }

    public static <T> LazyBinding<T> of(ObjectProvider<T> provider) {
        return provider != null ? new LazyBinding<>(provider) : null;
    }

    @Override
    public synchronized T get() {
        if (delegate == null) {
            delegate = requireNonNull(provider.singletonInstance());
        }
        return delegate;
    }
}

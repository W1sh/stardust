package com.w1sh.aperture.core.binding;

import com.w1sh.aperture.core.ObjectProvider;

import static java.util.Objects.requireNonNull;

public class LazyBinding<T> implements Lazy<T> {

    private final ObjectProvider<T> provider;
    private volatile T delegate;

    public LazyBinding(ObjectProvider<T> provider) {
        this.provider = provider;
    }

    @Override
    public synchronized T get() {
        if (delegate == null) {
            delegate = requireNonNull(provider.singletonInstance());
        }
        return delegate;
    }
}

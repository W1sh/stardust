package com.w1sh.aperture.binding;

import com.w1sh.aperture.ObjectProvider;

import static java.util.Objects.requireNonNull;

public class ProviderBinding<T> implements Provider<T> {

    private final ObjectProvider<T> provider;

    public ProviderBinding(ObjectProvider<T> provider) {
        this.provider = provider;
    }

    public static <T> ProviderBinding<T> of(ObjectProvider<T> provider) {
        return provider != null ? new ProviderBinding<>(provider) : null;
    }

    @Override
    public T get() {
        return requireNonNull(provider.newInstance());
    }
}

package com.w1sh.aperture.configuration;

import com.w1sh.aperture.*;
import com.w1sh.aperture.naming.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

public class ApertureConfiguration {

    private final List<InvocationInterceptor> interceptors;
    private ProviderContainer registry;
    private NamingStrategy namingStrategy;
    private ProviderContainer.OverrideStrategy overrideStrategy;

    public ApertureConfiguration() {
        this.interceptors = new ArrayList<>(16);
    }

    public ApertureConfiguration withRegistry(ProviderContainer registry) {
        return withRegistryIf(true, registry);
    }

    public ApertureConfiguration withRegistryIf(boolean predicate, ProviderContainer registry) {
        if (predicate) {
            this.registry = registry;
        }
        return this;
    }

    public ApertureConfiguration withNamingStrategy(NamingStrategy namingStrategy) {
        return withNamingStrategyIf(true, namingStrategy);
    }

    public ApertureConfiguration withNamingStrategyIf(boolean predicate, NamingStrategy namingStrategy) {
        if (predicate) {
            this.namingStrategy = namingStrategy;
        }
        return this;
    }

    public ApertureConfiguration withInterceptors(InvocationInterceptor... interceptors) {
        return withInterceptorsIf(true, interceptors);
    }

    public ApertureConfiguration withInterceptorsIf(boolean predicate, InvocationInterceptor... interceptors) {
        if (predicate) {
            this.interceptors.addAll(List.of(interceptors));
        }
        return this;
    }

    public ApertureConfiguration allowProviderOverriding(boolean value) {
        return allowProviderOverridingIf(true, value);
    }

    public ApertureConfiguration allowProviderOverridingIf(boolean predicate, boolean value) {
        if (predicate) {
            this.overrideStrategy = value ? ProviderContainer.OverrideStrategy.ALLOWED : ProviderContainer.OverrideStrategy.NOT_ALLOWED;
        }
        return this;
    }

    public void run(Class<?> primarySource, String... args) {
        new ApertureApplication(this, primarySource).run(args);
    }
}

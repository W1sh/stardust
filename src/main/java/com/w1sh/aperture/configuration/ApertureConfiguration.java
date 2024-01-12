package com.w1sh.aperture.configuration;

import com.w1sh.aperture.ApertureApplication;
import com.w1sh.aperture.ProviderContainer;
import com.w1sh.aperture.ProviderContainerImpl;
import com.w1sh.aperture.naming.DefaultNamingStrategy;
import com.w1sh.aperture.naming.NamingStrategy;

public class ApertureConfiguration {

    private ProviderContainer registry;
    private NamingStrategy namingStrategy;
    private ProviderContainer.OverrideStrategy overrideStrategy;

    public ApertureConfiguration() {}

    public static ApertureConfiguration base() {
        return new ApertureConfiguration()
                .withRegistry(new ProviderContainerImpl())
                .withNamingStrategy(new DefaultNamingStrategy())
                .allowProviderOverriding(true);
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

    public ProviderContainer getRegistry() {
        return registry;
    }

    public NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public ProviderContainer.OverrideStrategy getOverrideStrategy() {
        return overrideStrategy;
    }
}

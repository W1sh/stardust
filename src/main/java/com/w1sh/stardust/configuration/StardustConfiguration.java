package com.w1sh.stardust.configuration;

import com.w1sh.stardust.StardustApplication;
import com.w1sh.stardust.ProviderContainer;
import com.w1sh.stardust.ProviderContainerImpl;
import com.w1sh.stardust.naming.DefaultNamingStrategy;
import com.w1sh.stardust.naming.NamingStrategy;

public class StardustConfiguration {

    private ProviderContainer registry;
    private NamingStrategy namingStrategy;
    private ProviderContainer.OverrideStrategy overrideStrategy;

    public StardustConfiguration() {}

    public static StardustConfiguration base() {
        return new StardustConfiguration()
                .withRegistry(new ProviderContainerImpl())
                .withNamingStrategy(new DefaultNamingStrategy())
                .allowProviderOverriding(true);
    }

    public StardustConfiguration withRegistry(ProviderContainer registry) {
        return withRegistryIf(true, registry);
    }

    public StardustConfiguration withRegistryIf(boolean predicate, ProviderContainer registry) {
        if (predicate) {
            this.registry = registry;
        }
        return this;
    }

    public StardustConfiguration withNamingStrategy(NamingStrategy namingStrategy) {
        return withNamingStrategyIf(true, namingStrategy);
    }

    public StardustConfiguration withNamingStrategyIf(boolean predicate, NamingStrategy namingStrategy) {
        if (predicate) {
            this.namingStrategy = namingStrategy;
        }
        return this;
    }

    public StardustConfiguration allowProviderOverriding(boolean value) {
        return allowProviderOverridingIf(true, value);
    }

    public StardustConfiguration allowProviderOverridingIf(boolean predicate, boolean value) {
        if (predicate) {
            this.overrideStrategy = value ? ProviderContainer.OverrideStrategy.ALLOWED : ProviderContainer.OverrideStrategy.NOT_ALLOWED;
        }
        return this;
    }

    public void run(Class<?> primarySource, String... args) {
        new StardustApplication(this, primarySource).run(args);
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

package com.w1sh.stardust.configuration;

import com.w1sh.stardust.ProviderContainer;
import com.w1sh.stardust.ProviderContainerImpl;
import com.w1sh.stardust.StardustApplication;
import com.w1sh.stardust.naming.DefaultNamingStrategy;
import com.w1sh.stardust.naming.NamingStrategy;

public class StardustConfiguration {

    private Class<? extends ProviderContainer> registry;
    private Class<? extends PropertiesRegistry> propertiesRegistry;
    private Class<? extends NamingStrategy> namingStrategy;
    private ProviderContainer.OverrideStrategy overrideStrategy;

    public StardustConfiguration() {}

    public static StardustConfiguration base() {
        return new StardustConfiguration()
                .withRegistry(ProviderContainerImpl.class)
                .withPropertiesRegistry(PropertiesRegistryImpl.class)
                .withNamingStrategy(DefaultNamingStrategy.class)
                .allowProviderOverriding(true);
    }

    public StardustConfiguration withRegistry(Class<? extends ProviderContainer> registry) {
        return withRegistryIf(true, registry);
    }

    public StardustConfiguration withRegistryIf(boolean predicate, Class<? extends ProviderContainer> registry) {
        if (predicate) {
            this.registry = registry;
        }
        return this;
    }

    public StardustConfiguration withPropertiesRegistry(Class<? extends PropertiesRegistry> propertiesRegistry) {
        return withPropertiesRegistryIf(true, propertiesRegistry);
    }

    public StardustConfiguration withPropertiesRegistryIf(boolean predicate, Class<? extends PropertiesRegistry> propertiesRegistry) {
        if (predicate) {
            this.propertiesRegistry = propertiesRegistry;
        }
        return this;
    }

    public StardustConfiguration withNamingStrategy(Class<? extends NamingStrategy> namingStrategy) {
        return withNamingStrategyIf(true, namingStrategy);
    }

    public StardustConfiguration withNamingStrategyIf(boolean predicate, Class<? extends NamingStrategy> namingStrategy) {
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

    public Class<? extends ProviderContainer> getRegistry() {
        return registry;
    }

    public Class<? extends PropertiesRegistry> getPropertiesRegistry() {
        return propertiesRegistry;
    }

    public Class<? extends NamingStrategy> getNamingStrategy() {
        return namingStrategy;
    }

    public ProviderContainer.OverrideStrategy getOverrideStrategy() {
        return overrideStrategy;
    }
}

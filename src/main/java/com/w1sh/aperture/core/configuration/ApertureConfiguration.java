package com.w1sh.aperture.core.configuration;

import com.w1sh.aperture.core.ApertureApplication;
import com.w1sh.aperture.core.PostConstructorProcessor;
import com.w1sh.aperture.core.PreDestructionProcessor;
import com.w1sh.aperture.core.ProviderRegistry;
import com.w1sh.aperture.core.naming.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

import static com.w1sh.aperture.core.ProviderRegistry.OverrideStrategy.ALLOWED;
import static com.w1sh.aperture.core.ProviderRegistry.OverrideStrategy.NOT_ALLOWED;

public class ApertureConfiguration {

    private final List<PostConstructorProcessor> constructorProcessors;
    private final List<PreDestructionProcessor> destructionProcessors;
    private ProviderRegistry registry;
    private NamingStrategy namingStrategy;
    private ProviderRegistry.OverrideStrategy overrideStrategy;

    public ApertureConfiguration() {
        this.constructorProcessors = new ArrayList<>(16);
        this.destructionProcessors = new ArrayList<>(16);
    }

    public ApertureConfiguration withRegistry(ProviderRegistry registry) {
        return withRegistryIf(true, registry);
    }

    public ApertureConfiguration withRegistryIf(boolean predicate, ProviderRegistry registry) {
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

    public ApertureConfiguration withPostConstructorProcessors(PostConstructorProcessor... processors) {
        return withPostConstructorProcessorsIf(true, processors);
    }

    public ApertureConfiguration withPostConstructorProcessorsIf(boolean predicate, PostConstructorProcessor... processors) {
        if (predicate) {
            this.constructorProcessors.addAll(List.of(processors));
        }
        return this;
    }

    public ApertureConfiguration withPreDestructionProcessors(PreDestructionProcessor... processors) {
        return withPreDestructionProcessorsIf(true, processors);
    }

    public ApertureConfiguration withPreDestructionProcessorsIf(boolean predicate, PreDestructionProcessor... processors) {
        if (predicate) {
            this.destructionProcessors.addAll(List.of(processors));
        }
        return this;
    }

    public ApertureConfiguration allowProviderOverriding(boolean value) {
        return allowProviderOverridingIf(true, value);
    }

    public ApertureConfiguration allowProviderOverridingIf(boolean predicate, boolean value) {
        if (predicate) {
            this.overrideStrategy = value ? ALLOWED : NOT_ALLOWED;
        }
        return this;
    }

    public void run(Class<?> primarySource, String... args) {
        new ApertureApplication(this, primarySource).run(args);
    }
}

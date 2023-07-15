package com.w1sh.aperture.core;

import com.w1sh.aperture.core.annotation.Provide;
import com.w1sh.aperture.core.condition.Condition;
import com.w1sh.aperture.core.condition.MetadataConditionFactory;
import com.w1sh.aperture.core.condition.ProviderConditionEvaluator;
import com.w1sh.aperture.core.condition.ProviderConditionFactory;
import com.w1sh.aperture.core.event.PhaseEvent;
import com.w1sh.aperture.core.event.PhaseEventListener;
import com.w1sh.aperture.core.event.PhaseEventMulticaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ProviderRegistrationOrchestrator implements PhaseEventMulticaster {

    private static final Logger logger = LoggerFactory.getLogger(ProviderRegistrationOrchestrator.class);

    private final List<Definition<?>> pendingRegistrations = new ArrayList<>(256);
    private final List<PhaseEventListener> listeners = new ArrayList<>(64);

    private final DefaultProviderFactory factory;
    private final ProviderRegistry registry;
    private final ModuleAwareDefinitionFactory definitionFactory;
    private final ProviderConditionFactory conditionFactory;
    private final ProviderConditionEvaluator conditionEvaluator;

    public ProviderRegistrationOrchestrator(DefaultProviderRegistry registry, DefaultProviderFactory factory, Environment environment) {
        this.factory = factory;
        this.registry = registry;
        this.definitionFactory = new DefaultDefinitionFactory();
        this.conditionFactory = new ProviderConditionFactory();
        this.conditionEvaluator = new ProviderConditionEvaluator(registry, environment);
    }

    public ProviderRegistrationOrchestrator(DefaultProviderFactory factory, DefaultProviderRegistry registry,
                                            ProviderConditionFactory conditionFactory, ProviderConditionEvaluator conditionEvaluator) {
        this.factory = factory;
        this.registry = registry;
        this.definitionFactory = new DefaultDefinitionFactory();
        this.conditionFactory = conditionFactory;
        this.conditionEvaluator = conditionEvaluator;
    }

    public <T> void register(Class<T> clazz) {
        pendingRegistrations.add(definitionFactory.fromClass(clazz));
    }

    public <T> void register(Class<T> clazz, Supplier<T> supplier) {
        pendingRegistrations.add(ModuleMethodDefinition.withoutMetadata(clazz, supplier));
    }

    public void prepare() {
        logger.debug("Preparing context for registration");

        registerConditionFactories();
        registerAnnotatedProviders();
        registerModules();
    }

    public void orchestrate() {
        logger.debug("Received {} classes/instances to be registered", pendingRegistrations.size());
        final var delayedRegistrations = new ArrayList<Definition<?>>(256);

        ProviderPriorityComparator.sort(pendingRegistrations);

        pendingRegistrations.forEach(definition -> {
            logger.debug("Starting registration for class {}", definition.getClazz());
            List<? extends Condition> conditions = conditionFactory.create(definition.getMetadata());

            if (conditionEvaluator.canEvaluateEarly(conditions)) {
                boolean shouldSkip = conditionEvaluator.shouldSkip(conditions);
                if (shouldSkip) {
                    logger.info("Skipping registration of class {} as the conditions are not met", definition.getClazz().getSimpleName());
                } else {
                    ObjectProvider<?> provider = factory.newProvider(definition);
                    registry.register(provider, definition);
                }
            } else {
                logger.debug("Delaying registration of class {} as it requires other instances to be present",
                        definition.getClazz().getSimpleName());
                delayedRegistrations.add(definition);
            }
        });

        delayedRegistrations.forEach(definition -> {
            logger.debug("Starting delayed registration for class {}", definition.getClazz());
            List<? extends Condition> conditions = conditionFactory.create(definition.getMetadata());
            boolean shouldSkip = conditionEvaluator.shouldSkip(conditions);
            if (shouldSkip) {
                logger.info("Skipping registration of class {} as the conditions are not met", definition.getClazz().getSimpleName());
            } else {
                ObjectProvider<?> provider = factory.newProvider(definition);
                registry.register(provider, definition);
            }
        });

        pendingRegistrations.clear();
    }

    private void registerConditionFactories() {
        logger.debug("Registering condition factories");
        List<Class<?>> conditionFactories = ModuleInspector.findAllInternalSubclassesOf(MetadataConditionFactory.class);
        logger.debug("Found {} condition factories to be registered", conditionFactories.size());
        conditionFactories.stream()
                .map(definitionFactory::fromClass)
                .forEach(definition -> {
                    ObjectProvider<?> provider = factory.newProvider(definition);
                    registry.register(provider, definition);
                });

        registry.instances(new TypeReference<MetadataConditionFactory<?>>() {}).forEach(conditionFactory::register);
    }

    private void registerModules() {
        logger.debug("Registering modules");
        List<Class<?>> modules = ModuleInspector.findAllInternalSubclassesOf(Module.class);
        logger.debug("Found {} modules to be registered", modules.size());
        modules.stream()
                .map(definitionFactory::fromModule)
                .flatMap(Collection::stream)
                .forEach(definition -> {
                    ObjectProvider<?> provider = factory.newProvider(definition);
                    registry.register(provider, definition);
                });
    }

    private void registerAnnotatedProviders() {
        logger.debug("Registering annotated providers");
        List<Class<?>> annotatedProviders = ModuleInspector.findAllAnnotatedBy(Provide.class);
        logger.debug("Found {} annotated providers to be registered", annotatedProviders.size());
        annotatedProviders.stream()
                .map(definitionFactory::fromClass)
                .forEach(definition -> {
                    ObjectProvider<?> provider = factory.newProvider(definition);
                    registry.register(provider, definition);
                });
    }

    @Override
    public <T extends PhaseEvent> void addApplicationListener(PhaseEventListener<T> listener) {
        listeners.add(listener);
    }

    @Override
    public <T extends PhaseEvent> void removeApplicationListener(PhaseEventListener<T> listener) {
        listeners.remove(listener);
    }

    @Override
    public void removeAllListeners() {
        listeners.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PhaseEvent> void multicast(T event) {
        listeners.forEach(listener -> {
            if (listener.getEventType().isAssignableFrom(event.getClass())) {
                listener.onEvent(event);
            }
        });
    }
}

package com.w1sh.aperture.core;

import com.w1sh.aperture.core.builder.Options;
import com.w1sh.aperture.core.condition.Condition;
import com.w1sh.aperture.core.condition.MetadataConditionFactory;
import com.w1sh.aperture.core.condition.ProviderConditionEvaluator;
import com.w1sh.aperture.core.condition.ProviderConditionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ProviderRegistrationOrchestrator implements PhaseEventMulticaster {

    private static final Logger logger = LoggerFactory.getLogger(ProviderRegistrationOrchestrator.class);

    private final List<InitializationContext<?>> pendingRegistrations = new ArrayList<>(256);
    private final List<PhaseEventListener> listeners = new ArrayList<>(64);

    private final ProviderFactory factory;
    private final ProviderRegistry registry;
    private final ProviderConditionFactory conditionFactory;
    private final ProviderConditionEvaluator conditionEvaluator;

    public ProviderRegistrationOrchestrator(ProviderRegistry registry, ProviderFactory factory, Environment environment) {
        this.factory = factory;
        this.registry = registry;
        this.conditionFactory = new ProviderConditionFactory();
        this.conditionEvaluator = new ProviderConditionEvaluator(registry, environment);
    }

    public ProviderRegistrationOrchestrator(ProviderFactory factory, ProviderRegistry registry,
                                            ProviderConditionFactory conditionFactory, ProviderConditionEvaluator conditionEvaluator) {
        this.factory = factory;
        this.registry = registry;
        this.conditionFactory = conditionFactory;
        this.conditionEvaluator = conditionEvaluator;
    }

    public <T> void register(T instance, Options options) {
        pendingRegistrations.add(new InstanceInitializationContext<>(instance, options));
    }

    public <T> void register(Class<T> clazz, Options options) {
        pendingRegistrations.add(new ConstructorInitializationContext<>(clazz, options));
    }

    public <T> void register(Class<T> clazz, Options options, Supplier<T> supplier) {
        pendingRegistrations.add(new SupplierInitializationContext<>(clazz, options, supplier));
    }

    public void prepare() {
        logger.debug("Preparing context for registration");
        logger.debug("Registering condition factories");
        registerConditionFactories();
    }

    public void orchestrate() {
        logger.debug("Received {} classes/instances to be registered", pendingRegistrations.size());
        final var delayedRegistrations = new ArrayList<InitializationContext<?>>(256);

        ProviderOrderComparator.sort(pendingRegistrations);

        pendingRegistrations.forEach(context -> {
            logger.debug("Starting registration for class {}", context.getClazz());
            List<? extends Condition> conditions = conditionFactory.create(context.getOptions());

            if (conditionEvaluator.canEvaluateEarly(conditions)) {
                boolean shouldSkip = conditionEvaluator.shouldSkip(conditions);
                if (shouldSkip) {
                    logger.info("Skipping registration of class {} as the conditions are not met", context.getClazz().getSimpleName());
                } else {
                    ObjectProvider<?> provider = factory.create(context);
                    registry.register(provider, context.getClazz(), context.getName());
                }
            } else {
                logger.debug("Delaying registration of class {} as it requires other instances to be present",
                        context.getClazz().getSimpleName());
                delayedRegistrations.add(context);
            }
        });

        delayedRegistrations.forEach(context -> {
            logger.debug("Starting delayed registration for class {}", context.getClazz());
            List<? extends Condition> conditions = conditionFactory.create(context.getOptions());
            boolean shouldSkip = conditionEvaluator.shouldSkip(conditions);
            if (shouldSkip) {
                logger.info("Skipping registration of class {} as the conditions are not met", context.getClazz().getSimpleName());
            } else {
                ObjectProvider<?> provider = factory.create(context);
                registry.register(provider, context.getClazz(), context.getName());
            }
        });

        pendingRegistrations.clear();
    }

    private void registerConditionFactories() {
        List<Class<?>> conditionFactories = ModuleInspector.findAllInternalSubclassesOf(MetadataConditionFactory.class);
        logger.debug("Found {} internal condition factories to be register", conditionFactories.size());
        conditionFactories.forEach(cf -> {
            InitializationContext<?> context = new ConstructorInitializationContext<>(cf, Options.empty());
            ObjectProvider<?> provider = factory.create(context);
            registry.register(provider, cf, context.getName());
        });
        registry.instances(new TypeReference<MetadataConditionFactory<?>>() {}).forEach(conditionFactory::register);
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

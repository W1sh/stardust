package com.w1sh.stardust;

import com.w1sh.stardust.annotation.Provide;
import com.w1sh.stardust.configuration.StardustConfiguration;
import com.w1sh.stardust.dependency.DependencyResolver;
import com.w1sh.stardust.dependency.DependencyResolver.EvaluationPhase;
import com.w1sh.stardust.dependency.Resolver;
import com.w1sh.stardust.exception.ComponentCreationException;
import com.w1sh.stardust.naming.NamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StardustApplication {

    private static final Logger logger = LoggerFactory.getLogger(StardustApplication.class);

    private final Set<Class<?>> sources;
    private StardustConfiguration configuration;

    public StardustApplication(Class<?>... sources) {
        this.sources = Set.of(sources);
    }

    public StardustApplication(StardustConfiguration configuration, Class<?>... sources) {
        this.sources = Set.of(sources);
        this.configuration = configuration;
    }

    public static StardustConfiguration configure() {
        return new StardustConfiguration();
    }

    public static void run(Class<?> primarySource, String... args) {
        new StardustApplication(primarySource).run(args);
    }

    public void run(String... args) {
        if (configuration == null) {
            logger.debug("No configuration found, using default configuration.");
            configuration = StardustConfiguration.base();
        }
        StardustApplicationInitializer initializer = new StardustApplicationInitializer(configuration);
        initializer.initialize(sources);
    }

    static class StardustApplicationInitializer {

        private final SetValueEnumMap<EvaluationPhase, DependencyResolver> resolvers;
        private final ProviderContainer container;
        private final Environment environment;

        StardustApplicationInitializer(StardustConfiguration configuration) {
            try {
                NamingStrategy namingStrategy = configuration.getNamingStrategy().getConstructor().newInstance();
                this.container = configuration.getRegistry().getConstructor(NamingStrategy.class).newInstance(namingStrategy);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new ComponentCreationException("Failed to create provider container", e);
            }
            this.resolvers = new SetValueEnumMap<>(EvaluationPhase.class);
            this.environment = new Environment(container, new HashSet<>());

            container.register(configuration.getPropertiesRegistry());
        }

        public void initialize(Set<Class<?>> sources) {
            List<Class<?>> registrationReadyClasses = new ArrayList<>();
            List<Class<?>> allClassPendingRegistration = Inspector.findAllSubclassesOf(sources, Provide.class);

            allClassPendingRegistration.forEach(clazz -> {
                if (!dependenciesMatchForPhase(clazz, EvaluationPhase.BEFORE_REGISTRATION)) {
                    logger.debug("Skipping registration of class {} as conditionals did not match", clazz.getSimpleName());
                }
                registrationReadyClasses.add(clazz);
            });
            registrationReadyClasses.forEach(container::register);
        }

        private boolean dependenciesMatchForPhase(Class<?> clazz, EvaluationPhase phase) {
            Set<Resolver> annotations = Set.of(clazz.getAnnotationsByType(Resolver.class));
            if (annotations.isEmpty()) return true;

            Set<DependencyResolver> phaseResolvers = resolvers.get(phase);
            for (Resolver annotation : annotations) {
                for (DependencyResolver phaseResolver : phaseResolvers) {
                    if (annotation.value().equals(phaseResolver.getClass())) {
                        boolean matches = phaseResolver.matches(clazz, environment);
                        if (!matches) return false;
                    }
                }
            }
            return true;
        }
    }
}

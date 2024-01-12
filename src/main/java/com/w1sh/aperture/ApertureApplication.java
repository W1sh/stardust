package com.w1sh.aperture;

import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.configuration.ApertureConfiguration;
import com.w1sh.aperture.dependency.DependencyResolver;
import com.w1sh.aperture.dependency.DependencyResolver.EvaluationPhase;
import com.w1sh.aperture.dependency.Resolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApertureApplication {

    private static final Logger logger = LoggerFactory.getLogger(ApertureApplication.class);

    private final Set<Class<?>> sources;
    private ApertureConfiguration configuration;

    public ApertureApplication(Class<?>... sources) {
        this.sources = Set.of(sources);
    }

    public ApertureApplication(ApertureConfiguration configuration, Class<?>... sources) {
        this.sources = Set.of(sources);
        this.configuration = configuration;
    }

    public static ApertureConfiguration configure() {
        return new ApertureConfiguration();
    }

    public static void run(Class<?> primarySource, String... args) {
        new ApertureApplication(primarySource).run(args);
    }

    public void run(String... args) {
        if (configuration == null) {
            logger.debug("No configuration found, using default configuration.");
            configuration = ApertureConfiguration.base();
        }
        ApertureApplicationInitializer initializer = new ApertureApplicationInitializer(configuration.getRegistry());
        initializer.registerInternals();
        initializer.initialize(sources);
    }

    static class ApertureApplicationInitializer {

        private final SetValueEnumMap<EvaluationPhase, DependencyResolver> resolvers;
        private final ProviderContainer container;
        private final Environment environment;

        ApertureApplicationInitializer(ProviderContainer container) {
            this.container = container;
            this.resolvers = new SetValueEnumMap<>(EvaluationPhase.class);
            this.environment = new Environment(container, new HashSet<>());
        }

        public void registerInternals() {
            logger.debug("Registering aperture internal classes");
            List<Class<?>> internalClasses = ModuleInspector.findAllAnnotatedBy(ModuleInspector.MODULE_NAME, Provide.class);
            logger.debug("Found {} aperture internal classes to be registered", internalClasses.size());
            internalClasses.forEach(container::register);

            // save resolvers for later processing
            container.instances(DependencyResolver.class).forEach(dependencyResolver -> resolvers.put(dependencyResolver.getEvaluationPhase(), dependencyResolver));

            // register interceptors in interceptor aware classes
            List<InterceptorAware> interceptorAwareClasses = container.instances(InterceptorAware.class);
            logger.debug("Found {} interceptor aware classes", interceptorAwareClasses.size());

            logger.debug("Registering invocation interceptors for post processing");
            List<InvocationInterceptor> internalInterceptors = container.instances(InvocationInterceptor.class);
            logger.debug("Found {} invocation interceptors", internalInterceptors.size());

            internalInterceptors.forEach(invocationInterceptor -> interceptorAwareClasses
                    .forEach(interceptorAware -> interceptorAware.addInterceptor(invocationInterceptor)));
        }

        public void initialize(Set<Class<?>> sources) {
            List<Class<?>> registrationReadyClasses = new ArrayList<>();
            List<Class<?>> allClassPendingRegistration = ModuleInspector.findAllSubclassesOf(sources, Provide.class);

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

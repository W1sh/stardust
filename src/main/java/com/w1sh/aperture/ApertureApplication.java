package com.w1sh.aperture;

import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.configuration.ApertureConfiguration;
import com.w1sh.aperture.dependency.DependencyResolver;
import com.w1sh.aperture.dependency.DependencyResolver.EvaluationPhase;
import com.w1sh.aperture.dependency.Resolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
        ApertureApplicationInitializer initializer = new ApertureApplicationInitializer();
        initializer.registerInternals();
        initializer.initialize(sources);
    }

    static class ApertureApplicationInitializer {

        private final SetValueEnumMap<EvaluationPhase, DependencyResolver> resolvers;
        private final ProviderContainer container;
        private final Environment environment;

        ApertureApplicationInitializer() {
            this.container = new ProviderContainerImpl();
            this.resolvers = new SetValueEnumMap<>(EvaluationPhase.class);
            this.environment = new Environment(container, new HashSet<>());
        }

        public void registerInternals(){
            logger.debug("Registering aperture internal classes");
            List<Class<?>> internalClasses = ModuleInspector.findAllAnnotatedBy(this.getClass().getModule().getName(), Provide.class);
            logger.debug("Found {} aperture internal classes to be registered", internalClasses.size());
            internalClasses.forEach(container::register);
        }

        public void initialize(Set<Class<?>> sources) {
            List<Class<?>> classesToRegister = new ArrayList<>();
            for (Class<?> clazz : sources.stream()
                    .map(source -> ModuleInspector.findAllAnnotatedBy(source.getModule().getName(), Provide.class))
                    .flatMap(Collection::stream)
                    .toList()) {
                if (!dependenciesMatchForPhase(clazz, EvaluationPhase.BEFORE_REGISTRATION)) {
                    logger.debug("Skipping registration of class {} as conditionals did not match", clazz.getSimpleName());
                }
                classesToRegister.add(clazz);
            }
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

package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.core.annotation.Configuration;
import com.w1sh.wave.core.annotation.Profile;
import com.w1sh.wave.core.annotation.Provides;
import com.w1sh.wave.util.Annotations;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.reflections.ReflectionUtils.Methods;
import static org.reflections.scanners.Scanners.TypesAnnotated;
import static org.reflections.util.ReflectionUtilsPredicates.withAnnotation;

public class GenericComponentScanner implements ComponentScanner {

    private static final Logger logger = LoggerFactory.getLogger(GenericComponentScanner.class);

    private final Set<Class<?>> typesToIgnore = new HashSet<>();
    private final ClassDefinitionFactory classDefinitionFactory;
    private final MethodDefinitionFactory methodDefinitionFactory;
    private final Reflections reflections;

    private AbstractApplicationEnvironment environment;

    public GenericComponentScanner(AbstractApplicationEnvironment environment, ClassDefinitionFactory classDefinitionFactory,
                                   MethodDefinitionFactory methodDefinitionFactory) {
        this.reflections = new Reflections(new ConfigurationBuilder()
                .forPackage(environment.getPackagePrefix())
                .setScanners(TypesAnnotated, Scanners.SubTypes, Scanners.MethodsAnnotated));
        this.classDefinitionFactory = classDefinitionFactory != null ? classDefinitionFactory : new SimpleClassDefinitionFactory();
        this.methodDefinitionFactory = methodDefinitionFactory != null ? methodDefinitionFactory : new SimpleMethodDefinitionFactory();
    }

    public GenericComponentScanner() {
        this(ApplicationEnvironment.builder().build(), null, null);
    }

    @Override
    public List<Definition> scan() {
        return Stream.of(scanClasses(), scanMethods()).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public void ignoreType(Class<?> clazz) {
        typesToIgnore.add(clazz);
    }

    private List<Definition> scanClasses() {
        logger.debug("Scanning in defined package for annotated classes");
        final Set<Class<?>> componentClasses = reflections.get(TypesAnnotated.with(Component.class).asClass());
        componentClasses.removeAll(typesToIgnore);
        return componentClasses.stream()
                .filter(this::isProfileActive)
                .map(classDefinitionFactory::create)
                .collect(Collectors.toList());
    }

    private List<Definition> scanMethods() {
        logger.debug("Scanning in defined package for annotated methods");
        final Set<Class<?>> configurationClasses = reflections.get(TypesAnnotated.with(Configuration.class).asClass());
        configurationClasses.removeAll(typesToIgnore);
        return configurationClasses.stream()
                .map(clazz -> reflections.get(Methods.of(clazz)
                        .filter(withAnnotation(Provides.class))
                        .as(Method.class)))
                .flatMap(Collection::stream)
                .filter(this::isProfileActive)
                .map(methodDefinitionFactory::create)
                .collect(Collectors.toList());
    }

    private boolean isProfileActive(AnnotatedElement annotatedElement) {
        final List<Annotation> annotationsOfType = Annotations.getAnnotationsOfType(annotatedElement, Profile.class);
        return annotationsOfType.stream()
                .map(annotation -> ((Profile) annotation).value())
                .anyMatch(env -> environment.getActiveProfiles().contains(env));
    }

    @Override
    public AbstractApplicationEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(AbstractApplicationEnvironment environment) {
        this.environment = environment;
    }
}

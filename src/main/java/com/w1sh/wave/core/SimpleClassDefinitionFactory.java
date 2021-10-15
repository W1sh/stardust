package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.*;
import com.w1sh.wave.util.Annotations;
import com.w1sh.wave.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SimpleClassDefinitionFactory implements ClassDefinitionFactory {

    private static final Logger logger = LoggerFactory.getLogger(SimpleClassDefinitionFactory.class);
    private final ComponentNameGenerator nameGenerator = new QualifiedComponentNameGenerator();
    private final InjectionPointFactory injectionPointFactory = new SimpleInjectionPointFactory();

    @Override
    public Definition create(Class<?> clazz) {
        if (Modifier.isAbstract(clazz.getModifiers())) {
            logger.warn("Can not convert abstract class {} into a definition.", clazz);
            return null;
        }

        logger.debug("Creating component definition from class {}.", clazz);
        final var definition = new ComponentDefinition(clazz);
        final var constructor = ReflectionUtils.findInjectAnnotatedConstructor(clazz);
        definition.setPrimary(Annotations.isAnnotationPresent(clazz, Primary.class));
        definition.setConditional(Annotations.isAnnotationPresent(clazz, Conditional.class));
        definition.setPriority((Priority) Annotations.getAnnotationOfType(clazz, Priority.class).orElse(null));
        definition.setInjectionPoint(injectionPointFactory.create(constructor));
        definition.setName(nameGenerator.generate(clazz, clazz.getAnnotation(Component.class)));
        definition.setAliases(clazz.getAnnotation(Component.class).aliases());
        retrievePostConstructorMethods(clazz, definition);
        return definition;
    }

    private void retrievePostConstructorMethods(Class<?> aClass, ComponentDefinition definition) {
        final List<Method> postConstructorMethods = new ArrayList<>();
        for (Method method : aClass.getMethods()) {
            if (Annotations.isAnnotationPresent(method, PostConstruct.class)) {
                postConstructorMethods.add(method);
            }
        }
        definition.setPostConstructorMethods(postConstructorMethods);
    }
}

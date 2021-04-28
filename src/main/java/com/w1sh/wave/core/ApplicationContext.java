package com.w1sh.wave.core;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationContext extends AbstractApplicationContext {

    public ApplicationContext(ComponentRegistry registry, ComponentScanner scanner,
                              ClassDefinitionFactory classDefinitionFactory, MethodDefinitionFactory methodDefinitionFactory) {
        super(registry, scanner, classDefinitionFactory, methodDefinitionFactory);

    }

    public ApplicationContext(ComponentRegistry registry, ComponentScanner scanner, AbstractApplicationEnvironment environment,
                              ClassDefinitionFactory classDefinitionFactory, MethodDefinitionFactory methodDefinitionFactory) {
        super(registry, scanner, environment, classDefinitionFactory, methodDefinitionFactory);
    }

    public static ApplicationContextBuilder builder(){
        return new ApplicationContextBuilder();
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        return this.getRegistry().getComponent(clazz);
    }

    @Override
    public <T> T getComponent(Class<T> clazz, String name) {
        return this.getRegistry().getComponent(name, clazz);
    }

    @Override
    public boolean containsComponent(Class<?> clazz) {
        return this.getRegistry().getComponent(clazz) != null;
    }

    @Override
    public boolean containsComponent(String name) {
        return this.getRegistry().getComponent(name) != null;
    }

    @Override
    public Class<?> getType(String name) {
        final Object component = this.getRegistry().getComponent(name);
        if (component != null) {
            return component.getClass();
        }
        return null;
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> clazz) {
        final Class<?> type = getType(name);
        return type != null && type.isAssignableFrom(clazz);
    }

    @Override
    public void initialize() {
        this.getRegistry().register(ApplicationContext.class, this);

        final Set<Definition> classesDefinitions = this.getScanner().scanClasses().stream()
                .map(getClassDefinitionFactory()::create)
                .collect(Collectors.toSet());
        final Set<Definition> methodsDefinitions = this.getScanner().scanMethods().stream()
                .map(getMethodDefinitionFactory()::create)
                .collect(Collectors.toSet());

        final List<Definition> definitions = Stream.of(classesDefinitions, methodsDefinitions)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        this.getRegistry().initialize(definitions);
    }

    @Override
    public void refresh() {
        this.getRegistry().clear();
        initialize();
    }

    @Override
    public void clear() {
        this.getRegistry().clear();
    }

}

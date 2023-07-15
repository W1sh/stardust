package com.w1sh.aperture.core;

import com.w1sh.aperture.core.exception.ModuleProcessingException;
import com.w1sh.aperture.core.exception.ProviderInitializationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DefaultDefinitionFactory implements AnnotationAwareDefinitionFactory, ModuleAwareDefinitionFactory {

    private final MetadataFactory metadataFactory;

    public DefaultDefinitionFactory() {
        this.metadataFactory = new DefaultMetadataFactory();
    }

    @Override
    public <T> Definition<T> fromClass(Class<T> clazz) {
        Metadata metadata = metadataFactory.create(clazz);
        Constructor<T> constructor = findInjectAnnotatedConstructor(clazz);
        return ClassDefinition.withMetadata(clazz, constructor, metadata);
    }

    @Override
    public <T> List<Definition<T>> fromModule(Class<?> module) {
        if (List.of(module.getInterfaces()).contains(Module.class)) {
            throw new ModuleProcessingException("Tried to create definitions from class but it is not a Module");
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private <T, S> Supplier<T> createSupplier(Method method, S module) {
        return () -> {
            try {
                return (T) method.invoke(module);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ProviderInitializationException("Unable to create an instance of the class", e);
            }
        };
    }
}

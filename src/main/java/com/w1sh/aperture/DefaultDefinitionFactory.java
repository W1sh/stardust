package com.w1sh.aperture;

import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.exception.ModuleProcessingException;
import com.w1sh.aperture.exception.ProviderInitializationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DefaultDefinitionFactory implements AnnotationAwareDefinitionFactory {

    private final AnnotationAwareMetadataFactory metadataFactory;
    private final ProviderRegistry registry;

    public DefaultDefinitionFactory(ProviderRegistry registry) {
        this.registry = registry;
        this.metadataFactory = new DefaultMetadataFactory();
    }

    @Override
    public <T> Definition<T> fromClass(Class<T> clazz) {
        Metadata metadata = metadataFactory.create(clazz);
        Constructor<T> constructor = findInjectAnnotatedConstructor(clazz);
        return ClassDefinition.withMetadata(clazz, constructor, metadata);
    }

    @Override
    public List<Definition<?>> fromModuleClass(Class<?> module) {
        if (!List.of(module.getInterfaces()).contains(Module.class)) {
            throw new ModuleProcessingException("Tried to create definitions from class but it is not a Module");
        }
        var definitions = new ArrayList<Definition<?>>();
        for (Method declaredMethod : module.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(Provide.class)) {
                Metadata metadata = metadataFactory.create(declaredMethod);
                Object instance = registry.instance(module);
                ModuleMethodDefinition<?> methodDefinition = ModuleMethodDefinition.withMetadata(
                        declaredMethod.getReturnType(), metadata, createSupplier(declaredMethod, instance));
                definitions.add(methodDefinition);
            }
        }
        return definitions;
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

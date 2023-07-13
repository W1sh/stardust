package com.w1sh.aperture.core;

import com.w1sh.aperture.core.exception.MultipleProviderCandidatesException;
import com.w1sh.aperture.core.naming.DefaultNamingStrategy;
import com.w1sh.aperture.core.naming.NamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProviderRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ProviderRegistry.class);

    private final Map<Class<?>, ObjectProvider<?>> providers = new ConcurrentHashMap<>(256);
    private final Map<String, ObjectProvider<?>> named = new ConcurrentHashMap<>(256);
    private final NamingStrategy namingStrategy;

    public ProviderRegistry() {
        this(new DefaultNamingStrategy());
    }

    public ProviderRegistry(NamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
        this.register(new SingletonObjectProvider<>(this), this.getClass(), "providerRegistry");
    }

    public void register(ObjectProvider<?> provider, Class<?> clazz, String name) {
        final var providerName = name != null ? name : namingStrategy.generate(clazz);
        logger.debug("Registering provider of class {} with name {}", clazz.getSimpleName(), providerName);

        providers.put(clazz, provider);
        named.put(providerName, provider);
    }

    @SuppressWarnings("unchecked")
    public <T> T instance(Class<T> clazz) {
        ObjectProvider<?> provider = get(clazz);
        return provider != null ? (T) provider.singletonInstance() : null;
    }

    @SuppressWarnings("unchecked")
    public <T> T instance(String name) {
        ObjectProvider<?> provider = named.get(name);
        return provider != null ? (T) provider.singletonInstance() : null;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> instances(Class<T> clazz) {
        return (List<T>) candidates(clazz).stream()
                .map(ObjectProvider::singletonInstance)
                .toList();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> instances(TypeReference<T> typeReference) {
        return (List<T>) candidates(typeReference.getType().getClass()).stream()
                .map(ObjectProvider::singletonInstance)
                .toList();
    }

    public <T> ObjectProvider<T> provider(Class<T> clazz) {
        return get(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> ObjectProvider<T> provider(String name) {
        return (ObjectProvider<T>) named.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T> List<ObjectProvider<T>> providers(Class<T> clazz) {
        return (List<ObjectProvider<T>>) candidates(clazz);
    }

    public <T> boolean contains(Class<T> clazz) {
        return get(clazz) != null;
    }

    public boolean contains(String name) {
        return provider(name) != null;
    }

    public List<Class<?>> getAllAnnotatedWith(Class<? extends Annotation> annotationType) {
        return providers.keySet().stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotationType))
                .toList();
    }

    @SuppressWarnings("unchecked")
    private <T> ObjectProvider<T> get(Class<T> clazz) {
        final var candidates = candidates(clazz);

        if (candidates.size() > 1) {
            logger.error("Expected 1 candidate but found {} for class {}", candidates.size(), clazz);
            throw new MultipleProviderCandidatesException(candidates.size(), clazz);
        }
        return candidates.isEmpty() ? null : (ObjectProvider<T>) candidates.get(0);
    }

    private <T> List<? extends ObjectProvider<?>> candidates(Class<T> clazz) {
        return providers.entrySet().stream()
                .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                .map(Map.Entry::getValue)
                .toList();
    }
}

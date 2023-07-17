package com.w1sh.aperture.core;

import com.w1sh.aperture.core.exception.ProviderCandidatesException;
import com.w1sh.aperture.core.exception.ProviderRegistrationException;
import com.w1sh.aperture.core.naming.DefaultNamingStrategy;
import com.w1sh.aperture.core.naming.NamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultProviderRegistry implements ProviderRegistry {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProviderRegistry.class);

    private final Map<Class<?>, ObjectProvider<?>> providers = new ConcurrentHashMap<>(256);
    private final Map<Class<?>, Metadata> metadatas = new ConcurrentHashMap<>(256);
    private final Map<String, ObjectProvider<?>> named = new ConcurrentHashMap<>(256);
    private final NamingStrategy namingStrategy;

    private OverrideStrategy overrideStrategy = OverrideStrategy.ALLOWED;

    public DefaultProviderRegistry() {
        this(new DefaultNamingStrategy());
    }

    public DefaultProviderRegistry(NamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
        this.register(new SingletonObjectProvider<>(this), DefaultProviderRegistry.class, ProviderRegistry.DEFAULT_REGISTRY_NAME);
    }

    @Override
    public void register(ObjectProvider<?> provider, Class<?> clazz, String name) {
        final var providerName = name != null ? name : namingStrategy.generate(clazz);
        logger.debug("Registering provider of class {} with name {}", clazz.getSimpleName(), providerName);

        if (OverrideStrategy.NOT_ALLOWED.equals(overrideStrategy) && providers.containsKey(clazz)) {
            throw ProviderRegistrationException.notAllowedClass(clazz);
        }
        if (OverrideStrategy.NOT_ALLOWED.equals(overrideStrategy) && named.containsKey(providerName)) {
            throw ProviderRegistrationException.notAllowedName(providerName);
        }

        providers.put(clazz, provider);
        named.put(providerName, provider);
    }

    @Override
    public void register(ObjectProvider<?> provider, Definition<?> definition) {
        register(provider, definition.getClazz(), definition.getMetadata().name());
        metadatas.put(definition.getClazz(), definition.getMetadata());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T instance(Class<T> clazz) {
        ObjectProvider<?> provider = get(clazz);
        return provider != null ? (T) provider.singletonInstance() : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T instance(String name) {
        ObjectProvider<?> provider = named.get(name);
        return provider != null ? (T) provider.singletonInstance() : null;
    }

    @Override
    public <T> T primaryInstance(Class<T> clazz) {
        return primaryProvider(clazz).singletonInstance();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ObjectProvider<T> primaryProvider(Class<T> clazz) {
        List<? extends ObjectProvider<?>> primaries = providers.entrySet().stream()
                .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                .filter(entry -> isPrimary(metadatas.get(entry.getKey())))
                .map(Map.Entry::getValue)
                .toList();

        if (primaries.isEmpty()) {
            logger.error("Expected 1 primary candidate but found none for class {}", clazz);
            throw new ProviderCandidatesException(0, clazz);
        } else if (primaries.size() > 1) {
            logger.error("Expected 1 primary candidate but found {} for class {}", primaries.size(), clazz);
            throw new ProviderCandidatesException(primaries.size(), clazz);
        }
        return (ObjectProvider<T>) primaries.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> instances(Class<T> clazz) {
        return (List<T>) candidates(clazz).stream()
                .map(ObjectProvider::singletonInstance)
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> instances(TypeReference<T> typeReference) {
        return (List<T>) candidates(typeReference.getType().getClass()).stream()
                .map(ObjectProvider::singletonInstance)
                .toList();
    }

    @Override
    public <T> ObjectProvider<T> provider(Class<T> clazz) {
        return get(clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ObjectProvider<T> provider(String name) {
        return (ObjectProvider<T>) named.get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<ObjectProvider<T>> providers(Class<T> clazz) {
        return (List<ObjectProvider<T>>) candidates(clazz);
    }

    @Override
    public <T> boolean contains(Class<T> clazz) {
        return !candidates(clazz).isEmpty();
    }

    @Override
    public boolean contains(String name) {
        return provider(name) != null;
    }

    @Override
    public OverrideStrategy getOverrideStrategy() {
        return overrideStrategy;
    }

    @Override
    public void setOverrideStrategy(OverrideStrategy strategy) {
        this.overrideStrategy = strategy;
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
            throw new ProviderCandidatesException(candidates.size(), clazz);
        }
        return candidates.isEmpty() ? null : (ObjectProvider<T>) candidates.get(0);
    }

    private <T> List<? extends ObjectProvider<?>> candidates(Class<T> clazz) {
        return providers.entrySet().stream()
                .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                .map(Map.Entry::getValue)
                .toList();
    }

    private boolean isPrimary(Metadata metadata) {
        return metadata != null && metadata.primary() != null && metadata.primary();
    }
}

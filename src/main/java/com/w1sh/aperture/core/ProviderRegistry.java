package com.w1sh.aperture.core;

import java.util.List;

public interface ProviderRegistry {

    String DEFAULT_REGISTRY_NAME = "providerRegistry";

    void register(ObjectProvider<?> provider, Class<?> clazz, String name);

    void register(ObjectProvider<?> provider, Definition<?> definition);

    <T> T instance(Class<T> clazz);

    <T> T instance(String name);

    <T> T primaryInstance(Class<T> clazz);

    <T> ObjectProvider<T> primaryProvider(Class<T> clazz);

    <T> List<T> instances(Class<T> clazz);

    <T> List<T> instances(TypeReference<T> typeReference) ;

    <T> ObjectProvider<T> provider(Class<T> clazz);

    <T> ObjectProvider<T> provider(String name);

    <T> List<ObjectProvider<T>> providers(Class<T> clazz);

    <T> boolean contains(Class<T> clazz);

    boolean contains(String name);

    OverrideStrategy getOverrideStrategy();

    void setOverrideStrategy(OverrideStrategy strategy);

    enum OverrideStrategy {
        ALLOWED, NOT_ALLOWED
    }
}

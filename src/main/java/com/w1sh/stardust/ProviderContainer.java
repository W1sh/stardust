package com.w1sh.stardust;

import java.util.List;

public interface ProviderContainer {

    void register(Class<?> clazz);

    <T> T instance(Class<T> clazz);

    <T> T instance(String name);

    <T> T primaryInstance(Class<T> clazz);

    <T> ObjectProvider<T> primaryProvider(Class<T> clazz);

    <T> List<T> instances(Class<T> clazz);

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

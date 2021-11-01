package com.w1sh.wave.core;

import java.util.List;

public interface Registry {

    void register(Class<?> clazz, Object instance);

    void register(String name, Object instance);

    <T> T getComponent(Class<T> clazz);

    Object getComponent(String name);

    <T> T getComponent(String name, Class<T> clazz);

    <T> List<T> getComponentsOfType(Class<T> clazz);

    Class<?> getType(String name);

    boolean isTypeMatch(String name, Class<?> clazz);

    boolean containsComponent(String name);

    boolean containsComponent(Class<?> clazz);

    boolean containsComponent(Class<?> clazz, boolean allowSearchSubclasses);
}

package com.w1sh.stardust;

import java.util.List;
import java.util.Set;

public interface ProviderStore {

    <T> void register(String name, Class<T> clazz, ObjectProvider<T> provider);

    <T> ObjectProvider<T> get(String name);

    <T> List<ObjectProvider<T>> get(Class<T> clazz);

    Set<Class<?>> getAllClasses();

    List<ObjectProvider<?>> getAll();

    List<ObjectProvider<?>> getAllOrdered();

    Integer count();

    void clear();
}

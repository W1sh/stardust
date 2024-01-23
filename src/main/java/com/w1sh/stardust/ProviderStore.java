package com.w1sh.stardust;

import java.util.List;
import java.util.Set;

public interface ProviderStore {

    <T> void register(String name, Class<T> clazz, ObjectProvider<T> provider);

    <T> ObjectProvider<T> get(String name);

    <T> List<ObjectProvider<T>> get(Class<T> clazz);

    Set<Class<?>> getAllClasses();

    /**
     * Returns all the {@link ObjectProvider} elements registered in this store.
     *
     * @return a list containing all {@link ObjectProvider} objects.
     */
    List<ObjectProvider<?>> getAll();

    /**
     * Returns the amount of elements registered in this store. Will also count all the instances registered from
     * {@link PrototypeObjectProvider}.
     *
     * @return the amount of elements registered in this store.
     */
    Integer count();

    /**
     * Deletes all the elements from this store
     */
    void clear();
}

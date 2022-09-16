package com.w1sh.wave.core;

import java.util.List;

public sealed interface ObjectProvider<T> permits DefinedObjectProvider, SimpleObjectProvider {

    /**
     * Provides the first instance generated by this provider, if no instances exist then one will be created and provided.
     *
     * @return The first instance generated by the provider. Otherwise, a new instance of type {@link T}.
     */
    T singletonInstance();

    /**
     * Provides a new instance of the class
     *
     * @return A new instance of type {@link T}.
     */
    T newInstance();

    /**
     * Return a {@link List} of all the instances generated by this provider.
     * <br>
     * This could be used to retrieve a specific instance of the class if the one provided by {@code singletonInstance()}
     * is not desired.
     *
     * @return The {@link List} of all the instances generated by this provider.
     */
    List<T> instances();
}

package com.w1sh.aperture.core;

public interface PostConstructorProcessor {

    /**
     * Invokes all the methods annotated with {@link javax.annotation.PostConstruct} annotation for the given instance.
     *
     * @param instance The object instance to invoke the methods on.
     */
    void process(Object instance);
}

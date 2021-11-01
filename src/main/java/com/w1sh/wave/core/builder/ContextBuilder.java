package com.w1sh.wave.core.builder;

import com.w1sh.wave.core.Configuration;
import com.w1sh.wave.core.WaveContext;

public class ContextBuilder {

    private static final ThreadLocal<WaveContext> staticContext = new ThreadLocal<>();

    private ContextBuilder() {}

    public static WaveContext staticInstance() {
        WaveContext context = staticContext.get();
        if (context == null) {
            throw new IllegalStateException("The static API can only be used within a singletons() call.");
        }
        return context;
    }

    public static void setStaticContext(WaveContext context) {
        staticContext.set(context);
    }

    public static void clearStaticContext() {
        staticContext.remove();
    }

    public static void singleton(Object instance) {
        staticInstance().register(instance.getClass(), instance);
    }

    public static void singleton(String name, Object instance) {
        staticInstance().register(name, instance);
    }

    public static void singleton(Class<?> clazz) {
        staticInstance().register(clazz);
    }

    public static void singleton(String name, Class<?> clazz) {
        staticInstance().register(name, clazz);
    }

    public static void include(Configuration configuration) {
        configuration.singletons().apply();
    }

    public static void include(Iterable<Configuration> configurations) {
        configurations.forEach(ContextBuilder::include);
    }
}

package com.w1sh.wave.core.builder;

import com.w1sh.wave.core.Configuration;
import com.w1sh.wave.core.WaveContext;

import java.util.function.Supplier;

public class ContextBuilder {

    private static final ThreadLocal<WaveContext> staticContext = new ThreadLocal<>();

    private ContextBuilder() {
    }

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

    public static void singleton(Object instance, Options options) {
        staticInstance().registerSingleton(instance, options);
    }

    public static void singleton(Object instance) {
        singleton(instance, null);
    }

    public static void singleton(Class<?> clazz, Options options) {
        staticInstance().registerSingleton(clazz, options);
    }

    public static void singleton(Class<?> clazz) {
        singleton(clazz, null);
    }

    public static void provider(Class<?> clazz) {
        staticInstance().registerProvider(clazz);
    }

    public static <T> void provider(Class<T> clazz, Supplier<T> supplier) {
        staticInstance().registerProvider(clazz, supplier);
    }

    public static void include(Configuration configuration) {
        configuration.singletons().apply();
    }

    public static void include(Iterable<Configuration> configurations) {
        configurations.forEach(ContextBuilder::include);
    }
}

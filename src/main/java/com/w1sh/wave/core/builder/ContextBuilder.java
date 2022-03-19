package com.w1sh.wave.core.builder;

import com.w1sh.wave.core.Configuration;
import com.w1sh.wave.core.WaveContext;
import com.w1sh.wave.core.condition.Condition;

import java.util.function.Supplier;

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
        staticInstance().registerSingleton(instance.getClass(), instance);
    }

    public static void singleton(String name, Object instance) {
        staticInstance().registerSingleton(name, instance);
    }

    public static void singleton(Class<?> clazz) {
        staticInstance().registerSingleton((String) null, clazz);
    }

    public static void singleton(String name, Class<?> clazz) {
        staticInstance().registerSingleton(name, clazz);
    }

    public static void provider(Class<?> clazz) { staticInstance().registerProvider(clazz); }

    public static <T> void provider(Class<T> clazz, Supplier<T> supplier) { staticInstance().registerProvider(clazz, supplier); }

    public static void singletonIf(Class<?> clazz, Condition condition) {
        // TODO: condition validation, throw if condition is not met
        staticInstance().registerSingleton((String) null, clazz);
    }
    
    public static void singletonIf(String name, Class<?> clazz, Condition condition) {
        // TODO: condition validation, throw if condition is not met
        staticInstance().registerSingleton(name, clazz);
    }

    public static void include(Configuration configuration) {
        configuration.singletons().apply();
    }

    public static void include(Iterable<Configuration> configurations) {
        configurations.forEach(ContextBuilder::include);
    }
}

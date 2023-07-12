package com.w1sh.aperture.core.builder;

import com.w1sh.aperture.core.Aperture;
import com.w1sh.aperture.core.Configuration;

import java.util.function.Supplier;

public class ContextBuilder {

    private static final ThreadLocal<Aperture> staticContext = new ThreadLocal<>();

    private ContextBuilder() {
    }

    public static Aperture staticInstance() {
        Aperture context = staticContext.get();
        if (context == null) {
            throw new IllegalStateException("The context static API can only be used within a Aperture.context() call.");
        }
        return context;
    }

    public static void setStaticContext(Aperture context) {
        staticContext.set(context);
    }

    public static void clearStaticContext() {
        staticContext.remove();
    }

    public static void singleton(Object instance, Options options) {
        staticInstance().getOrchestrator().register(instance, optionsOrDefault(options));
    }

    public static void singleton(Object instance) {
        singleton(instance, null);
    }

    public static void singleton(Class<?> clazz, Options options) {
        staticInstance().getOrchestrator().register(clazz, optionsOrDefault(options));
    }

    public static void singleton(Class<?> clazz) {
        singleton(clazz, null);
    }

    public static void provider(Class<?> clazz, Options options) {
        staticInstance().getOrchestrator().register(clazz, optionsOrDefault(options));
    }

    public static void provider(Class<?> clazz) {
        provider(clazz, (Options) null);
    }

    public static <T> void provider(Class<T> clazz, Supplier<T> supplier) {
        staticInstance().getOrchestrator().register(clazz, optionsOrDefault(null), supplier);
    }

    public static void include(Configuration configuration) {
        configuration.singletons().apply();
    }

    public static void include(Iterable<Configuration> configurations) {
        configurations.forEach(ContextBuilder::include);
    }

    private static Options optionsOrDefault(Options options) {
        return options != null ? options : Options.builder().build();
    }
}

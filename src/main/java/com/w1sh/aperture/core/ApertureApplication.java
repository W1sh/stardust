package com.w1sh.aperture.core;

import com.w1sh.aperture.core.configuration.ApertureConfiguration;

import java.util.Set;

public class ApertureApplication {

    private final Set<Class<?>> sources;
    private ApertureConfiguration configuration;

    public ApertureApplication(Class<?>... sources) {
        this.sources = Set.of(sources);
    }

    public ApertureApplication(ApertureConfiguration configuration, Class<?>... sources) {
        this.sources = Set.of(sources);
        this.configuration = configuration;
    }

    public static ApertureConfiguration configure() {
        return new ApertureConfiguration();
    }

    public static void run(Class<?> primarySource, String... args) {
        new ApertureApplication(primarySource).run(args);
    }

    public void run(String... args) {

    }
}

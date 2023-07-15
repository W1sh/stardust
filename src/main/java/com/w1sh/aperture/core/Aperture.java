package com.w1sh.aperture.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Aperture {

    private static final Logger logger = LoggerFactory.getLogger(Aperture.class);

    private ProviderRegistrationOrchestrator orchestrator;
    private DefaultProviderRegistry registry;
    private ProviderFactory factory;
    private Environment environment;

    private String[] profiles;

    public Aperture() {
        this.registry = new DefaultProviderRegistry();
        this.factory = new ProviderFactory(registry);
    }

    public Aperture profiles(String... profiles) {
        this.profiles = profiles;
        return this;
    }

    public void initialize() {
        this.orchestrator = new ProviderRegistrationOrchestrator(registry, factory, environment);
        logger.info("Starting application running Aperture. Java v{}", System.getProperty("java.version"));
        long startTime = System.nanoTime();
        orchestrator.prepare();
        orchestrator.orchestrate();
        Duration timeTaken = Duration.ofNanos(System.nanoTime() - startTime);
        logger.info("Completed context initialization in {} ms", timeTaken.toNanos() / 1000);
    }

    public ProviderRegistrationOrchestrator getOrchestrator() {
        return orchestrator;
    }
}

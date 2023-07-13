package com.w1sh.aperture.core;

import com.w1sh.aperture.core.builder.Options;
import com.w1sh.aperture.core.condition.ProviderConditionEvaluator;
import com.w1sh.aperture.core.condition.ProviderConditionFactory;
import com.w1sh.aperture.example.service.impl.DuplicateCalculatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.mockito.Mockito.*;

class ProviderRegistrationOrchestratorTest {

    private ProviderRegistrationOrchestrator orchestrator;
    private ProviderFactory factory;
    private ProviderRegistry registry;

    @BeforeEach
    void setUp() {
        registry = spy(new ProviderRegistry());
        ProviderPostConstructorProcessor postConstructorProcessor = new ProviderPostConstructorProcessor();
        factory = spy(new ProviderFactory(registry, postConstructorProcessor));
        ProviderConditionFactory conditionFactory = new ProviderConditionFactory();
        ProviderConditionEvaluator conditionEvaluator = new ProviderConditionEvaluator(registry, new Environment(new HashSet<>()));
        orchestrator = new ProviderRegistrationOrchestrator(factory, registry, conditionFactory, conditionEvaluator);
    }

    @Test
    void should_orchestrateClassRegistration_whenGivenClassIsRegistered() {
        orchestrator.register(DuplicateCalculatorServiceImpl.class, Options.empty());

        orchestrator.orchestrate();

        verify(factory, times(1)).create(any());
        verify(registry, times(1)).register(any(), any(), any());
    }

}
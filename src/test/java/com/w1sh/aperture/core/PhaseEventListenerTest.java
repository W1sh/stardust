package com.w1sh.aperture.core;

import com.w1sh.aperture.core.condition.ProviderConditionEvaluator;
import com.w1sh.aperture.core.condition.ProviderConditionFactory;
import com.w1sh.aperture.core.event.ConfigurationPhaseStartEvent;
import com.w1sh.aperture.core.event.PhaseEventListener;
import com.w1sh.aperture.core.event.RegistrationPhaseEndEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.mockito.Mockito.*;

class PhaseEventListenerTest {

    private ProviderRegistrationOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        DefaultProviderRegistry registry = spy(new DefaultProviderRegistry());
        DefaultProviderFactory factory = spy(new DefaultProviderFactory(registry));
        ProviderConditionFactory conditionFactory = new ProviderConditionFactory();
        ProviderConditionEvaluator conditionEvaluator = new ProviderConditionEvaluator(registry, new Environment(new HashSet<>()));
        orchestrator = new ProviderRegistrationOrchestrator(factory, registry, conditionFactory, conditionEvaluator);
    }

    @Test
    void should_publishEvent_whenListenersAreRegistered() {
        TestEventListener eventListener = spy(new TestEventListener());
        orchestrator.addApplicationListener(eventListener);

        orchestrator.multicast(new ConfigurationPhaseStartEvent() {});

        verify(eventListener, times(0)).onEvent(any());
    }

    @Test
    void should_publishEventAndEventsAreHandled_whenListenersAreRegistered() {
        TestEventListener eventListener = spy(new TestEventListener());
        orchestrator.addApplicationListener(eventListener);

        orchestrator.multicast(new RegistrationPhaseEndEvent() {});

        verify(eventListener, times(1)).onEvent(any());
    }

    private static class TestEventListener implements PhaseEventListener<RegistrationPhaseEndEvent> {
        @Override
        public void onEvent(RegistrationPhaseEndEvent event) {}
    }
}
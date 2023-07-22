package com.w1sh.aperture;

import com.w1sh.aperture.condition.DefaultConditionProcessor;
import com.w1sh.aperture.event.ConfigurationPhaseStartEvent;
import com.w1sh.aperture.event.PhaseEventListener;
import com.w1sh.aperture.event.RegistrationPhaseEndEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class PhaseEventListenerTest {

    private ProviderRegistrationOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        DefaultProviderRegistry registry = spy(new DefaultProviderRegistry());
        DefaultProviderFactory factory = spy(new DefaultProviderFactory(registry));
        DefaultConditionProcessor conditionProcessor = new DefaultConditionProcessor(registry, Environment.empty());
        orchestrator = new ProviderRegistrationOrchestrator(factory, registry, conditionProcessor);
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
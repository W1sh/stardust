package com.w1sh.stardust.health;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class HealthProbeProcessorImplTest {

    private final ScheduledExecutorService executorService = spy(ScheduledExecutorService.class);
    private final HealthProbeProcessor processor = new HealthProbeProcessorImpl(executorService);

    @Test
    void should_returnAllRegisteredProbes() {
        final var delay = 10L;
        final var period = 60L;
        processor.register(new TestHealthProbe(), delay, period);
        processor.register(new TestHealthProbe());

        List<HealthProbe> probes = processor.getAll();

        assertNotNull(probes);
        assertEquals(2, probes.size());
    }

    @Test
    void should_scheduleProbe_whenProbeIsRegisteredWithGivenDelayAndPeriod() {
        final var delay = 10L;
        final var period = 60L;
        processor.register(new TestHealthProbe(), delay, period);

        processor.initializeHealthChecks();

        verify(executorService, times(1)).scheduleAtFixedRate(any(), eq(delay), eq(period), eq(TimeUnit.SECONDS));
    }

    @Test
    void should_scheduleProbe_whenProbeIsRegisteredWithDefaultDelayAndPeriod() {
        processor.register(new TestHealthProbe());

        processor.initializeHealthChecks();

        verify(executorService, times(1)).scheduleAtFixedRate(any(), eq(1L), eq(1L), eq(TimeUnit.SECONDS));
    }

    @Test
    void should_removeProbe_whenProbeIsRegistered() {
        final var testHealthProbe = new TestHealthProbe();
        processor.register(testHealthProbe);

        processor.remove(testHealthProbe);
    }

    private static class TestHealthProbe implements HealthProbe {

        @Override
        public ProbeResult probe() {
            return new ProbeResult(ProbeResult.Status.UP) {};
        }
    }
}
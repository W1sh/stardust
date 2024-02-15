package com.w1sh.stardust.health.probe;

import com.w1sh.stardust.health.ProbeResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationHealthProbeTest {

    @Test
    void should_returnArrayInstance_whenRegisteredArrayIsProviderViaModule() {
        ApplicationHealthProbe healthProbe = new ApplicationHealthProbe();

        ProbeResult probe = healthProbe.probe();

        assertNotNull(probe);
        assertEquals(ProbeResult.up(), probe);
    }

}
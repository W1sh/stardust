package com.w1sh.stardust.health.probe;

import com.w1sh.stardust.health.HealthProbe;
import com.w1sh.stardust.health.Probe;
import com.w1sh.stardust.health.ProbeResult;

@Probe
public class ApplicationHealthProbe implements HealthProbe {

    @Override
    public ProbeResult probe() {
        return ProbeResult.up();
    }
}

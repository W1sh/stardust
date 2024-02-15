package com.w1sh.stardust.health;

import java.util.List;

public interface HealthProbeProcessor {

    List<HealthProbe> getAll();

    List<ProbeResult> getLatestProbeResults();

    void register(HealthProbe healthProbe, long delay, long period);

    void remove(HealthProbe healthProbe);

    void initializeHealthChecks();
}

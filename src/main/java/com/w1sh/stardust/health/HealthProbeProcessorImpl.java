package com.w1sh.stardust.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HealthProbeProcessorImpl implements HealthProbeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(HealthProbeProcessorImpl.class);

    private final List<HealthProbe> probes = new ArrayList<>(8);
    private final Map<HealthProbe, HealthProbeSchedule> probeSchedules = new ConcurrentHashMap<>(8);
    private final Map<HealthProbe, ProbeResult> lastProbeResults = new ConcurrentHashMap<>(8);
    private final ScheduledExecutorService scheduler;

    public HealthProbeProcessorImpl() {
        this(Executors.newScheduledThreadPool(8));
    }

    public HealthProbeProcessorImpl(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public List<HealthProbe> getAll() {
        return probes;
    }

    @Override
    public List<ProbeResult> getLatestProbeResults() {
        return lastProbeResults.values().stream().toList();
    }

    @Override
    public void register(HealthProbe healthProbe, long delay, long period) {
        probes.add(healthProbe);
        probeSchedules.put(healthProbe, new HealthProbeSchedule(delay, period));
    }

    @Override
    public void remove(HealthProbe healthProbe) {
        probes.remove(healthProbe);
        probeSchedules.remove(healthProbe);
    }

    @Override
    public void initializeHealthChecks() {
        logger.info("Initializing health checks for registered {} probes", probeSchedules.size());
        for (Map.Entry<HealthProbe, HealthProbeSchedule> entry : probeSchedules.entrySet()) {
            final var probeSchedule = entry.getValue();
            logger.info("Scheduling health probe of type {} with a period of {} second/s",
                    entry.getKey().getClass(), probeSchedule.period());
            scheduler.scheduleAtFixedRate(() -> {
                ProbeResult probe = entry.getKey().probe();
                lastProbeResults.put(entry.getKey(), probe);
            }, probeSchedule.delay(), probeSchedule.period(), TimeUnit.SECONDS);
        }
    }

    private record HealthProbeSchedule(long delay, long period) {}
}

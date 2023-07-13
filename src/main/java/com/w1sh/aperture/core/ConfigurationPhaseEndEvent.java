package com.w1sh.aperture.core;

public class ConfigurationPhaseEndEvent extends PhaseEvent {
    @Override
    public Phase getPhase() {
        return Phase.CONFIGURATION_END;
    }
}

package com.w1sh.aperture.event;

public class ConfigurationPhaseStartEvent extends PhaseEvent {
    @Override
    public Phase getPhase() {
        return Phase.CONFIGURATION_START;
    }
}

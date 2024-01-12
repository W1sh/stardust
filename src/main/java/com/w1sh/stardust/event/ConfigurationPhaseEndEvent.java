package com.w1sh.stardust.event;

public class ConfigurationPhaseEndEvent extends PhaseEvent {
    @Override
    public Phase getPhase() {
        return Phase.CONFIGURATION_END;
    }
}

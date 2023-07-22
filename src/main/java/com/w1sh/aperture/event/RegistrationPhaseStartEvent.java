package com.w1sh.aperture.event;

public class RegistrationPhaseStartEvent extends PhaseEvent {
    @Override
    public Phase getPhase() {
        return Phase.REGISTRATION_START;
    }
}

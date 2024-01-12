package com.w1sh.stardust.event;

public class RegistrationPhaseStartEvent extends PhaseEvent {
    @Override
    public Phase getPhase() {
        return Phase.REGISTRATION_START;
    }
}

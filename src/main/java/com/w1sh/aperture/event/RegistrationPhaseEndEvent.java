package com.w1sh.aperture.event;

public class RegistrationPhaseEndEvent extends PhaseEvent {
    @Override
    public Phase getPhase() {
        return Phase.REGISTRATION_END;
    }
}

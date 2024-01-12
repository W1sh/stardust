package com.w1sh.stardust.event;

public class RegistrationPhaseEndEvent extends PhaseEvent {
    @Override
    public Phase getPhase() {
        return Phase.REGISTRATION_END;
    }
}

package com.w1sh.aperture.core;

public abstract class PhaseEvent {

    public enum Phase {
        CONFIGURATION_START, CONFIGURATION_END, REGISTRATION_START, REGISTRATION_END
    }

    public abstract Phase getPhase();
}

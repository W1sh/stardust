package com.w1sh.stardust.event;

public abstract class PhaseEvent {

    public enum Phase {
        CONFIGURATION_START, CONFIGURATION_END, REGISTRATION_START, REGISTRATION_END
    }

    public abstract Phase getPhase();

    public static PhaseEvent configurationPhaseStart() {
        return new ConfigurationPhaseStartEvent();
    }

    public static PhaseEvent configurationPhaseEnd() {
        return new ConfigurationPhaseEndEvent();
    }

    public static PhaseEvent registrationPhaseStart() {
        return new RegistrationPhaseStartEvent();
    }

    public static PhaseEvent registrationPhaseEnd() {
        return new RegistrationPhaseEndEvent();
    }

    public static final class ConfigurationPhaseStartEvent extends PhaseEvent {
        @Override
        public Phase getPhase() {
            return Phase.CONFIGURATION_START;
        }
    }

    public static final class ConfigurationPhaseEndEvent extends PhaseEvent {
        @Override
        public Phase getPhase() {
            return Phase.CONFIGURATION_END;
        }
    }

    public static final class RegistrationPhaseStartEvent extends PhaseEvent {
        @Override
        public Phase getPhase() {
            return Phase.REGISTRATION_START;
        }
    }

    public static final class RegistrationPhaseEndEvent extends PhaseEvent {
        @Override
        public Phase getPhase() {
            return Phase.REGISTRATION_END;
        }
    }
}

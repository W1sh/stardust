package com.w1sh.wave.core.phase;

import java.util.List;

public interface EventDispatcher {

    void addListener(PhaseListener phaseListener);

    default void addListeners(List<PhaseListener> phaseListeners) {
        phaseListeners.forEach(this::addListener);
    }

    void removeListener(PhaseListener phaseListener);

    default void removeListeners(List<PhaseListener> phaseListeners) {
        phaseListeners.forEach(this::removeListener);
    }

    void emit();
}

package com.w1sh.aperture.core;

import com.w1sh.aperture.core.util.Types;

import java.util.EventListener;

/**
 * Interface to be implemented by aperture event listeners.
 *
 * <p>Based on the standard {@link java.util.EventListener} interface for the
 * Observer design pattern.
 *
 * @param <T> the specific {@code ApertureEvent} subclass to listen to
 * @see PhaseEvent
 */
@FunctionalInterface
public interface PhaseEventListener<T extends PhaseEvent> extends EventListener {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    void onEvent(T event);

    /**
     * Returns the {@link Class} of event listened by the {@link EventListener}.
     *
     * @return The {@link Class} of event listened by the {@link EventListener}.
     */
    @SuppressWarnings("unchecked")
    default Class<? extends T> getEventType() {
        return (Class<? extends T>) Types.getInterfaceActualTypeArgument(getClass(), 0);
    }

}

package com.w1sh.aperture.core;

/**
 * Interface to be implemented by aperture event publishers.
 *
 * @see PhaseEvent
 */
public interface PhaseEventMulticaster {

    /**
     * Add a listener to be notified of all events.
     *
     * @param listener the listener to add
     * @see #removeApplicationListener(PhaseEventListener listener)
     */
    <T extends PhaseEvent> void addApplicationListener(PhaseEventListener<T> listener);

    /**
     * Remove a listener from the notification list.
     *
     * @param listener the listener to remove
     * @see #addApplicationListener(PhaseEventListener listener)
     */
    <T extends PhaseEvent> void removeApplicationListener(PhaseEventListener<T> listener);

    /**
     * Remove all listeners registered with this multicaster.
     */
    void removeAllListeners();

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    <T extends PhaseEvent> void multicast(T event);

}

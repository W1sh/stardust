package com.w1sh.wave.core.phase;

import com.w1sh.wave.core.AbstractApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PhasedApplicationContext extends AbstractApplicationContext implements EventDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(PhasedApplicationContext.class);

    private List<PhaseListener> phaseListeners;
    private Phase phase;

    @Override
    protected void initialize() {

    }

    @Override
    public void addListener(PhaseListener phaseListener) {
        phaseListeners.add(phaseListener);
    }

    @Override
    public void removeListener(PhaseListener phaseListener) {
        phaseListeners.remove(phaseListener);
    }

    @Override
    public void emit() {
        switch (phase) {
            case INITIALIZATION -> phaseListeners.forEach(PhaseListener::onInitialization);
            case SCANNING_PACKAGES -> phaseListeners.forEach(PhaseListener::onScanningPackages);
            case FILTERING_CONDITIONALS -> phaseListeners.forEach(PhaseListener::onFilteringConditionals);
            case CREATING_INSTANCES -> phaseListeners.forEach(PhaseListener::onCreatingInstances);
            case POST_CONSTRUCTOR_PROCESSING -> phaseListeners.forEach(PhaseListener::onPostConstructorProcessing);
            case FINALIZATION -> phaseListeners.forEach(PhaseListener::onFinalization);
        }
    }
}

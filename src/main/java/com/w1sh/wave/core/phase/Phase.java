package com.w1sh.wave.core.phase;

public enum Phase {
    INITIALIZATION, SCANNING_PACKAGES, FILTERING_CONDITIONALS, CREATING_INSTANCES, POST_CONSTRUCTOR_PROCESSING, FINALIZATION;

    public static Phase next(Phase phase) {
        return null;
    }
    
}

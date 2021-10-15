package com.w1sh.wave.core.phase;

public interface PhaseListener {

    void onInitialization();

    void onScanningPackages();

    void onFilteringConditionals();

    void onCreatingInstances();

    void onPostConstructorProcessing();

    void onFinalization();
}

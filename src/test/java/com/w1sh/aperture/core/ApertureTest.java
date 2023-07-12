package com.w1sh.aperture.core;

import com.w1sh.aperture.core.Aperture;
import org.junit.jupiter.api.Test;

class ApertureTest {

    @Test
    void should_initializeContext_whenCalled() {
        new Aperture().initialize();
    }
}
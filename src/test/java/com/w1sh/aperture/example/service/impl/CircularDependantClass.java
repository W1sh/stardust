package com.w1sh.aperture.example.service.impl;

import com.w1sh.aperture.core.annotation.Inject;

public class CircularDependantClass {

    private final CircularDependantClass dependantClass;

    @Inject
    public CircularDependantClass(CircularDependantClass circularDependantClass) {
        this.dependantClass = circularDependantClass;
    }
}

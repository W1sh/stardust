package com.w1sh.aperture.example.service.impl;

import com.w1sh.aperture.core.annotation.Inject;

public class CircularDependantClass {

    @Inject
    public CircularDependantClass(CircularDependantClass circularDependantClass) {}
}

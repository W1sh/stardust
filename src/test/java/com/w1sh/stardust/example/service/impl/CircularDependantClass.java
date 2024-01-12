package com.w1sh.stardust.example.service.impl;

import com.w1sh.stardust.annotation.Inject;
import com.w1sh.stardust.annotation.Provide;

@Provide
public class CircularDependantClass {

    @Inject
    public CircularDependantClass(CircularDependantClass circularDependantClass) {}
}

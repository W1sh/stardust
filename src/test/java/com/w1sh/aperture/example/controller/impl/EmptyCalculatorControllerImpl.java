package com.w1sh.aperture.example.controller.impl;

import com.w1sh.aperture.annotation.Profile;
import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.example.controller.CalculatorController;

@Profile("non-test")
@Provide
public class EmptyCalculatorControllerImpl implements CalculatorController {

    @Override
    public Integer calculate(int first, int second) {
        return first + second;
    }
}

package com.w1sh.aperture.example.controller.impl;

import com.w1sh.aperture.example.controller.CalculatorController;

public class EmptyCalculatorControllerImpl implements CalculatorController {

    @Override
    public Integer calculate(int first, int second) {
        return first + second;
    }
}

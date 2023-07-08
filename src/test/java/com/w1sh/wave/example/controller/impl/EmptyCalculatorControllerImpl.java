package com.w1sh.wave.example.controller.impl;

import com.w1sh.wave.example.controller.CalculatorController;

public class EmptyCalculatorControllerImpl implements CalculatorController {

    @Override
    public Integer calculate(int first, int second) {
        return first + second;
    }
}

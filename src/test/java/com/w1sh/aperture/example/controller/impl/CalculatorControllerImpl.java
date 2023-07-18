package com.w1sh.aperture.example.controller.impl;

import com.w1sh.aperture.core.annotation.Primary;
import com.w1sh.aperture.example.controller.CalculatorController;

@Primary
public class CalculatorControllerImpl implements CalculatorController {

    public Integer calculate(int first, int second) {
        return first + second;
    }
}

package com.w1sh.stardust.example.controller.impl;

import com.w1sh.stardust.annotation.Profile;
import com.w1sh.stardust.annotation.Provide;
import com.w1sh.stardust.example.controller.CalculatorController;

@Profile("non-test")
@Provide
public class EmptyCalculatorControllerImpl implements CalculatorController {

    @Override
    public Integer calculate(int first, int second) {
        return first + second;
    }
}

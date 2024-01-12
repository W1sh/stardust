package com.w1sh.stardust.example.controller.impl;

import com.w1sh.stardust.annotation.Primary;
import com.w1sh.stardust.annotation.Provide;
import com.w1sh.stardust.example.controller.CalculatorController;

@Primary
@Provide
public class CalculatorControllerImpl implements CalculatorController {

    public Integer calculate(int first, int second) {
        return first + second;
    }
}

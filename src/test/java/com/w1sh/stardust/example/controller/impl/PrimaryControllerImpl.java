package com.w1sh.stardust.example.controller.impl;

import com.w1sh.stardust.annotation.Primary;
import com.w1sh.stardust.annotation.Profile;
import com.w1sh.stardust.annotation.Provide;
import com.w1sh.stardust.example.controller.CalculatorController;

@Profile("test")
@Primary
@Provide
public class PrimaryControllerImpl implements CalculatorController {

    @Override
    public Integer calculate(int first, int second) {
        return null;
    }
}

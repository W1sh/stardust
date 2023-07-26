package com.w1sh.aperture.example.controller.impl;

import com.w1sh.aperture.annotation.Primary;
import com.w1sh.aperture.annotation.Profile;
import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.example.controller.CalculatorController;

@Profile("test")
@Primary
@Provide
public class PrimaryControllerImpl implements CalculatorController {

    @Override
    public Integer calculate(int first, int second) {
        return null;
    }
}

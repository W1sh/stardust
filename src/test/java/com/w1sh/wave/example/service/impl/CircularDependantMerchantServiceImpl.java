package com.w1sh.wave.example.service.impl;

import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.example.service.CalculatorService;

@Component(name = "merchantService")
public class CircularDependantMerchantServiceImpl {

    private final CalculatorService calculatorService;

    @Inject
    public CircularDependantMerchantServiceImpl(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }
}

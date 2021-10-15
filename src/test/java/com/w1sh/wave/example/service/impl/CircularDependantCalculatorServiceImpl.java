package com.w1sh.wave.example.service.impl;

import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.example.service.MerchantService;

@Component(name = "calculatorService")
public class CircularDependantCalculatorServiceImpl {

    private final MerchantService merchantService;

    @Inject
    public CircularDependantCalculatorServiceImpl(MerchantService merchantService) {
        this.merchantService = merchantService;
    }
}

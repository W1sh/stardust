package com.w1sh.aperture.example.controller.impl;

import com.w1sh.aperture.annotation.Inject;
import com.w1sh.aperture.example.controller.CalculatorController;
import com.w1sh.aperture.example.service.MerchantService;

public class ArrayDependantControllerImpl implements CalculatorController {

    private final MerchantService[] merchantServices;

    @Inject
    public ArrayDependantControllerImpl(MerchantService[] merchantServices) {
        this.merchantServices = merchantServices;
    }

    @Override
    public Integer calculate(int first, int second) {
        return null;
    }
}

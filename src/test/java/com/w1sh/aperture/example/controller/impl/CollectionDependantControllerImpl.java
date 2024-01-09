package com.w1sh.aperture.example.controller.impl;

import com.w1sh.aperture.annotation.Inject;
import com.w1sh.aperture.example.controller.CalculatorController;
import com.w1sh.aperture.example.service.MerchantService;

import java.util.Set;

public class CollectionDependantControllerImpl implements CalculatorController {

    private final Set<MerchantService> merchantServices;

    @Inject
    public CollectionDependantControllerImpl(Set<MerchantService> merchantServices) {
        this.merchantServices = merchantServices;
    }

    @Override
    public Integer calculate(int first, int second) {
        return null;
    }
}

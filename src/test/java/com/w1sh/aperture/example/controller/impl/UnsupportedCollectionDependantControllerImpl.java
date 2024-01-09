package com.w1sh.aperture.example.controller.impl;

import com.w1sh.aperture.annotation.Inject;
import com.w1sh.aperture.example.controller.CalculatorController;
import com.w1sh.aperture.example.service.MerchantService;

import java.util.Queue;

public class UnsupportedCollectionDependantControllerImpl implements CalculatorController {

    private final Queue<MerchantService> merchantServices;

    @Inject
    public UnsupportedCollectionDependantControllerImpl(Queue<MerchantService> merchantServices) {
        this.merchantServices = merchantServices;
    }

    @Override
    public Integer calculate(int first, int second) {
        return null;
    }
}

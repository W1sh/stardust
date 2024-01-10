package com.w1sh.aperture.example.controller.impl;

import com.w1sh.aperture.annotation.Inject;
import com.w1sh.aperture.example.controller.CalculatorController;
import com.w1sh.aperture.example.service.MerchantService;

import java.util.Queue;
import java.util.Set;

public class CollectionDependantControllerImpl implements CalculatorController {

    private final Set<MerchantService> merchantServices;
    private final Queue<MerchantService> merchantServicesQueue;
    private final MerchantService[] merchantServicesArray;

    @Inject
    public CollectionDependantControllerImpl(Set<MerchantService> merchantServices) {
        this.merchantServices = merchantServices;
        this.merchantServicesQueue = null;
        this.merchantServicesArray = null;
    }

    public CollectionDependantControllerImpl(Queue<MerchantService> merchantServices) {
        this.merchantServices = null;
        this.merchantServicesQueue = merchantServices;
        this.merchantServicesArray = null;
    }

    public CollectionDependantControllerImpl(MerchantService[] merchantServices) {
        this.merchantServices = null;
        this.merchantServicesQueue = null;
        this.merchantServicesArray = merchantServices;
    }

    @Override
    public Integer calculate(int first, int second) {
        return null;
    }
}

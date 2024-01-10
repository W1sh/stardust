package com.w1sh.aperture.example.controller.impl;

import com.w1sh.aperture.annotation.Inject;
import com.w1sh.aperture.annotation.Required;
import com.w1sh.aperture.example.controller.CalculatorController;
import com.w1sh.aperture.example.service.MerchantService;

public class RequiredDependantControllerImpl implements CalculatorController {

    private MerchantService merchantService;

    public RequiredDependantControllerImpl() {}

    @Inject
    public RequiredDependantControllerImpl(@Required MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @Override
    public Integer calculate(int first, int second) {
        return null;
    }

    @Inject
    public void setMerchantService(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    public MerchantService getMerchantService() {
        return merchantService;
    }
}

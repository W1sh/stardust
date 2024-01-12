package com.w1sh.stardust.example.controller.impl;

import com.w1sh.stardust.annotation.Inject;
import com.w1sh.stardust.annotation.Required;
import com.w1sh.stardust.example.controller.CalculatorController;
import com.w1sh.stardust.example.service.MerchantService;

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

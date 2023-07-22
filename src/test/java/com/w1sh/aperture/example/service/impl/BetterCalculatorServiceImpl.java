package com.w1sh.aperture.example.service.impl;

import com.w1sh.aperture.annotation.Inject;
import com.w1sh.aperture.example.service.CalculatorService;
import com.w1sh.aperture.example.service.MerchantService;

public class BetterCalculatorServiceImpl implements CalculatorService {

    private final MerchantService merchantService;

    @Inject
    public BetterCalculatorServiceImpl(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    public MerchantService getMerchantService() {
        return merchantService;
    }
}

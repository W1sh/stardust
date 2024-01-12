package com.w1sh.stardust.example.service.impl;

import com.w1sh.stardust.annotation.Inject;
import com.w1sh.stardust.annotation.Provide;
import com.w1sh.stardust.example.service.CalculatorService;
import com.w1sh.stardust.example.service.MerchantService;

@Provide
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

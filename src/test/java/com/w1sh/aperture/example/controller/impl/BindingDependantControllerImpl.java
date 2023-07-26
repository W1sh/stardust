package com.w1sh.aperture.example.controller.impl;

import com.w1sh.aperture.annotation.Inject;
import com.w1sh.aperture.annotation.Primary;
import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.binding.Lazy;
import com.w1sh.aperture.binding.Provider;
import com.w1sh.aperture.example.controller.CalculatorController;
import com.w1sh.aperture.example.service.MerchantService;

@Provide
public class BindingDependantControllerImpl implements CalculatorController {

    private final Lazy<MerchantService> lazyMerchantService;
    private final Provider<MerchantService> merchantServiceProvider;

    @Inject
    public BindingDependantControllerImpl(Lazy<MerchantService> merchantService, Provider<MerchantService> merchantServiceProvider) {
        this.lazyMerchantService = merchantService;
        this.merchantServiceProvider = merchantServiceProvider;
    }

    @Override
    public Integer calculate(int first, int second) {
        return null;
    }
}

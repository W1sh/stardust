package com.w1sh.aperture.example.service.impl;

import com.w1sh.aperture.annotation.Inject;
import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.example.service.CalculatorService;

@Provide
public class CalculatorServiceImpl implements CalculatorService {
    @Inject
    public CalculatorServiceImpl() { }
}

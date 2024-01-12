package com.w1sh.stardust.example.service.impl;

import com.w1sh.stardust.annotation.Inject;
import com.w1sh.stardust.annotation.Provide;
import com.w1sh.stardust.example.service.CalculatorService;

@Provide
public class CalculatorServiceImpl implements CalculatorService {
    @Inject
    public CalculatorServiceImpl() { }
}

package com.w1sh.wave.core;

import com.w1sh.wave.example.service.impl.BetterCalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleNamingStrategyTest {

    private final NamingStrategy namingStrategy = new SimpleNamingStrategy();

    @Test
    void should_returnSingletonName_whenInvokingGenerateWithGivenClass(){
        assertEquals("betterCalculatorServiceImpl", namingStrategy.generate(BetterCalculatorServiceImpl.class));
    }

}
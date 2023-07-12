package com.w1sh.aperture.core;

import com.w1sh.aperture.core.naming.DefaultNamingStrategy;
import com.w1sh.aperture.core.naming.NamingStrategy;
import com.w1sh.aperture.example.service.impl.BetterCalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultNamingStrategyTest {

    private final NamingStrategy namingStrategy = new DefaultNamingStrategy();

    @Test
    void should_returnSingletonName_whenInvokingGenerateWithGivenClass(){
        assertEquals("betterCalculatorServiceImpl", namingStrategy.generate(BetterCalculatorServiceImpl.class));
    }

}
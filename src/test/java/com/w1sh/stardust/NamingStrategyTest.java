package com.w1sh.stardust;

import com.w1sh.stardust.example.service.impl.BetterCalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NamingStrategyTest {

    private final NamingStrategy defaultStrategy = new NamingStrategy.Default();
    private final NamingStrategy qualifiedStrategy = new NamingStrategy.Qualified();

    @Test
    void should_returnDefaultSingletonName_whenInvokingGenerateWithGivenClass() {
        assertEquals("betterCalculatorServiceImpl", defaultStrategy.generate(BetterCalculatorServiceImpl.class));
    }

    @Test
    void should_returnQualifiedSingletonName_whenInvokingGenerateWithGivenClass() {
        assertEquals("com.w1sh.stardust.example.service.impl.BetterCalculatorServiceImpl", qualifiedStrategy.generate(BetterCalculatorServiceImpl.class));
    }
}
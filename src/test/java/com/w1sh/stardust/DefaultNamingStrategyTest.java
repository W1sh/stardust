package com.w1sh.stardust;

import com.w1sh.stardust.naming.DefaultNamingStrategy;
import com.w1sh.stardust.naming.NamingStrategy;
import com.w1sh.stardust.example.service.impl.BetterCalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultNamingStrategyTest {

    private final NamingStrategy namingStrategy = new DefaultNamingStrategy();

    @Test
    void should_returnSingletonName_whenInvokingGenerateWithGivenClass(){
        assertEquals("betterCalculatorServiceImpl", namingStrategy.generate(BetterCalculatorServiceImpl.class));
    }

}
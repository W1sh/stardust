package com.w1sh.aperture;

import com.w1sh.aperture.naming.NamingStrategy;
import com.w1sh.aperture.naming.QualifiedNamingStrategy;
import com.w1sh.aperture.example.service.impl.BetterCalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QualifiedNamingStrategyTest {

    private final NamingStrategy namingStrategy = new QualifiedNamingStrategy();

    @Test
    void should_returnSingletonName_whenInvokingGenerateWithGivenClass(){
        assertEquals("com.w1sh.aperture.example.service.impl.BetterCalculatorServiceImpl", namingStrategy.generate(BetterCalculatorServiceImpl.class));
    }

}
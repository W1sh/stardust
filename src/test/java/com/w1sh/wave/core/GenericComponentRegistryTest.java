package com.w1sh.wave.core;

import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.LazyServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GenericComponentRegistryTest {

    private final ComponentDefinitionFactory factory = Mockito.spy(new GenericComponentDefinitionFactory());
    private final ComponentRegistry registry = new GenericComponentRegistry(factory);

    @Test
    void test(){
        // TODO: complete
        registry.register(LazyServiceImpl.class);
        registry.register(CalculatorServiceImpl.class);
    }
}
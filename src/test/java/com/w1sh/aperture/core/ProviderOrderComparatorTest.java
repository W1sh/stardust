package com.w1sh.aperture.core;

import com.w1sh.aperture.core.builder.Options;
import com.w1sh.aperture.example.controller.impl.CalculatorControllerImpl;
import com.w1sh.aperture.example.controller.impl.EmptyCalculatorControllerImpl;
import com.w1sh.aperture.example.service.impl.CalculatorServiceImpl;
import com.w1sh.aperture.example.service.impl.DuplicateCalculatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProviderOrderComparatorTest {

    private List<InitializationContext<?>> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();
        list.add(new ConstructorInitializationContext<>(CalculatorServiceImpl.class, Options.builder().order(4).build()));
        list.add(new ConstructorInitializationContext<>(DuplicateCalculatorServiceImpl.class, Options.builder().order(2).build()));
        list.add(new ConstructorInitializationContext<>(EmptyCalculatorControllerImpl.class, Options.builder().order(3).build()));
        list.add(new ConstructorInitializationContext<>(CalculatorControllerImpl.class, Options.builder().order(1).build()));
    }

    @Test
    void should_returnProvider_whenGivenValidConstructorInitializationContext() {
        ProviderOrderComparator.sort(list);

        assertNotNull(list);
        assertEquals(CalculatorControllerImpl.class, list.get(0).getClazz());
        assertEquals(DuplicateCalculatorServiceImpl.class, list.get(1).getClazz());
        assertEquals(EmptyCalculatorControllerImpl.class, list.get(2).getClazz());
        assertEquals(CalculatorServiceImpl.class, list.get(3).getClazz());
    }

}
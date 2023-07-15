package com.w1sh.aperture.core;

import com.w1sh.aperture.example.controller.impl.CalculatorControllerImpl;
import com.w1sh.aperture.example.controller.impl.EmptyCalculatorControllerImpl;
import com.w1sh.aperture.example.service.impl.CalculatorServiceImpl;
import com.w1sh.aperture.example.service.impl.DuplicateCalculatorServiceImpl;
import com.w1sh.aperture.util.Tests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProviderPriorityComparatorTest {

    private List<Definition<?>> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();
        list.add(Tests.definition(CalculatorServiceImpl.class, Metadata.builder().priority(4).build()));
        list.add(Tests.definition(DuplicateCalculatorServiceImpl.class, Metadata.builder().priority(2).build()));
        list.add(Tests.definition(EmptyCalculatorControllerImpl.class, Metadata.builder().priority(3).build()));
        list.add(Tests.definition(CalculatorControllerImpl.class, Metadata.builder().priority(1).build()));
    }

    @Test
    void should_returnProvider_whenGivenValidConstructorInitializationContext() {
        ProviderPriorityComparator.sort(list);

        assertNotNull(list);
        assertEquals(CalculatorControllerImpl.class, list.get(0).getClazz());
        assertEquals(DuplicateCalculatorServiceImpl.class, list.get(1).getClazz());
        assertEquals(EmptyCalculatorControllerImpl.class, list.get(2).getClazz());
        assertEquals(CalculatorServiceImpl.class, list.get(3).getClazz());
    }

}
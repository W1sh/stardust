package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.builder.Options;
import com.w1sh.aperture.example.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProviderConditionFactoryTest {

    private ProviderConditionFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ProviderConditionFactory();
    }

    @Test
    void should_returnNoConditions_whenGivenDefaultOptions() {
        final var options = Options.builder().build();

        var conditions = factory.create(options);

        assertNotNull(conditions);
        assertEquals(0, conditions.size());
    }
}
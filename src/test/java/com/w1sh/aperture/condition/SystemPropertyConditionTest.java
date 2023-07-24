package com.w1sh.aperture.condition;

import com.w1sh.aperture.ProviderContainerImpl;
import com.w1sh.aperture.Environment;
import com.w1sh.aperture.Metadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class SystemPropertyConditionTest {

    private DefaultConditionProcessor evaluator;

    @BeforeEach
    void setUp() {
        ProviderContainerImpl registry = new ProviderContainerImpl();
        evaluator = new DefaultConditionProcessor(registry, Environment.builder().build());
    }

    @Test
    void should_returnTrue_whenGivenSystemPropertyConditionMatches() {
        var option = Metadata.builder().requiredSystemProperty("java.version", "19.0.2").build();
        var condition = new SystemPropertyCondition(option.requiredSystemProperties());

        boolean shouldSkip = evaluator.shouldSkip(List.of(condition));

        assertFalse(shouldSkip);
    }

}
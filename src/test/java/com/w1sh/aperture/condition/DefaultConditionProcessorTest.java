package com.w1sh.aperture.condition;

import com.w1sh.aperture.ProviderContainerImpl;
import com.w1sh.aperture.Environment;
import com.w1sh.aperture.Metadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class DefaultConditionProcessorTest {

    private DefaultConditionProcessor processor;

    @BeforeEach
    void setUp() {
        ProviderContainerImpl registry = spy(new ProviderContainerImpl());
        Environment environment = Environment.builder()
                .profiles("test")
                .build();
        processor = new DefaultConditionProcessor(registry, environment);
    }

    @Test
    void should_returnTrue_whenEvaluatingNoConditions() {
        boolean shouldSkip = processor.shouldSkip(new ArrayList<>());

        assertFalse(shouldSkip);
    }

    @Test
    void should_returnTrue_whenAllConditionsMatchEvaluationPhase() {
        ActiveProfileCondition condition = new ActiveProfileCondition(List.of("dev"));
        boolean match = processor.allMatch(List.of(condition), Condition.EvaluationPhase.CONFIGURATION);

        assertTrue(match);
    }

    @Test
    void should_returnTrue_whenEvaluatingActiveProfileConditionAndProfileIsActive() {
        ActiveProfileCondition condition = new ActiveProfileCondition(List.of("test"));

        boolean shouldSkip = processor.shouldSkip(List.of(condition));

        assertFalse(shouldSkip);
    }

    @Test
    void should_returnFalse_whenEvaluatingActiveProfileConditionAndProfileIsNotActive() {
        ActiveProfileCondition condition = new ActiveProfileCondition(List.of("dev"));

        boolean shouldSkip = processor.shouldSkip(List.of(condition));

        assertTrue(shouldSkip);
    }

    @Test
    void should_returnEmptyConditionsList_whenCreatingFromEmptyMetadata() {
        List<? extends Condition> conditions = processor.create(Metadata.empty());

        assertNotNull(conditions);
        assertEquals(0, conditions.size());
    }

    @Test
    void should_returnConditionsList_whenCreatingFromMetadata() {
        processor.addFactory(new ActiveProfileConditionFactory());
        processor.addFactory(new SystemPropertyConditionFactory());
        List<? extends Condition> conditions = processor.create(Metadata.builder()
                .profiles("test")
                .requiredSystemProperty("test", "test")
                .build());

        assertNotNull(conditions);
        assertEquals(2, conditions.size());
    }
}
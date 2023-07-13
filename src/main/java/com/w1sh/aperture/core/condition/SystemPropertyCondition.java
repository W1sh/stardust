package com.w1sh.aperture.core.condition;

import java.util.Map;

public class SystemPropertyCondition implements Condition {

    private final Map<String, String> expectedKeyValues;

    public SystemPropertyCondition(Map<String, String> expectedKeyValues) {
        this.expectedKeyValues = expectedKeyValues;
    }

    @Override
    public boolean matches(ConditionContext context) {
        return expectedKeyValues.entrySet().stream()
                .allMatch(entry -> System.getProperty(entry.getKey()).equalsIgnoreCase(entry.getValue()));
    }

    @Override
    public EvaluationPhase getEvaluationPhase() {
        return EvaluationPhase.CONFIGURATION;
    }
}

package com.w1sh.aperture.core.condition;

import java.util.Map;

public class SystemPropertyCondition implements Condition {

    private final Map.Entry<String, String> expectedKeyValue;

    public SystemPropertyCondition(Map.Entry<String, String> expectedKeyValue) {
        this.expectedKeyValue = expectedKeyValue;
    }

    @Override
    public boolean matches(ConditionContext context) {
        return System.getProperty(expectedKeyValue.getKey()).equalsIgnoreCase(expectedKeyValue.getValue());
    }

    @Override
    public EvaluationPhase getEvaluationPhase() {
        return EvaluationPhase.CONFIGURATION;
    }
}

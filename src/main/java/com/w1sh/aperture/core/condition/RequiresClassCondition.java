package com.w1sh.aperture.core.condition;

import java.util.List;

public class RequiresClassCondition implements Condition {

    private final List<Class<?>> requiredClasses;

    public RequiresClassCondition(List<Class<?>> requiredClasses) {
        this.requiredClasses = requiredClasses;
    }

    @Override
    public boolean matches(ConditionContext context) {
        return requiredClasses.stream().allMatch(clazz -> context.registry().contains(clazz));
    }

    @Override
    public EvaluationPhase getConfigurationPhase() {
        return EvaluationPhase.REGISTRATION;
    }
}

package com.w1sh.aperture.core.condition;

import java.util.List;

public class RequiresMissingClassCondition implements Condition {

    private final List<Class<?>> requiredMissingClasses;

    public RequiresMissingClassCondition(List<Class<?>> requiredMissingClasses) {
        this.requiredMissingClasses = requiredMissingClasses;
    }

    @Override
    public boolean matches(ConditionContext context) {
        return requiredMissingClasses.stream().noneMatch(clazz -> context.registry().contains(clazz));
    }

    @Override
    public EvaluationPhase getConfigurationPhase() {
        return EvaluationPhase.REGISTRATION;
    }
}

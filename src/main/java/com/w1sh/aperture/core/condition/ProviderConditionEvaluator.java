package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.Environment;
import com.w1sh.aperture.core.DefaultProviderRegistry;

import java.util.List;

public class ProviderConditionEvaluator {

    private final ConditionContext context;

    public ProviderConditionEvaluator(DefaultProviderRegistry registry, Environment environment) {
        this.context = new ConditionContext(registry, environment);
    }

    public boolean shouldSkip(List<? extends Condition> conditions) {
        for (Condition condition : conditions) {
            if (!condition.matches(context)) {
                return true;
            }
        }
        return false;
    }

    public boolean canEvaluateEarly(List<? extends Condition> conditions) {
        return conditions != null && conditions.stream()
                .anyMatch(condition -> Condition.EvaluationPhase.REGISTRATION.equals(condition.getEvaluationPhase()));
    }
}

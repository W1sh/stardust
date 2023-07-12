package com.w1sh.aperture.core.condition;

import java.util.List;

public class ActiveProfileCondition implements Condition {

    private final List<String> profiles;

    public ActiveProfileCondition(List<String> profiles) {
        this.profiles = profiles;
    }

    @Override
    public boolean matches(ConditionContext context) {
        return context.environment().activeProfiles().containsAll(profiles);
    }

    @Override
    public EvaluationPhase getConfigurationPhase() {
        return EvaluationPhase.CONFIGURATION;
    }
}

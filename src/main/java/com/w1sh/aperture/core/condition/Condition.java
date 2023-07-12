package com.w1sh.aperture.core.condition;

public interface Condition {

    boolean matches(ConditionContext context);

    /**
     * Return the {@link EvaluationPhase} in which the condition should be evaluated.
     */
    EvaluationPhase getEvaluationPhase();

    /**
     * The various phases where the condition could be evaluated.
     */
    enum EvaluationPhase {
        CONFIGURATION, REGISTRATION
    }
}

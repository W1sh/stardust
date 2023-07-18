package com.w1sh.aperture.core.condition;

import java.util.List;

public interface ConditionEvaluator {

    boolean shouldSkip(List<? extends Condition> conditions);

    boolean allMatch(List<? extends Condition> conditions, Condition.EvaluationPhase phase);
}

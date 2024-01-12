package com.w1sh.stardust.dependency;

import com.w1sh.stardust.Environment;

public interface DependencyResolver {

    boolean matches(Class<?> clazz, Environment environment);

    /**
     * Return the {@link EvaluationPhase} in which the dependency should be evaluated.
     */
    EvaluationPhase getEvaluationPhase();

    /**
     * The various phases where the dependency could be evaluated.
     */
    enum EvaluationPhase {
        BEFORE_REGISTRATION, REGISTRATION
    }
}

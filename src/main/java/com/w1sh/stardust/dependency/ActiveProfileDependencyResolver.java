package com.w1sh.stardust.dependency;

import com.w1sh.stardust.Environment;
import com.w1sh.stardust.annotation.Profile;

import java.util.Arrays;

public class ActiveProfileDependencyResolver implements DependencyResolver {

    @Override
    public boolean matches(Class<?> clazz, Environment environment) {
        Profile annotation = clazz.getAnnotation(Profile.class);
        return environment.activeProfiles().containsAll(Arrays.asList(annotation.value()));
    }

    @Override
    public EvaluationPhase getEvaluationPhase() {
        return EvaluationPhase.BEFORE_REGISTRATION;
    }
}

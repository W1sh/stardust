package com.w1sh.aperture.dependency;

import com.w1sh.aperture.Environment;
import com.w1sh.aperture.annotation.Profile;
import com.w1sh.aperture.annotation.Provide;

import java.util.Arrays;

@Provide
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

package com.w1sh.aperture.dependency;

import com.w1sh.aperture.Environment;
import com.w1sh.aperture.annotation.Provide;

import java.util.Arrays;

@Provide
public class MissingClassDependencyResolver implements DependencyResolver {

    @Override
    public boolean matches(Class<?> clazz, Environment environment) {
        DependsOnMissingClass annotation = clazz.getAnnotation(DependsOnMissingClass.class);
        return Arrays.stream(annotation.value()).noneMatch(aClass -> environment.container().contains(aClass));
    }

    @Override
    public EvaluationPhase getEvaluationPhase() {
        return EvaluationPhase.BEFORE_REGISTRATION;
    }
}

package com.w1sh.stardust.dependency;

import com.w1sh.stardust.Environment;

import java.util.Arrays;

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

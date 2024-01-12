package com.w1sh.stardust.dependency;

import com.w1sh.stardust.Environment;
import com.w1sh.stardust.annotation.Provide;

import java.util.Arrays;

@Provide
public class ClassDependencyResolver implements DependencyResolver {

    @Override
    public boolean matches(Class<?> clazz, Environment environment) {
        DependsOnClass annotation = clazz.getAnnotation(DependsOnClass.class);
        return Arrays.stream(annotation.value()).allMatch(aClass -> environment.container().contains(aClass));
    }

    @Override
    public EvaluationPhase getEvaluationPhase() {
        return EvaluationPhase.BEFORE_REGISTRATION;
    }
}

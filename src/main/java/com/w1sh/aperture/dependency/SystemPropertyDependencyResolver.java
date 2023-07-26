package com.w1sh.aperture.dependency;

import com.w1sh.aperture.Environment;
import com.w1sh.aperture.annotation.Provide;

@Provide
public class SystemPropertyDependencyResolver implements DependencyResolver {

    @Override
    public boolean matches(Class<?> clazz, Environment environment) {
        DependsOnSystemProperty annotations = clazz.getAnnotation(DependsOnSystemProperty.class);
        for (int i = 0, valueLength = annotations.value().length; i < valueLength; i++) {
            String key = annotations.value()[i];
            var matches = System.getProperty(key).equalsIgnoreCase(annotations.expectedValue()[i]);
            if (!matches) return false;
        }
        return true;
    }

    @Override
    public EvaluationPhase getEvaluationPhase() {
        return EvaluationPhase.BEFORE_REGISTRATION;
    }
}

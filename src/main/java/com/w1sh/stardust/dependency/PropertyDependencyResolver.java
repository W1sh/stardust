package com.w1sh.stardust.dependency;

import com.w1sh.stardust.Environment;
import com.w1sh.stardust.configuration.PropertiesRegistry;

public class PropertyDependencyResolver implements DependencyResolver {

    @Override
    public boolean matches(Class<?> clazz, Environment environment) {
        PropertiesRegistry registry = environment.container().instance(PropertiesRegistry.class);
        DependsOnProperty annotations = clazz.getAnnotation(DependsOnProperty.class);
        for (int i = 0, valueLength = annotations.value().length; i < valueLength; i++) {
            String key = annotations.value()[i];
            var matches = registry.getProperty(key).equalsIgnoreCase(annotations.expectedValue()[i]);
            if (!matches) return false;
        }
        return true;
    }

    @Override
    public EvaluationPhase getEvaluationPhase() {
        return EvaluationPhase.BEFORE_REGISTRATION;
    }
}

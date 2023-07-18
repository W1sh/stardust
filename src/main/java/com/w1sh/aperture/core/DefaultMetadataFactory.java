package com.w1sh.aperture.core;

import com.w1sh.aperture.core.annotation.Primary;
import com.w1sh.aperture.core.annotation.Profile;
import com.w1sh.aperture.core.annotation.Provide;

import javax.annotation.Priority;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public class DefaultMetadataFactory implements AnnotationAwareMetadataFactory {

    @Override
    public Metadata create(Class<?> clazz) {
        return fromAnnotatedElement(clazz);
    }

    @Override
    public Metadata create(Method method) {
        return fromAnnotatedElement(method);
    }

    private static Metadata fromAnnotatedElement(AnnotatedElement annotatedElement) {
        Provide provideAnnotation = annotatedElement.getAnnotation(Provide.class);
        var name = (provideAnnotation != null && !provideAnnotation.value().isBlank()) ? provideAnnotation.value() : null;
        var scope = provideAnnotation != null ? provideAnnotation.scope() : Scope.SINGLETON;
        Priority priorityAnnotation = annotatedElement.getAnnotation(Priority.class);
        var priority = priorityAnnotation != null ? priorityAnnotation.value() : null;
        Profile profileAnnotation = annotatedElement.getAnnotation(Profile.class);
        var profiles = profileAnnotation != null ? profileAnnotation.value() : null;
        var primary = annotatedElement.getAnnotation(Primary.class) != null;

        return Metadata.builder()
                .name(name)
                .primary(primary)
                .scope(scope)
                .profiles(profiles)
                .priority(priority)
                .build();
    }

    @Override
    public Metadata merge(Metadata m1, Metadata m2) {
        return Metadata.builder()
                .name(mergeValue(m1.name(), m2.name()))
                .primary(mergeValue(m1.primary(), m2.primary()))
                .scope(mergeValue(m1.scope(), m2.scope()))
                .priority(mergeValue(m1.priority(), m2.priority()))
                .profiles(mergeValue(m1.profiles(), m2.profiles()))
                .conditionalOn(mergeValue(m1.requiredClasses(), m2.requiredClasses()))
                .conditionalOnMissing(mergeValue(m1.requiredMissingClasses(), m2.requiredMissingClasses()))
                .requiredSystemProperties(mergeValue(m1.requiredSystemProperties(), m2.requiredSystemProperties()))
                .build();
    }
}

package com.w1sh.aperture;

import com.w1sh.aperture.annotation.Primary;
import com.w1sh.aperture.annotation.Profile;
import com.w1sh.aperture.annotation.Provide;

import javax.annotation.Priority;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public class AnnotationAwareMetadataFactory implements MetadataFactory {

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
}

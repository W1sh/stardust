package com.w1sh.aperture.core;

import com.w1sh.aperture.core.annotation.Profile;
import com.w1sh.aperture.core.annotation.Provide;

import javax.annotation.Priority;

public class DefaultMetadataFactory implements AnnotationAwareMetadataFactory{

    @Override
    public Metadata create(Class<?> clazz) {
        Provide provideAnnotation = clazz.getAnnotation(Provide.class);
        var name = (provideAnnotation != null && !provideAnnotation.value().isBlank()) ? provideAnnotation.value() : null;
        var scope = provideAnnotation != null ? provideAnnotation.scope() : Scope.SINGLETON;
        Priority priorityAnnotation = clazz.getAnnotation(Priority.class);
        var priority = priorityAnnotation != null ? priorityAnnotation.value() : null;
        Profile profileAnnotation = clazz.getAnnotation(Profile.class);
        var profiles = profileAnnotation != null ? profileAnnotation.value() : null;

        return Metadata.builder()
                .name(name)
                .scope(scope)
                .profiles(profiles)
                .priority(priority)
                .build();
    }

    @Override
    public Metadata merge(Class<?> clazz, Metadata metadata) {
        Metadata classMetadata = create(clazz);
        return Metadata.builder()
                .name(mergeValue(classMetadata.name(), metadata.name()))
                .scope(mergeValue(classMetadata.scope(), metadata.scope()))
                .priority(mergeValue(classMetadata.priority(), metadata.priority()))
                .profiles(mergeValue(classMetadata.profiles(), metadata.profiles()))
                .conditionalOn(mergeValue(classMetadata.requiredClasses(), metadata.requiredClasses()))
                .conditionalOnMissing(mergeValue(classMetadata.requiredMissingClasses(), metadata.requiredMissingClasses()))
                .requiredSystemProperties(mergeValue(classMetadata.requiredSystemProperties(), metadata.requiredSystemProperties()))
                .build();
    }
}

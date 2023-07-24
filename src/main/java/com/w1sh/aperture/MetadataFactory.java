package com.w1sh.aperture;

import com.w1sh.aperture.exception.MetadataProcessingException;

import java.lang.reflect.Method;

public interface MetadataFactory {

    Metadata create(Method method);

    Metadata create(Class<?> clazz);

    default Metadata merge(Metadata m1, Metadata m2) {
        if (m1.equals(Metadata.empty())) return m2;
        if (m2.equals(Metadata.empty())) return m1;
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

    default <T> T mergeValue(T o1, T o2) throws MetadataProcessingException {
        if (o1 != null && o2 != null) {
            if (o1.equals(o2)) {
                return o1;
            } else throw new MetadataProcessingException("Failed to merge metadata as both have different values");
        }
        return o1 != null ? o1 : o2;
    }
}

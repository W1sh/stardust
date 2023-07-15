package com.w1sh.aperture.core;

import com.w1sh.aperture.core.exception.MetadataProcessingException;

public interface MetadataFactory {

    Metadata create(Class<?> clazz);

    Metadata merge(Class<?> clazz, Metadata metadata);

    default <T> T mergeValue(T o1, T o2) throws MetadataProcessingException {
        if (o1 != null && o2 != null) {
            if (o1.equals(o2)) {
                return o1;
            } else throw new MetadataProcessingException("Failed to merge metadata as both have different values");
        }
        return o1 != null ? o1 : o2;
    }
}

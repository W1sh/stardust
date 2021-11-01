package com.w1sh.wave.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

public final class Annotations {

    private Annotations() {
    }

    public static boolean isAnnotationPresent(AnnotatedElement annotatedElement, Class<? extends Annotation> annotation) {
        return Arrays.stream(annotatedElement.getAnnotations())
                .anyMatch(a -> a.annotationType().equals(annotation) || a.annotationType().isAnnotationPresent(annotation));
    }
}

package com.w1sh.stardust;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface Resolvable<S> {

    Class<S> getActualType();

    /**
     * Get this resolvable element annotation of a certain annotation type.
     *
     * @param <T>            the type of the annotation
     * @param annotationType the class of the annotation type
     * @return the annotation of the given annotation type, or a null value
     */
    <T extends Annotation> T getAnnotation(Class<T> annotationType);

    /**
     * Returns all the annotations present in this resolvable element of the provided type.
     *
     * @param <T>            the type of the annotation
     * @param annotationType the class of the annotation type
     * @return the annotations of the given annotation type, or an empty collection
     */
    <T extends Annotation> Set<T> getAnnotations(Class<T> annotationType);

    /**
     * Determine if this resolvable element contains the provided annotationType.
     *
     * @param annotationType the annotation type to check for
     * @return <tt>true</tt> if the program element has an annotation of the given annotation type, or <tt>false</tt> otherwise
     */
    boolean isAnnotationPresent(Class<? extends Annotation> annotationType);
}

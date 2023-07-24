package com.w1sh.aperture;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassDefinition<T> extends Definition<T> {

    private final Constructor<T> constructor;
    private final Set<Class<?>> initializationChain;

    public ClassDefinition(Class<T> clazz, Constructor<T> constructor, Metadata metadata) {
        super(clazz, metadata);
        this.constructor = constructor;
        this.initializationChain = new HashSet<>();
        this.initializationChain.add(clazz);
    }

    public static <T> ClassDefinition<T> withMetadata(Class<T> clazz, Constructor<T> constructor, Metadata metadata) {
        return new ClassDefinition<>(clazz, constructor, metadata);
    }

    public static <T> ClassDefinition<T> withoutMetadata(Class<T> clazz, Constructor<T> constructor) {
        return new ClassDefinition<>(clazz, constructor, Metadata.empty());
    }

    public String chainAsString() {
        return initializationChain.stream()
                .map(chainClazz -> chainClazz.getSimpleName() + " -> ")
                .collect(Collectors.joining("", "", getClazz().getSimpleName()));
    }

    public Constructor<T> getConstructor() {
        return constructor;
    }

    public Set<Class<?>> getChain() {
        return initializationChain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClassDefinition<?> that = (ClassDefinition<?>) o;
        return Objects.equals(constructor, that.constructor) && Objects.equals(initializationChain, that.initializationChain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), constructor, initializationChain);
    }
}

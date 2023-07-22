package com.w1sh.aperture;

import java.util.Objects;

public abstract class Definition<T> {

    private final Class<T> clazz;
    private final Metadata metadata;

    protected Definition(Class<T> clazz, Metadata metadata) {
        this.clazz = clazz;
        this.metadata = metadata;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Definition<?> that = (Definition<?>) o;
        return Objects.equals(clazz, that.clazz) && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, metadata);
    }
}

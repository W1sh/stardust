package com.w1sh.wave.core;

import java.util.Objects;

public class Key<T> {

    private final Class<T> type;
    private final String name;
    private final boolean primary;

    public Key(Class<T> type, String name, boolean primary) {
        this.type = type;
        this.name = name;
        this.primary = primary;
    }

    public static <T> Key<T> of(Class<T> type, String name) {
        return new Key<>(type, name, false);
    }

    public static <T> Key<T> of(Class<T> type, String name, boolean primary) {
        return new Key<>(type, name, primary);
    }

    public Class<T> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimary() {
        return primary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key<?> key = (Key<?>) o;
        return primary == key.primary && type.equals(key.type) && name.equals(key.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, primary);
    }

}

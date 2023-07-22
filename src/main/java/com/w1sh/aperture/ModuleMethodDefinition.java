package com.w1sh.aperture;

import java.util.Objects;
import java.util.function.Supplier;

public class ModuleMethodDefinition<T> extends Definition<T> {

    private final Supplier<T> supplier;

    public ModuleMethodDefinition(Class<T> clazz, Metadata metadata, Supplier<T> supplier) {
        super(clazz, metadata);
        this.supplier = supplier;
    }

    public static <T> ModuleMethodDefinition<T> withMetadata(Class<T> clazz, Metadata metadata, Supplier<T> supplier) {
        return new ModuleMethodDefinition<>(clazz, metadata, supplier);
    }

    public static <T> ModuleMethodDefinition<T> withoutMetadata(Class<T> clazz, Supplier<T> supplier) {
        return new ModuleMethodDefinition<>(clazz, Metadata.empty(), supplier);
    }

    public Supplier<T> getSupplier() {
        return supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ModuleMethodDefinition<?> that = (ModuleMethodDefinition<?>) o;
        return Objects.equals(supplier, that.supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), supplier);
    }
}

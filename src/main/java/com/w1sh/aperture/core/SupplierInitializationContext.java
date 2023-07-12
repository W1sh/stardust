package com.w1sh.aperture.core;

import com.w1sh.aperture.core.builder.Options;

import java.util.function.Supplier;

public class SupplierInitializationContext<T> extends InitializationContext<T> {

    private final Supplier<T> supplier;

    public SupplierInitializationContext(Class<T> clazz, Options options, Supplier<T> supplier) {
        super(clazz, options);
        this.supplier = supplier;
    }

    public Supplier<T> getSupplier() {
        return supplier;
    }
}

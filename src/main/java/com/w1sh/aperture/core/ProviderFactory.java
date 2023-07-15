package com.w1sh.aperture.core;

public interface ProviderFactory {

    <T> ObjectProvider<T> newProvider(Definition<T> definition);
}

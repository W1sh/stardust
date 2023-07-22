package com.w1sh.aperture;

public interface ProviderFactory {

    String DEFAULT_PROVIDER_FACTORY_NAME = "providerFactory";

    <T> ObjectProvider<T> newProvider(Definition<T> definition);
}

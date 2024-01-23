package com.w1sh.stardust;

import com.w1sh.stardust.example.service.MerchantService;
import com.w1sh.stardust.example.service.impl.DuplicateCalculatorServiceImpl;
import com.w1sh.stardust.example.service.impl.MerchantServiceImpl;
import com.w1sh.stardust.exception.ProviderRegistrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProviderStoreImplTest {

    private ProviderStoreImpl store;

    @BeforeEach
    void setUp() {
        store = new ProviderStoreImpl();
    }

    @Test
    void should_returnInstance_whenProviderOfClassIsRegistered() {
        store.register("duplicate", DuplicateCalculatorServiceImpl.class, new SingletonObjectProvider<>(new DuplicateCalculatorServiceImpl()));

        List<ObjectProvider<DuplicateCalculatorServiceImpl>> instance = store.get(DuplicateCalculatorServiceImpl.class);

        assertNotNull(instance);
        assertEquals(1, instance.size());
    }

    @Test
    void should_returnInstance_whenProviderIsRegisteredWithGivenName() {
        store.register("duplicate", DuplicateCalculatorServiceImpl.class, new SingletonObjectProvider<>(new DuplicateCalculatorServiceImpl()));

        ObjectProvider<DuplicateCalculatorServiceImpl> instance = store.get("duplicate");

        assertNotNull(instance);
    }

    @Test
    void should_returnAllClassesRegistered() {
        store.register("duplicate", DuplicateCalculatorServiceImpl.class, new SingletonObjectProvider<>(new DuplicateCalculatorServiceImpl()));
        store.register("merchant", MerchantService.class, new SingletonObjectProvider<>(new MerchantServiceImpl()));

        Set<Class<?>> classes = store.getAllClasses();

        assertNotNull(classes);
        assertEquals(2, classes.size());
        assertTrue(classes.contains(DuplicateCalculatorServiceImpl.class));
        assertTrue(classes.contains(MerchantService.class));
    }

    @Test
    void should_returnAllProvidersRegistered() {
        SingletonObjectProvider<DuplicateCalculatorServiceImpl> duplicateCalculatorServiceSingletonObjectProvider = new SingletonObjectProvider<>(new DuplicateCalculatorServiceImpl());
        SingletonObjectProvider<MerchantServiceImpl> merchantServiceSingletonObjectProvider = new SingletonObjectProvider<>(new MerchantServiceImpl());
        store.register("duplicate", DuplicateCalculatorServiceImpl.class, duplicateCalculatorServiceSingletonObjectProvider);
        store.register("merchant", MerchantServiceImpl.class, merchantServiceSingletonObjectProvider);

        List<ObjectProvider<?>> providers = store.getAll();

        assertNotNull(providers);
        assertEquals(2, providers.size());
        assertEquals(providers.get(0), duplicateCalculatorServiceSingletonObjectProvider);
        assertEquals(providers.get(1), merchantServiceSingletonObjectProvider);
    }

    @Test
    void should_returnCountOfAllProvidersRegistered() {
        store.register("duplicate", DuplicateCalculatorServiceImpl.class, new SingletonObjectProvider<>(new DuplicateCalculatorServiceImpl()));
        store.register("merchant", MerchantService.class, new SingletonObjectProvider<>(new MerchantServiceImpl()));

        Integer providers = store.count();

        assertEquals(2, providers);
    }

    @Test
    void should_throwProviderRegistrationException_whenRegisteringWithExistingNameAndOverridingNotAllowed() {
        store.setAllowOverride(false);
        store.register("duplicate", DuplicateCalculatorServiceImpl.class, new SingletonObjectProvider<>(new DuplicateCalculatorServiceImpl()));

        assertThrows(ProviderRegistrationException.class, () -> store.register("duplicate", MerchantService.class, new SingletonObjectProvider<>(new MerchantServiceImpl())));
    }
}
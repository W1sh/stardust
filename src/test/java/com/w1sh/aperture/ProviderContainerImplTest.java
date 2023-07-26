package com.w1sh.aperture;

import com.w1sh.aperture.annotation.Module;
import com.w1sh.aperture.annotation.Primary;
import com.w1sh.aperture.annotation.Profile;
import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.example.controller.CalculatorController;
import com.w1sh.aperture.example.controller.impl.BindingDependantControllerImpl;
import com.w1sh.aperture.example.controller.impl.CalculatorControllerImpl;
import com.w1sh.aperture.example.controller.impl.EmptyCalculatorControllerImpl;
import com.w1sh.aperture.example.controller.impl.PrimaryControllerImpl;
import com.w1sh.aperture.example.service.CalculatorService;
import com.w1sh.aperture.example.service.impl.CalculatorServiceImpl;
import com.w1sh.aperture.example.service.impl.DuplicateCalculatorServiceImpl;
import com.w1sh.aperture.exception.ProviderCandidatesException;
import com.w1sh.aperture.exception.ProviderRegistrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProviderContainerImplTest {

    private ProviderContainerImpl registry;

    @BeforeEach
    void setUp() {
        registry = new ProviderContainerImpl();
    }

    @Test
    void should_returnInstance_whenProviderOfClassIsRegistered() {
        registry.register(DuplicateCalculatorServiceImpl.class);

        DuplicateCalculatorServiceImpl instance = registry.instance(DuplicateCalculatorServiceImpl.class);

        assertNotNull(instance);
    }

    @Test
    void should_returnInstance_whenProviderWithNameIsRegistered() {
        registry.register(DuplicateCalculatorServiceImpl.class);

        DuplicateCalculatorServiceImpl instance = registry.instance("duplicateCalculatorServiceImpl");

        assertNotNull(instance);
    }

    @Test
    void should_returnNull_whenProviderWithNameIsNotRegistered() {
        registry.register(DuplicateCalculatorServiceImpl.class);

        Object instance = registry.instance("calculator");

        assertNull(instance);
    }

    @Test
    void should_returnNull_whenProviderOfClassIsNotRegistered() {
        registry.register(DuplicateCalculatorServiceImpl.class);

        Object instance = registry.instance(CalculatorController.class);

        assertNull(instance);
    }

    @Test
    void should_returnInstance_whenProviderOfSubclassIsRegistered() {
        registry.register(DuplicateCalculatorServiceImpl.class);

        CalculatorService instance = registry.instance(CalculatorService.class);

        assertNotNull(instance);
    }

    @Test
    void should_returnAllClassesRegisteredAnnotated_whenGivenAnnotation() {
        registry.register(CalculatorControllerImpl.class);
        registry.register(EmptyCalculatorControllerImpl.class);

        List<Class<?>> classes = registry.getAllAnnotatedWith(Primary.class);

        assertNotNull(classes);
        assertEquals(1, classes.size());
        assertEquals(CalculatorControllerImpl.class, classes.get(0));
    }

    @Test
    void should_throwRegistrationException_whenTryingToRegisterClassTwiceButOverrideNotAllowed() {
        registry.setOverrideStrategy(ProviderContainer.OverrideStrategy.NOT_ALLOWED);
        registry.register(CalculatorControllerImpl.class);

        assertThrows(ProviderRegistrationException.class, () -> registry.register(CalculatorControllerImpl.class));
    }

    @Test
    void should_returnPrimaryInstanceOrProvider_whenMultipleProvidersAreRegisteredAndOneIsPrimary() {
        registry.register(CalculatorControllerImpl.class);
        registry.register(DuplicateCalculatorServiceImpl.class);

        CalculatorController instance = registry.primaryInstance(CalculatorController.class);
        ObjectProvider<CalculatorController> provider = registry.primaryProvider(CalculatorController.class);

        assertNotNull(provider);
        assertEquals(CalculatorControllerImpl.class, provider.singletonInstance().getClass());
        assertNotNull(instance);
        assertEquals(CalculatorControllerImpl.class, instance.getClass());
    }

    @Test
    void should_throwProviderCandidatesException_whenMultiplePrimaryProvidersAreRegistered() {
        registry.register(CalculatorControllerImpl.class);
        registry.register(PrimaryControllerImpl.class);

        assertThrows(ProviderCandidatesException.class, () -> registry.primaryInstance(CalculatorController.class));
        assertThrows(ProviderCandidatesException.class, () -> registry.primaryProvider(CalculatorController.class));
    }

    @Test
    void should_throwProviderCandidatesException_whenNoPrimaryProvidersAreRegistered() {
        registry.register(BindingDependantControllerImpl.class);
        registry.register(EmptyCalculatorControllerImpl.class);

        assertThrows(ProviderCandidatesException.class, () -> registry.primaryInstance(CalculatorController.class));
        assertThrows(ProviderCandidatesException.class, () -> registry.primaryProvider(CalculatorController.class));
    }

    @Test
    void should_returnAllInstances_whenMultipleProvidersAreRegisteredForClass() {
        registry.register(CalculatorControllerImpl.class);
        registry.register(EmptyCalculatorControllerImpl.class);

        List<CalculatorController> instances = registry.instances(CalculatorController.class);
        List<ObjectProvider<CalculatorController>> providers = registry.providers(CalculatorController.class);

        assertNotNull(instances);
        assertNotNull(providers);
        assertEquals(2, instances.size());
        assertEquals(2, providers.size());
    }

    @Test
    void should_returnTrue_whenThereIsAtLeastOneProviderRegisteredForClass() {
        registry.register(CalculatorControllerImpl.class);
        registry.register(EmptyCalculatorControllerImpl.class);

        boolean containsClass = registry.contains(CalculatorController.class);
        boolean containsName = registry.contains("calculatorControllerImpl");

        assertTrue(containsClass);
        assertTrue(containsName);
    }

    @Test
    void should_returnInstance_whenRegisteredViaModule() {
        registry.register(AnnotatedClass.class);

        CalculatorController controller = registry.instance(CalculatorController.class);

        assertNotNull(controller);
    }

    @Test
    void should_throwNullPointerException_whenTryingToRetrieveInstanceWithNullClass() {
        assertThrows(NullPointerException.class, () -> registry.instance((Class<Object>) null));
    }

    @Test
    void should_throwNullPointerException_whenTryingToRetrieveInstanceWithNullName() {
        assertThrows(NullPointerException.class, () -> registry.instance((String) null));
    }

    @Test
    void should_throwNullPointerException_whenTryingToRetrieveProviderWithNullClass() {
        assertThrows(NullPointerException.class, () -> registry.instance((Class<Object>) null));
    }

    @Test
    void should_throwNullPointerException_whenTryingToRetrieveProviderWithNullName() {
        assertThrows(NullPointerException.class, () -> registry.instance((String) null));
    }

    @Test
    void should_throwNullPointerException_whenTryingToRetrievePrimaryInstanceWithNullClass() {
        assertThrows(NullPointerException.class, () -> registry.primaryInstance(null));
    }

    @Test
    void should_throwNullPointerException_whenTryingToRetrievePrimaryProviderWithNullClass() {
        assertThrows(NullPointerException.class, () -> registry.primaryInstance(null));
    }

    @Test
    void should_throwNullPointerException_whenTryingToSetNullOverrideStrategy() {
        assertThrows(NullPointerException.class, () -> registry.setOverrideStrategy(null));
    }

    @Test
    void should_throwNullPointerException_whenTryingToRetrieveProvidersWithNullClass() {
        assertThrows(NullPointerException.class, () -> registry.providers(null));
    }

    @Test
    void should_throwNullPointerException_whenTryingToRetrieveInstancesWithNullClass() {
        assertThrows(NullPointerException.class, () -> registry.instances(null));
    }

    @Test
    void should_throwNullPointerException_whenTryingToRetrieveListOfClassesAnnotatedWithNullAnnotation() {
        assertThrows(NullPointerException.class, () -> registry.getAllAnnotatedWith(null));
    }

    @Test
    void should_returnFalse_whenSearchingContainerForNullClass() {
        boolean contains = registry.contains((Class<Object>) null);

        assertFalse(contains);
    }

    @Test
    void should_returnFalse_whenSearchingContainerForNullName() {
        boolean contains = registry.contains((String) null);

        assertFalse(contains);
    }

    @Module
    private static class AnnotatedClass {

        public AnnotatedClass() {}

        @Primary
        @Profile("module")
        @Provide
        public CalculatorController controller() {
            return new CalculatorControllerImpl();
        }

        public CalculatorService service() {
            return new CalculatorServiceImpl();
        }
    }
}
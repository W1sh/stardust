package com.w1sh.stardust;

import com.w1sh.stardust.annotation.Module;
import com.w1sh.stardust.annotation.Primary;
import com.w1sh.stardust.annotation.Profile;
import com.w1sh.stardust.annotation.Provide;
import com.w1sh.stardust.example.controller.CalculatorController;
import com.w1sh.stardust.example.controller.impl.*;
import com.w1sh.stardust.example.service.CalculatorService;
import com.w1sh.stardust.example.service.MerchantService;
import com.w1sh.stardust.example.service.impl.CalculatorServiceImpl;
import com.w1sh.stardust.example.service.impl.DuplicateCalculatorServiceImpl;
import com.w1sh.stardust.example.service.impl.MerchantServiceImpl;
import com.w1sh.stardust.exception.ProviderCandidatesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AbstractProviderContainerTest {

    private AbstractProviderContainer registry;

    @BeforeEach
    void setUp() {
        registry = AbstractProviderContainer.base();
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

    @Test
    void should_returnInstance_whenRegisteredViaModuleAndDependsOnInstanceFromSameModule() {
        registry.register(ModuleTestingClass.class);

        MerchantService service = registry.instance(MerchantService.class);
        RequiredDependantControllerImpl controller = (RequiredDependantControllerImpl) registry.instance(CalculatorController.class);

        assertNotNull(service);
        assertNotNull(controller);
        assertNotNull(controller.getMerchantService());
    }

    @Test
    void should_returnListInstance_whenRegisteredListIsProviderViaModule() {
        registry.register(ModuleTestingClass.class);

        List<?> listInstanceFromClass = registry.instance(List.class);
        List<?> listInstanceFromName = registry.instance("services");

        assertNotNull(listInstanceFromClass);
        assertNotNull(listInstanceFromName);
        assertEquals(listInstanceFromClass, listInstanceFromName);
    }

    @Test
    void should_returnMapInstance_whenRegisteredMapIsProviderViaModule() {
        registry.register(ModuleTestingClass.class);

        Map<?, ?> mapInstanceFromClass = registry.instance(Map.class);
        Map<?, ?> mapInstanceFromName = registry.instance("mappedServices");

        assertNotNull(mapInstanceFromClass);
        assertNotNull(mapInstanceFromName);
        assertEquals(mapInstanceFromClass, mapInstanceFromName);
    }

    @Test
    void should_containInterceptor_whenInterceptorIsAdded() {
        assertEquals(0, registry.getAllInterceptors().size());

        registry.addInterceptor(new JakartaPostConstructInterceptor());

        assertEquals(1, registry.getAllInterceptors().size());
    }

    @Test
    void should_containNoInterceptor_whenInterceptorIsRemoved() {
        JakartaPostConstructInterceptor interceptor = new JakartaPostConstructInterceptor();
        registry.addInterceptor(interceptor);
        assertEquals(1, registry.getAllInterceptors().size());

        registry.removeInterceptor(interceptor);

        assertEquals(0, registry.getAllInterceptors().size());
    }

    @Test
    void should_containNoInterceptor_whenAllInterceptorsAreRemoved() {
        registry.addInterceptor(new JakartaPostConstructInterceptor());
        assertEquals(1, registry.getAllInterceptors().size());

        registry.removeAllInterceptors();

        assertEquals(0, registry.getAllInterceptors().size());
    }

    @Test
    void should_returnAllInterceptorsOfGivenType() {
        registry.addInterceptor(new JakartaPostConstructInterceptor());
        assertEquals(1, registry.getAllInterceptors().size());

        List<InvocationInterceptor> allInterceptorsOfType = registry.getAllInterceptorsOfType(InvocationInterceptor.InvocationType.POST_CONSTRUCT);

        assertNotNull(allInterceptorsOfType);
        assertEquals(1, allInterceptorsOfType.size());
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

    @Module
    private static class ModuleTestingClass {

        public ModuleTestingClass() {}

        @Provide
        public MerchantService service() {
            return new MerchantServiceImpl();
        }

        @Provide
        public CalculatorController controller(MerchantService merchantService) {
            return new RequiredDependantControllerImpl(merchantService);
        }

        @Provide
        public List<String> services() {
            return List.of("service1", "service2", "service3");
        }

        @Provide
        public Map<String, Integer> mappedServices() {
            return Map.of("service1", 1, "service2", 2);
        }
    }
}
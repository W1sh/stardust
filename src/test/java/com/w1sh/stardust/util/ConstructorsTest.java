package com.w1sh.stardust.util;

import com.w1sh.stardust.annotation.Inject;
import com.w1sh.stardust.example.controller.impl.EmptyCalculatorControllerImpl;
import com.w1sh.stardust.example.service.MerchantService;
import com.w1sh.stardust.example.service.impl.CalculatorServiceImpl;
import com.w1sh.stardust.example.service.impl.RecordService;
import com.w1sh.stardust.exception.ProviderRegistrationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstructorsTest {

    @Test
    void should_returnNoArgConstructor_whenNoConstructorIsAvailable() {
        final var constructor = Constructors.getInjectConstructor(EmptyCalculatorControllerImpl.class);

        assertNotNull(constructor);
        assertEquals(0, constructor.getParameterCount());
    }

    @Test
    void should_returnInjectedAnnotatedConstructor_whenOneIsPresent() {
        final var constructor = Constructors.getInjectConstructor(CalculatorServiceImpl.class);

        assertNotNull(constructor);
        assertTrue(constructor.isAnnotationPresent(Inject.class));
    }

    @Test
    void should_returnNoArgConstructor_whenNoConstructorIsAvailableAndIsRecord() {
        final var constructor = Constructors.getInjectConstructor(RecordService.class);

        assertNotNull(constructor);
        assertEquals(0, constructor.getParameterCount());
    }

    @Test
    void should_returnNoArgConstructor_whensNoConstructorIsAvailableAndIsInnerRecord() {
        final var constructor = Constructors.getInjectConstructor(RecordService.InnerRecord.class);

        assertNotNull(constructor);
        assertEquals(0, constructor.getParameterCount());
    }

    @Test
    void should_throwProviderRegistrationException_whenClassHasMultipleEligibleConstructors() {
        assertThrows(ProviderRegistrationException.class,
                () -> Constructors.getInjectConstructor(RecordService.InnerRecord2.class));
    }

    @Test
    void should_throwProviderRegistrationException_whenClassHasMultipleInjectAnnotatedConstructors() {
        assertThrows(ProviderRegistrationException.class,
                () -> Constructors.getInjectConstructor(RecordService.InnerRecord3.class));
    }

    @Test
    void should_throwProviderRegistrationException_whenRetrievingConstructorFromNonDeclarableClass() {
        assertThrows(ProviderRegistrationException.class,
                () -> Constructors.getInjectConstructor(MerchantService.class));
    }
}
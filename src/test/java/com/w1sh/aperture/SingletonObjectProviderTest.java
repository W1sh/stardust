package com.w1sh.aperture;

import com.w1sh.aperture.example.service.impl.TestClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SingletonObjectProviderTest {

    private final ObjectProvider<TestClass> provider = new SingletonObjectProvider<>(new TestClass());

    @Test
    void should_AlwaysReturnSameInstance_WhenInvokingSingletonInstance() {
        final TestClass testClass = provider.singletonInstance();
        final TestClass secondTestClass = provider.singletonInstance();

        assertEquals(testClass, secondTestClass);
        assertEquals(1, provider.instances().size());
    }

    @Test
    void should_ThrowUnsupportedOperationException_WhenInvokingNewInstance() {
        assertThrows(UnsupportedOperationException.class, provider::newInstance);
    }

}
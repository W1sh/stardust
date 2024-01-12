package com.w1sh.stardust;

import com.w1sh.stardust.example.service.impl.TestClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PrototypeObjectProviderTest {

    private final ObjectProvider<TestClass> provider = new PrototypeObjectProvider<>(TestClass::new);

    @Test
    void should_AlwaysReturnSameInstance_WhenInvokingSingletonInstance(){
        final TestClass testClass = provider.singletonInstance();
        final TestClass secondTestClass = provider.singletonInstance();

        assertEquals(testClass, secondTestClass);
        assertEquals(1, provider.instances().size());
    }

    @Test
    void should_AlwaysReturnDifferentInstance_WhenInvokingNewInstance(){
        final TestClass testClass = provider.newInstance();
        final TestClass secondTestClass = provider.newInstance();

        assertNotEquals(testClass, secondTestClass);
    }
}
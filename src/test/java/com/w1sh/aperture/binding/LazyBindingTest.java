package com.w1sh.aperture.binding;

import com.w1sh.aperture.ObjectProvider;
import com.w1sh.aperture.PrototypeObjectProvider;
import com.w1sh.aperture.example.service.impl.TestClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LazyBindingTest {

    private final ObjectProvider<TestClass> provider = new PrototypeObjectProvider<>(TestClass::new);

    @Test
    void should_AlwaysReturnSameInstance_WhenInvokingGet() {
        Lazy<TestClass> testClassProvider = new LazyBinding<>(provider) {
        };

        final TestClass testClassFirstCall = testClassProvider.get();
        final TestClass testClassSecondCall = testClassProvider.get();
        assertEquals(testClassFirstCall, testClassSecondCall);
    }
}
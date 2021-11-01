package com.w1sh.wave.core.binding;

import com.w1sh.wave.core.ObjectProvider;
import com.w1sh.wave.core.SimpleObjectProvider;
import com.w1sh.wave.example.service.impl.TestClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ProviderBindingTest {

    private final ObjectProvider<TestClass> provider = new SimpleObjectProvider<>(TestClass::new);

    @Test
    void should_AlwaysReturnNewInstance_WhenInvokingGet() {
        Provider<TestClass> testClassProvider = new ProviderBinding<>(provider);

        final TestClass testClassFirstCall = testClassProvider.get();
        final TestClass testClassSecondCall = testClassProvider.get();
        assertNotEquals(testClassFirstCall, testClassSecondCall);
    }
}
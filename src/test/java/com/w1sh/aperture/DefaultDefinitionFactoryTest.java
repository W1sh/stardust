package com.w1sh.aperture;

import com.w1sh.aperture.annotation.Primary;
import com.w1sh.aperture.annotation.Profile;
import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.exception.ModuleProcessingException;
import com.w1sh.aperture.example.controller.CalculatorController;
import com.w1sh.aperture.example.controller.impl.CalculatorControllerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Priority;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DefaultDefinitionFactoryTest {

    private ProviderContainer registry;
    private AnnotationAwareDefinitionFactory definitionFactory;

    @BeforeEach
    void setUp() {
        registry = spy(new ProviderContainerImpl());
        definitionFactory = new DefaultDefinitionFactory(registry);
    }

    @Test
    void should_throwException_whenProvidedClassDoesNotImplementModuleClass() {
        assertThrows(ModuleProcessingException.class, () -> definitionFactory.fromModuleClass(DefaultDefinitionFactoryTest.class));
    }

    @Test
    void should_returnDefinition_whenProvidedModuleClassContainsAnnotatedProvideMethod() {
        registry.register(new SingletonObjectProvider<>(new AnnotatedClass()), AnnotatedClass.class, "annotated");
        List<Definition<?>> definitions = definitionFactory.fromModuleClass(AnnotatedClass.class);

        assertNotNull(definitions);
        assertEquals(1, definitions.size());
        assertEquals(CalculatorController.class, definitions.get(0).getClazz());
        verify(registry, times(1)).instance(AnnotatedClass.class);
    }

    @Test
    void should_returnDefinition_whenProvidedClassContainsAnnotations() {
        Definition<AnnotatedProvideClass> definition = definitionFactory.fromClass(AnnotatedProvideClass.class);

        assertNotNull(definition);
        assertEquals(AnnotatedProvideClass.class, definition.getClazz());
    }

    @Primary
    @Provide(value = "test", scope = Scope.PROTOTYPE)
    @Profile("test")
    @Priority(5)
    private static class AnnotatedProvideClass {

        public AnnotatedProvideClass() {}
    }

    private static class AnnotatedClass implements Module {

        public AnnotatedClass() {}

        @Provide
        public CalculatorController controller() {
            return new CalculatorControllerImpl();
        }
    }
}
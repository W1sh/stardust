package com.w1sh.aperture;

import com.w1sh.aperture.annotation.Primary;
import com.w1sh.aperture.annotation.Profile;
import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.exception.MetadataProcessingException;
import com.w1sh.aperture.example.controller.CalculatorController;
import com.w1sh.aperture.example.controller.impl.CalculatorControllerImpl;
import com.w1sh.aperture.example.service.CalculatorService;
import com.w1sh.aperture.example.service.impl.CalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import javax.annotation.Priority;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnnotationAwareMetadataFactoryTest {

    private final AnnotationAwareMetadataFactory metadataFactory = spy(new AnnotationAwareMetadataFactory());

    @Test
    void should_returnMetadata_whenProvidedClassHasMetadataAnnotations() {
        Metadata metadata = metadataFactory.create(AnnotatedClass.class);

        assertNotNull(metadata);
        assertEquals("ann", metadata.name());
        assertEquals(1, metadata.priority());
        assertEquals(Scope.PROTOTYPE, metadata.scope());
        assertEquals(1, metadata.profiles().length);
        assertEquals("test", metadata.profiles()[0]);
        assertEquals(true, metadata.primary());
    }

    @Test
    void should_returnEmptySingletonMetadata_whenProvidedClassHasNoMetadataAnnotations() {
        Metadata metadata = metadataFactory.create(AnnotationAwareMetadataFactoryTest.class);

        assertNotNull(metadata);
        assertNull(metadata.name());
        assertNull(metadata.priority());
        assertEquals(Scope.SINGLETON, metadata.scope());
        assertNull(metadata.profiles());
        assertFalse(metadata.primary());
    }

    @Test
    void should_returnMergedMetadata_whenProvidedClassHasMetadataAnnotations() {
        Metadata metadata = metadataFactory.create(AnnotatedClass.class);
        Metadata mergedMetadata = metadata.merge(metadata);

        assertNotNull(mergedMetadata);
        assertEquals("ann", mergedMetadata.name());
        assertEquals(1, mergedMetadata.priority());
        assertEquals(Scope.PROTOTYPE, mergedMetadata.scope());
        assertEquals(1, mergedMetadata.profiles().length);
        assertEquals("test", mergedMetadata.profiles()[0]);
        assertEquals(true, mergedMetadata.primary());
    }

    @Test
    void should_throwException_whenCreatedMetadataHasDifferentValuesFromProvidedMetadata() {
        Metadata classMetadata = metadataFactory.create(AnnotatedClass.class);
        Metadata metadata = Metadata.builder().scope(Scope.SINGLETON).build();

        assertThrows(MetadataProcessingException.class, () -> classMetadata.merge(metadata));
    }

    @Test
    void should_returnMetadata_whenProvidedMethodHasMetadataAnnotations() throws NoSuchMethodException {
        Metadata metadata = metadataFactory.create(AnnotatedClass.class.getMethod("controller"));

        assertNotNull(metadata);
        assertEquals(Scope.SINGLETON, metadata.scope());
        assertEquals(1, metadata.profiles().length);
        assertEquals("method_test", metadata.profiles()[0]);
        assertEquals(true, metadata.primary());
    }

    @Test
    void should_returnEmptySingletonMetadata_whenProvidedMethodHasNoMetadataAnnotations() throws NoSuchMethodException {
        Metadata metadata = metadataFactory.create(AnnotatedClass.class.getMethod("service"));

        assertNotNull(metadata);
        assertNull(metadata.name());
        assertNull(metadata.priority());
        assertEquals(Scope.SINGLETON, metadata.scope());
        assertNull(metadata.profiles());
        assertFalse(metadata.primary());
    }

    @Primary
    @Provide(value = "ann", scope = Scope.PROTOTYPE)
    @Profile("test")
    @Priority(1)
    private static class AnnotatedClass implements Module {

        @Primary
        @Profile("method_test")
        @Provide
        public CalculatorController controller() {
            return new CalculatorControllerImpl();
        }

        public CalculatorService service() {
            return new CalculatorServiceImpl();
        }
    }
}
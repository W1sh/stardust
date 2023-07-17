package com.w1sh.aperture.core;

import com.w1sh.aperture.core.annotation.Primary;
import com.w1sh.aperture.core.annotation.Profile;
import com.w1sh.aperture.core.annotation.Provide;
import com.w1sh.aperture.core.exception.MetadataProcessingException;
import org.junit.jupiter.api.Test;

import javax.annotation.Priority;

import static org.junit.jupiter.api.Assertions.*;

class DefaultMetadataFactoryTest {

    private final DefaultMetadataFactory metadataFactory = new DefaultMetadataFactory();

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
        Metadata metadata = metadataFactory.create(DefaultMetadataFactoryTest.class);

        assertNotNull(metadata);
        assertNull(metadata.name());
        assertNull(metadata.priority());
        assertEquals(Scope.SINGLETON, metadata.scope());
        assertNull(metadata.profiles());
    }

    @Test
    void should_returnMergedMetadata_whenProvidedClassHasMetadataAnnotations() {
        Metadata metadata = metadataFactory.merge(AnnotatedClass.class, Metadata.builder().scope(Scope.PROTOTYPE).build());

        assertNotNull(metadata);
        assertEquals("ann", metadata.name());
        assertEquals(1, metadata.priority());
        assertEquals(Scope.PROTOTYPE, metadata.scope());
        assertEquals(1, metadata.profiles().length);
        assertEquals("test", metadata.profiles()[0]);
        assertEquals(true, metadata.primary());
    }

    @Test
    void should_throwException_whenCreatedMetadataHasDifferentValuesFromProvidedMetadata() {
        Metadata metadata = Metadata.builder().scope(Scope.SINGLETON).build();
        assertThrows(MetadataProcessingException.class, () ->
                metadataFactory.merge(AnnotatedClass.class, metadata));
    }

    @Primary
    @Provide(value = "ann", scope = Scope.PROTOTYPE)
    @Profile("test")
    @Priority(1)
    private static class AnnotatedClass {}
}
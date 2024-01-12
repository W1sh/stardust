package com.w1sh.stardust;

import com.w1sh.stardust.InvocationInterceptor.InvocationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SetValueEnumMapTest {

    private SetValueEnumMap<InvocationType, InvocationInterceptor> map;

    @BeforeEach
    void setUp() {
        map = new SetValueEnumMap<>(InvocationType.class);
    }

    @Test
    void should_returnSetWithValues_whenKeyHasValues() {
        map.put(InvocationType.POST_CONSTRUCT, new JakartaPostConstructInterceptor());
        Set<InvocationInterceptor> set = map.get(InvocationType.POST_CONSTRUCT);

        assertNotNull(set);
        assertEquals(1, set.size());
    }

    @Test
    void should_returnEmptySet_whenKeyHasNoValues() {
        Set<InvocationInterceptor> set = map.get(InvocationType.PRE_DESTROY);

        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    void should_removeValueFromBackingSet_whenKeyHasValuesAndValueIsPresent() {
        JakartaPostConstructInterceptor jakartaPostConstructInterceptor = new JakartaPostConstructInterceptor();
        map.put(InvocationType.POST_CONSTRUCT, jakartaPostConstructInterceptor);
        Set<InvocationInterceptor> beforeRemove = Set.copyOf(map.get(InvocationType.POST_CONSTRUCT));
        map.remove(InvocationType.POST_CONSTRUCT, jakartaPostConstructInterceptor);
        Set<InvocationInterceptor> afterRemove = map.get(InvocationType.POST_CONSTRUCT);

        assertNotNull(beforeRemove);
        assertEquals(1, beforeRemove.size());
        assertNotNull(afterRemove);
        assertEquals(0, afterRemove.size());
    }
}
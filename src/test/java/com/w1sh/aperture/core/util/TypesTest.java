package com.w1sh.aperture.core.util;

import com.w1sh.aperture.core.condition.ActiveProfileCondition;
import com.w1sh.aperture.core.condition.ActiveProfileConditionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TypesTest {

    @Test
    void should_returnInterfaceActualTypeArgument_whenClassContainsInterfaceAndItIsGeneric() {
        Class<?> interfaceActualTypeArgument = Types.getInterfaceActualTypeArgument(ActiveProfileConditionFactory.class, 0);

        assertEquals(ActiveProfileCondition.class, interfaceActualTypeArgument);
    }
}
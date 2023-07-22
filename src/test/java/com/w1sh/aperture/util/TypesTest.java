package com.w1sh.aperture.util;

import com.w1sh.aperture.condition.ActiveProfileCondition;
import com.w1sh.aperture.condition.ActiveProfileConditionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TypesTest {

    @Test
    void should_returnInterfaceActualTypeArgument_whenClassContainsInterfaceAndItIsGeneric() {
        Class<?> interfaceActualTypeArgument = Types.getInterfaceActualTypeArgument(ActiveProfileConditionFactory.class, 0);

        assertEquals(ActiveProfileCondition.class, interfaceActualTypeArgument);
    }
}
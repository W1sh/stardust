package com.w1sh.stardust.example;

import com.w1sh.stardust.annotation.Inject;
import com.w1sh.stardust.annotation.Property;

public class PropertyDependantService {

    @Inject
    public PropertyDependantService(@Property("test.value") String value,
                                    @Property(value = "test.value-array", isArray = true) String[] arrayValue,
                                    @Property(value = "test.value") Integer intValue) {}
}

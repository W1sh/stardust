package com.w1sh.stardust.example;

import com.w1sh.stardust.annotation.Inject;
import com.w1sh.stardust.annotation.Property;

public class PropertyDependantService {

    @Inject
    public PropertyDependantService(@Property("test.value") String value,
                                    @Property("test.value-array") String[] arrayValue,
                                    @Property("test.value") Integer intValue,
                                    @Property("test.value") Exception e,
                                    @Property("test.value") Integer[] intValues) {}
}

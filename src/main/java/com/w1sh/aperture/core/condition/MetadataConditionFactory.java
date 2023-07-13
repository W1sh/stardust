package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.builder.Options;

public interface MetadataConditionFactory<T extends Condition> {

    T create(Options options);
}

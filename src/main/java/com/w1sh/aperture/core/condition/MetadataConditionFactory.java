package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.Metadata;

public interface MetadataConditionFactory<T extends Condition> {

    T create(Metadata metadata);
}

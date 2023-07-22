package com.w1sh.aperture.condition;

import com.w1sh.aperture.Metadata;

public interface MetadataConditionFactory<T extends Condition> {

    T create(Metadata metadata);
}

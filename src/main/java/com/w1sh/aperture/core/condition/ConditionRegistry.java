package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.Metadata;

import java.util.List;

public interface ConditionRegistry {

    List<? extends Condition> create(Metadata metadata);

    void addFactory(MetadataConditionFactory<?> factory);

    void removeFactory(MetadataConditionFactory<?> factory);

    void removaAllFactories();

}

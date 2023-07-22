package com.w1sh.aperture.condition;

import com.w1sh.aperture.Metadata;

import java.util.List;

public interface ConditionRegistry {

    List<? extends Condition> create(Metadata metadata);

    void addFactory(MetadataConditionFactory<?> factory);

    void removeFactory(MetadataConditionFactory<?> factory);

    void removaAllFactories();

}

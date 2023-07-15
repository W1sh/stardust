package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.Metadata;

public class SystemPropertyConditionFactory implements MetadataConditionFactory<SystemPropertyCondition> {
    @Override
    public SystemPropertyCondition create(Metadata metadata) {
        if (metadata.requiredSystemProperties() != null && metadata.requiredSystemProperties().size() > 0) {
            return new SystemPropertyCondition(metadata.requiredSystemProperties());
        } else return null;
    }
}

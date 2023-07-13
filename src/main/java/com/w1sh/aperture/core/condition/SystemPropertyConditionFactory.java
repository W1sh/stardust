package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.builder.Options;

public class SystemPropertyConditionFactory implements MetadataConditionFactory<SystemPropertyCondition> {
    @Override
    public SystemPropertyCondition create(Options options) {
        if (options.requiredSystemProperties().size() > 0) {
            return new SystemPropertyCondition(options.requiredSystemProperties());
        } else return null;
    }
}

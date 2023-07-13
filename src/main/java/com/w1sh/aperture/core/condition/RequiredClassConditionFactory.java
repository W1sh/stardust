package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.builder.Options;

import java.util.Arrays;

public class RequiredClassConditionFactory implements MetadataConditionFactory<RequiresClassCondition> {
    @Override
    public RequiresClassCondition create(Options options) {
        if (options.requiredClasses().length > 0) {
            return new RequiresClassCondition(Arrays.asList(options.requiredClasses()));
        } else return null;
    }
}

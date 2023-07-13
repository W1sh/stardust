package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.builder.Options;

import java.util.Arrays;

public class RequiredMissingClassConditionFactory implements MetadataConditionFactory<RequiresMissingClassCondition> {
    @Override
    public RequiresMissingClassCondition create(Options options) {
        if (options.requiredMissingClasses().length > 0) {
            return new RequiresMissingClassCondition(Arrays.asList(options.requiredMissingClasses()));
        } else return null;
    }
}

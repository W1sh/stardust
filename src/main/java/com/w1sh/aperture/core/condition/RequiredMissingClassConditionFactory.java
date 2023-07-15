package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.Metadata;

import java.util.Arrays;

public class RequiredMissingClassConditionFactory implements MetadataConditionFactory<RequiresMissingClassCondition> {
    @Override
    public RequiresMissingClassCondition create(Metadata metadata) {
        if (metadata.requiredMissingClasses() != null && metadata.requiredMissingClasses().length > 0) {
            return new RequiresMissingClassCondition(Arrays.asList(metadata.requiredMissingClasses()));
        } else return null;
    }
}

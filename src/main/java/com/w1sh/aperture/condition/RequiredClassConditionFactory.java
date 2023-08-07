package com.w1sh.aperture.condition;

import com.w1sh.aperture.Metadata;

import java.util.Arrays;

public class RequiredClassConditionFactory implements MetadataConditionFactory<RequiresClassCondition> {
    @Override
    public RequiresClassCondition create(Metadata metadata) {
        if (metadata.requiredClasses() != null && metadata.requiredClasses().length > 0) {
            return new RequiresClassCondition(Arrays.asList(metadata.requiredClasses()));
        } else return null;
    }
}
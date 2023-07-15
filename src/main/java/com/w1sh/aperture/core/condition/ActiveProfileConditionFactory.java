package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.Metadata;

import java.util.Arrays;

public class ActiveProfileConditionFactory implements MetadataConditionFactory<ActiveProfileCondition> {
    @Override
    public ActiveProfileCondition create(Metadata metadata) {
        if (metadata.profiles() != null && metadata.profiles().length > 0) {
            return new ActiveProfileCondition(Arrays.asList(metadata.profiles()));
        } else return null;
    }
}

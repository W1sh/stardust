package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.builder.Options;

import java.util.Arrays;

public class ActiveProfileConditionFactory implements MetadataConditionFactory<ActiveProfileCondition> {
    @Override
    public ActiveProfileCondition create(Options options) {
        if (options.profiles().length > 0) {
            return new ActiveProfileCondition(Arrays.asList(options.profiles()));
        } else return null;
    }
}

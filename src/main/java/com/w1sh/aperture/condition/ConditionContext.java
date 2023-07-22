package com.w1sh.aperture.condition;

import com.w1sh.aperture.DefaultProviderRegistry;
import com.w1sh.aperture.Environment;

public record ConditionContext(DefaultProviderRegistry registry, Environment environment) {

}
